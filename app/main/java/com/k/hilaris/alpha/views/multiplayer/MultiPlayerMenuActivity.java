package com.k.hilaris.alpha.views.multiplayer;

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
import com.l.hilaris.alpha.views.sudoku.multiplayer.MultiplayerSudokuActivity;

import com.k.hilaris.alpha.R;

public class MultiPlayerMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button search, create;
    final static String TAG = "Suodoku Online";
    final static int Select_Players_Requset = 10000;
    final static int Waiting_Room_Request = 10001;

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                startQuickGame();
                break;
            case R.id.create:
                // 방생성
                switchToScreen(R.id.screen_wait);
                // 플레이어 목록보여주기
                RealtimeMultiplayClient.getSelectOpponentsIntent(1, 3).addOnSuccessListener(
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
        super.onActivityResult(requestCode, resultCode, intent);
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
        switchToScreen(R.id.screen_wait);
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
        switchToScreen(R.id.screen_wait);
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
