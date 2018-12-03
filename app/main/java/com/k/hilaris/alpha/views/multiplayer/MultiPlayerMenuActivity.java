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
                // TODO
                break;
            case R.id.create:
                // TODO
                break;

        }
    }
}
