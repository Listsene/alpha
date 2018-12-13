package com.l.hilaris.alpha.views.multiplayer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.InvitationsClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.l.hilaris.alpha.adapters.SudokuListAdapter;
import com.l.hilaris.alpha.models.SudokuCellData;
import com.l.hilaris.alpha.models.SudokuVariation;
import com.l.hilaris.alpha.views.sudoku.multi.MultiListActivity;
import com.l.hilaris.alpha.views.sudoku.multiplayer.MultiplayerSudokuActivity;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.views.sudoku.singleplayer.SudokuActivity;

public class TeamMultiPlayerMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button search, create;
    final static String TAG = "Suodoku Online";
    final static int Select_Players_Requset = 10000;
    final static int Waiting_Room_Request = 10001;
    final static int Invitation_IN_BOX=10002;
    private java.util.List<SudokuVariation> sudokus = new ArrayList<>();
    private SudokuListAdapter Adapter;
    private SudokuVariation sudoku;

    private RealTimeMultiplayerClient RealtimeMultiplayClient = null;
    // 멀티플레이 시스템에 요청하는 클라이언트
    boolean MultiPlayer = false;
    //멀티플레이 모드 실행
    ArrayList<Participant> CurrentParticipants = null;
    // 현재게임 참가자
    String CurrentMyId = null;
    // 현재게임에 내참가아이디
    String ReceivingInvitationID = null;
    String RoomID = null;
    // 방생성 번호
    RoomConfig Roomconfig;
    // 현재방 구성 .


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_menu);

        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(this);
        RealtimeMultiplayClient = Games.getRealTimeMultiplayerClient(this, gsa);

        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        create = findViewById(R.id.create);
        create.setOnClickListener(this);

        Adapter = new SudokuListAdapter(getApplicationContext(),sudokus);
        createSudokus();
        sudoku = sudokus.get(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                startQuickGame();
                Log.d(TAG, "Please wait ...");
                Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.create:
                // 방생성
                //switchToScreen(R.id.screen_wait);
                Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Please wait ...");
                // 플레이어 목록보여주기
                RealtimeMultiplayClient.getSelectOpponentsIntent(1, 1).addOnSuccessListener(
                        new OnSuccessListener<Intent>() {
                            @Override
                            public void onSuccess(Intent intent) {
                                startActivityForResult(intent, Select_Players_Requset);
                            }
                        }
                ).addOnFailureListener(createFailureListener("error in selecting players"));
                break;
        }
    }
    private OnFailureListener createFailureListener(final String string) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handleException(e, string);
            }
        };
    }
    private void handleException(Exception exception, String details) {
        int status = 0;

        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            status = apiException.getStatusCode();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Select_Players_Requset) {
            //플레이 선택준비 완료시
            handleSelectPlayersResult(resultCode, intent);
        }
        else if (requestCode == Waiting_Room_Request) {
            //대기실
            if (resultCode == Activity.RESULT_OK) {
                // 플레이 시작 준비
                Log.d(TAG, "Start game.");
                startGame(true);
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                //방나갈떄
                leaveRoom();
            }
        }
        else if (requestCode == Invitation_IN_BOX) {
            //초대UI핸들링
            handleInvitationInboxResult(resultCode, intent);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
    void startGame(boolean multiplayer) {
        MultiPlayer = multiplayer;
        //switchToScreen(R.id.screen_game);
        //Intent intent = new Intent(this, MultiListActivity.class);
        Intent intent = new Intent(this, SudokuActivity.class);
        intent.putExtra("sudoku", sudoku);
        startActivity(intent);
    }
    //초대창 핸들
    private void handleInvitationInboxResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "invitation inbox UI cancelled, " + response);
            switchToMainScreen();
            return;
        }
        Log.d(TAG, "Invitation succeeded.");
        Invitation invitation = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);
        // 초대수락
        if (invitation != null) {
            acceptInviteToRoom(invitation.getInvitationId());
        }
    }

    // 초대수락펑션
    void acceptInviteToRoom(String invitationId) {
        Log.d(TAG, "Accepting invitation: " + invitationId);

        Roomconfig = RoomConfig.builder(mRoomUpdateCallback)
                .setInvitationIdToAccept(invitationId)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .build();

        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        resetGameVars();

        RealtimeMultiplayClient.join(Roomconfig)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Joining room Successed");
                    }
                });
    }

    // 방나감.
    void leaveRoom() {
        Log.d(TAG, "Leaving room.");
        stopKeepingScreenOn();
        if (RoomID != null) {
            RealtimeMultiplayClient.leave(Roomconfig, RoomID)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            RoomID = null;
                            Roomconfig = null;
                        }
                    });
            //witchToScreen(R.id.screen_wait);
        } else {
            switchToMainScreen();
        }
    }
    //친구초대버
    private void handleSelectPlayersResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "selecting players cancelled, " + response);
            switchToMainScreen();
            return;
        }
        Log.d(TAG, "Select players succeeded.");

        // 초대리스트
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d(TAG, "Invitee count: " + invitees.size());


        // 자동매치
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
        }
        // 방생성
        Log.d(TAG, "Please wait.. we are creating room");
        //switchToScreen(R.id.screen_wait);
        keepScreenOn();
        resetGameVars();

        Roomconfig = RoomConfig.builder(mRoomUpdateCallback)
                .addPlayersToInvite(invitees)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria).build();
        RealtimeMultiplayClient.create(Roomconfig);
        Log.d(TAG, "Room creation completed");
    }

    void startQuickGame() {
        //1명의 랜덤으로 선택된 적과 게임 시작.
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS, 0);
        //switchToScreen(R.id.screen_wait);
        keepScreenOn();
        resetGameVars();

        Roomconfig = RoomConfig.builder(mRoomUpdateCallback)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();
        RealtimeMultiplayClient.create(Roomconfig);
    }
    // 각 개인스크린
    final static int[] SCREENS = {
            R.id.screen_game, R.id.screen_wait
    };
    int mCurScreen = -1;

    private void createSudokus() {
        SudokuVariation sudoku = new SudokuVariation();
        List<SudokuCellData> cells;
        List<String> solution;
        SudokuCellData cell;

        Gson gson;
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = preferences.edit();

        cells = new ArrayList<>();
        String sudokuCells =
                "4| |9||2||7|1|3|" +
                        "||3||7||2||5|" +
                        "2|7||9||1|4||8|" +
                        "|||1|5||9|7|2|" +
                        "|5||7|4||3|8|1|" +
                        "|||||8|||4|" +
                        "|9||4||2||3|6|" +
                        "|2|||8||5|4|9|" +
                        "3|||5||9|||7|";
        String[] splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        String solCells =
                "4|6|9|8|2|5|7|1|3|" +
                        "8|1|3|6|7|4|2|9|5|" +
                        "2|7|5|9|3|1|4|6|8|" +
                        "6|8|4|1|5|3|9|7|2|" +
                        "9|5|2|7|4|6|3|8|1|" +
                        "7|3|1|2|9|8|6|5|4|" +
                        "5|9|7|4|1|2|8|3|6|" +
                        "1|2|6|3|8|7|5|4|9|" +
                        "3|4|8|5|6|9|1|2|7|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 01");
        sudokus.add(sudoku);

        gson = new Gson();
        String json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        Adapter.notifyDataSetChanged();
    }

    void switchToScreen(int screenId) {
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
        }
        mCurScreen = screenId;

        boolean showInvPopup;
        if (ReceivingInvitationID == null) {
            showInvPopup = false;
        } else if (MultiPlayer) {
            showInvPopup = (mCurScreen == R.id.easy);
        } else {
            showInvPopup = (mCurScreen == R.id.easy || mCurScreen == R.id.screen_game);
        }
        findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }
    void switchToMainScreen() {
        if (RealtimeMultiplayClient != null) {
            switchToScreen(R.id.google_sign_in);
        }
    }
    void showWaitingRoom(Room room) {
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        RealtimeMultiplayClient.getWaitingRoomIntent(room, MIN_PLAYERS)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, Waiting_Room_Request);
                    }
                })
                .addOnFailureListener(createFailureListener("error in show waiting room"));
    }

    void resetGameVars() {
    }

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {
        //방생성시
        @Override
        public void onRoomCreated(int statusCode, Room room) {
            Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e(TAG, "Error in onRoomCreated, status " + statusCode);
                showGameError();
                return;
            }
            // 방 나갈때
            showWaitingRoom(room);
        }

        // 방연결시
        @Override
        public void onRoomConnected(int statusCode, Room room) {
            Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e(TAG, "Error in onRoomConnected, status " + statusCode);
                showGameError();
                return;
            }
            updateRoom(room);
        }
        @Override
        public void onJoinedRoom(int statusCode, Room room) {
            Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e(TAG, "Error in onRoomConnected, status " + statusCode);
                showGameError();
                return;
            }
            showWaitingRoom(room);
        }
        //방나갔을때
        @Override
        public void onLeftRoom(int statusCode, @NonNull String roomId) {
            Log.d(TAG, "onLeftRoom, code " + statusCode);
            switchToMainScreen();
        }
    };

    private String PlayerId;

    private RoomStatusUpdateCallback mRoomStatusUpdateCallback = new RoomStatusUpdateCallback() {
        @Override
        public void onConnectedToRoom(Room room) {
            Log.d(TAG, "onConnectedToRoom.");

            CurrentParticipants = room.getParticipants();
            CurrentMyId= room.getParticipantId(PlayerId);

            if (RoomID == null) {
                RoomID = room.getRoomId();
            }
        }
        //연결끊겼을때
        @Override
        public void onDisconnectedFromRoom(Room room) {
            RoomID = null;
            Roomconfig = null;
            showGameError();
        }
        @Override
        public void onRoomConnecting(Room room) {
            updateRoom(room);
        }
        @Override
        public void onP2PConnected(@NonNull String participant) {
        }
        @Override
        public void onP2PDisconnected(@NonNull String participant) {
        }
        @Override
        public void onPeerDeclined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }
        @Override
        public void onPeersConnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }
        @Override
        public void onPeersDisconnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }
        @Override
        public void onPeerJoined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }
        @Override
        public void onPeerLeft(Room room, @NonNull List<String> peersWhoLeft) {
            updateRoom(room);
        }
        @Override
        public void onRoomAutoMatching(Room room) {
            updateRoom(room);
        }
        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
        }
    };
    void updateRoom(Room room) {
        if (room != null) {
            CurrentParticipants = room.getParticipants();
        }
        if (CurrentParticipants != null) {
            //play 환경상 실행
            //점수등
        }
    }
    OnRealTimeMessageReceivedListener mOnRealTimeMessageReceivedListener = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            byte[] buf = realTimeMessage.getMessageData();
            String sender = realTimeMessage.getSenderParticipantId();
        }
    };
    // 게임취소시 에러표시, 메인으로 복귀
    void showGameError() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.game_problem))
                .setNeutralButton(android.R.string.ok, null).create();
        switchToMainScreen();
    }
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}