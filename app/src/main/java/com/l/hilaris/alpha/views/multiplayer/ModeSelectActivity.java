package com.l.hilaris.alpha.views.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.l.hilaris.alpha.R;

public class ModeSelectActivity  extends AppCompatActivity implements View.OnClickListener {

        private Button team, versus, team_match;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_multiplay_mode_select_menu);

            team= findViewById(R.id.button);
            team.setOnClickListener(this);
            versus = findViewById(R.id.button2);
            versus.setOnClickListener(this);
            team_match=findViewById(R.id.button3);
            team_match.setOnClickListener(this);
        }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                //team
                //team search create 화면으로
                Intent intent = new Intent(this, TeamMultiPlayerMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                //versus
                //versus search create 화면으로
                intent = new Intent(this,VersusMultiplayerMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.button3:
                //team match
                //team match search create 화면으로
                intent = new Intent(this,TeamMatchMultiplayerMenuActivity.class);
                startActivity(intent);
                break;
        }
    }
}
