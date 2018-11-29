package com.k.hilaris.alpha.views.sudoku.multiplayer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.adapters.SudokuGridAdapter;
import com.k.hilaris.alpha.models.SudokuVariation;
import com.k.hilaris.alpha.utilities.ConnectionToServer;
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

public class MultiplayerSudokuActivity extends AppCompatActivity implements InputButtonsGridFragment.InputClicked {
    private Toolbar mToolbar;
    TextView timerTv, scoreTv;
    private int score;
    long fiveMinutes;
    private MultiplayerSudokuGridFragment sudokuGridFragment = new MultiplayerSudokuGridFragment();
    private InputButtonsGridFragment inputButtonsGridFragment = new InputButtonsGridFragment();
    CountDownTimer timer = null;
    boolean isFinish;

    // For connection with server
    Server connection;
    SocketChannel channel;
    Selector selector;
    //final static String HOSTNAME = "10.0.2.2"; // emulator
    final static String HOSTNAME = "ec2-13-209-98-37.ap-northeast-2.compute.amazonaws.com";
    final static int PORT = 3000;

    class Server extends AsyncTask<Void, Void, Void> {
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
                        /*if(!str.equals(score)) {
                            String nonStrange = str.replaceAll("\\p{Cntrl}", ""); // gets rid of special character (diamond question mark)
                            score = Integer.valueOf(nonStrange);
                            Score();
                        }*/
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
        public void writeMessage(String str) {
            try {
                channel.write(ByteBuffer.wrap(str.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        void onBack();
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
        editor.putInt("score", score);
        editor.apply();
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
        score = sharedPreferences.getInt("score",0);
        isFinish = false;

        SudokuVariation sudoku = (SudokuVariation) getIntent().getSerializableExtra("sudoku");
        getIntent().putExtra("sudoku", sudoku);

        // server
        connection = new Server();
        connection.execute();

        // Dangerous, allows main thread to execute on thing.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.SudokuGridFragment, sudokuGridFragment);
        ft.add(R.id.InputButtonsFragment, inputButtonsGridFragment);
        ft.commit();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        timerTv = findViewById(R.id.timer);
        scoreTv = findViewById(R.id.score);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Timer();
        scoreTv.setText(String.valueOf(score));

        Button newGameButton = findViewById(R.id.newGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGrid();
                resetTimer();
                resetScore();
                isFinish=false;
                putIsFinish();
            }
        });
    }

    public void resetTimer(){
        timer.cancel();
        fiveMinutes = 300000;
        Timer();
    }
    public void resetGrid(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        sudokuGridFragment.newGame();
        ft.replace(R.id.SudokuGridFragment, sudokuGridFragment = new MultiplayerSudokuGridFragment());
        ft.replace(R.id.InputButtonsFragment, inputButtonsGridFragment = new InputButtonsGridFragment());
        ft.commit();
    }
    public void resetScore(){
        score = 0;
        scoreTv.setText(String.valueOf(score));
    }
    public void Timer(){
        timer = new CountDownTimer(fiveMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                isFinish = false;
                putIsFinish();
                sudokuGridFragment.getAdapter().notifyThis();
                inputButtonsGridFragment.getAdapter().notifyThis();
                long millis = millisUntilFinished;
                String time;
                if((TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))<10)){
                    time =  "0"+ (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":0"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                }else{
                    time =  "0"+ (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                }
                timerTv.setText(time);
                fiveMinutes = millis;
            }
            public void onFinish() {
                timerTv.setText(getResources().getText(R.string.Timer_Complete));
                isFinish=true;
                putIsFinish();
                sudokuGridFragment.getAdapter().notifyThis();
                inputButtonsGridFragment.getAdapter().notifyThis();
            }
        }.start();
    }
    public void Score() {
        scoreTv.setText(String.valueOf(score));
    }

    @Override
    public void sendInput(String input){
        sudokuGridFragment = (MultiplayerSudokuGridFragment) getFragmentManager().findFragmentById(R.id.SudokuGridFragment);
        //score = sudokuGridFragment.getInput(input);
        if(score != sudokuGridFragment.getInput(input)) {
            score = sudokuGridFragment.getInput(input);
            try {
                channel.write(ByteBuffer.wrap(String.valueOf(score).getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Score();
        }
    }
    public void putIsFinish(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFinish",isFinish);
        editor.commit();
    }
}