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
    final static int Invitation_popup_Reqeust = 10001;
    final static int Waiting_Room_Request = 10002;

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
    void resetGameVars() {
    }
}
