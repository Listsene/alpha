package com.k.hilaris.alpha.views.sudoku.multiplayer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.adapters.SudokuGridAdapter;
import com.k.hilaris.alpha.views.sudoku.singleplayer.InputButtonsGridFragment;
import com.k.hilaris.alpha.views.sudoku.singleplayer.SudokuGridFragment;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SudokuActivity extends AppCompatActivity implements InputButtonsGridFragment.InputClicked {
    private Toolbar mToolbar;
    TextView timerTv, scoreTv;
    private int score;
    long fiveMinutes;
    private SudokuGridFragment sudokuGridFragment = new SudokuGridFragment();
    CountDownTimer timer = null;
    String time;

    // For connection with server
    ConnectionToServer connection;
    AsyncTaskForSendingMessage messaging;
    SocketChannel channel;
    Selector selector;
    final static String HOSTNAME = "10.0.2.2";
    final static int PORT = 3000;

    class AsyncTaskForSendingMessage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                while (true){
                    wait(1000);
                    writeMessage(time);
                    break;
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }
        void writeMessage(String str) {
            try {
                channel.write(ByteBuffer.wrap(str.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class ConnectionToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                try {
                    selector = Selector.open();
                    channel = SocketChannel.open();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_CONNECT);
                    channel.connect(new InetSocketAddress(HOSTNAME, PORT));

                } catch (IOException e) {
                    new IOException("Connection failed. Hostname : " + HOSTNAME + ", port : " + PORT, e).printStackTrace();
                }

                while (!Thread.interrupted()) {
                    selector.select();
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();

                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isConnectable()) {
                            connect(key);
                        }
                        if (key.isReadable()) {
                            String str = read(key);
                            Log.d("Received a message", str);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
            return null;
        }
        private String read(SelectionKey key) throws IOException {
            channel = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1000);
            readBuffer.clear();
            int length;
            try {
                length = channel.read(readBuffer);
            } catch (IOException e) {
                key.cancel();
                channel.close();
                return "Reading problem, closing connection";
            }
            if (length == -1) {
                channel.close();
                key.cancel();
                return "Nothing was read from server";
            }
            readBuffer.flip();
            byte[] buff = new byte[1024];
            readBuffer.get(buff, 0, length);
            return new String(buff);
        }
        private void connect(SelectionKey key) throws IOException {
            channel = (SocketChannel) key.channel();
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        }
        private void close() {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public interface onKeyBackPressedListener {
        public void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }
    @Override
    public void onBackPressed(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("time",fiveMinutes);
        editor.commit();
        if(mOnKeyBackPressedListener != null){
            mOnKeyBackPressedListener.onBack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref",0);
        fiveMinutes = sharedPreferences.getLong("time", 300000);

        // server
        connection = new ConnectionToServer();
        connection.execute();
        messaging = new AsyncTaskForSendingMessage();
        messaging.execute();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.SudokuGridFragment, sudokuGridFragment);
        ft.add(R.id.InputButtonsFragment, new InputButtonsGridFragment());
        ft.commit();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        timerTv = findViewById(R.id.timer);
        scoreTv = findViewById(R.id.score);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Timer();
        Score();

        Button newGameButton = findViewById(R.id.newGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                fiveMinutes = 300000;
                Timer();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                sudokuGridFragment.newGame();
                ft.replace(R.id.SudokuGridFragment, sudokuGridFragment = new SudokuGridFragment());
                ft.commit();
            }
        });
    }


    public void Timer(){
        timer = new CountDownTimer(fiveMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                if((TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))<10)){
                    time =  "0"+ (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":0"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                }else{
                    time =  "0"+ (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                }
                timerTv.setText(time);
                fiveMinutes = millis;
//                connection.writeMessage(time);
            }
            public void onFinish() {
                timerTv.setText(getResources().getText(R.string.Timer_Complete));
            }
        }.start();
    }
    public void Score() {
        scoreTv.setText(String.valueOf(score));
//        if(String.valueOf(score).equals("100"))
//            return true;
//        else
//            return false;
    }


    @Override
    public void sendInput(String input){
        sudokuGridFragment = (SudokuGridFragment) getFragmentManager().findFragmentById(R.id.SudokuGridFragment);
        score = sudokuGridFragment.getInput(input);
        Score();
    }
}