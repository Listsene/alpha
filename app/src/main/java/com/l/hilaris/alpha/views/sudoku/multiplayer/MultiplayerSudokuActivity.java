package com.l.hilaris.alpha.views.sudoku.multiplayer;

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

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuVariation;
import com.l.hilaris.alpha.views.sudoku.InputButtonsGridFragment;
import com.l.hilaris.alpha.views.sudoku.SudokuBaseActivity;
import com.l.hilaris.alpha.views.sudoku.SudokuGridFragment;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MultiplayerSudokuActivity extends SudokuBaseActivity implements InputButtonsGridFragment.InputClicked {
    // For connection with server
    protected Server connection;
    protected SocketChannel channel;
    protected Selector selector;
    protected String uniqueID;
    protected final static String HOSTNAME = "ec2-13-209-98-37.ap-northeast-2.compute.amazonaws.com";
    protected final static int PORT = 3000;

    protected class Server implements Runnable {

        @Override
        public void run() {
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
                            str = str.replaceAll("\\p{Cntrl}", ""); // gets rid of special character (diamond question mark)
                            if(str.length() > 30) {
                                if(!str.substring(0,36).equals(uniqueID)) {
                                    str = str.replace(str.substring(0, 36), "v");
                                }
                                else
                                    str = str.replace(uniqueID, "s");
                            }
                            String type = str.substring(0, 1);
                            if(type.equals("s")) {
                                str = str.replaceFirst("s", "");
                                if(!str.equals(score)) {
                                    score = Integer.valueOf(str);
                                    Score();
                                }
                            }
                            else if(type.equals("v")) {
                                str = str.replaceFirst("v", "");
                                score2 = Integer.valueOf(str);
                                Score();
                            }
                            else if(type.equals("i")) {
                                str = str.replaceFirst("i", "");
                                if(str.length() > 2) {
                                    updateSudoku(str.substring(0,1), str.substring(1,3));
                                }
                                else {
                                    updateSudoku(str.substring(0,1), str.substring(1,2));
                                }
                            }
                            Log.d("Received a message", str);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GUID to identify app instance
        uniqueID = UUID.randomUUID().toString();

        // server
        connection = new Server();
        new Thread(connection).start();
        //connection.execute();

        // Dangerous, allows asynctask to execute on main thread.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connection = new Server();
        new Thread(connection).start();
    }

    protected void updateSudoku(String input, String position) {
        int pos = Integer.valueOf(position);
        Sudoku.getCells().get(pos).clearMemo();
        Sudoku.getCells().get(pos).setSolved(true);
        Sudoku.getCells().get(pos).setInput(input);
    }

    @Override
    public void sendInput(String input){
        sudokuGridFragment = (SudokuGridFragment) getFragmentManager().findFragmentById(R.id.SudokuGridFragment);
        SudokuVariation sudoku = sudokuGridFragment.getInput(input);
        if (score != sudoku.getScore()) { // checks if score is changed
            score = sudoku.getScore();
            //channel.write(ByteBuffer.wrap(String.valueOf(score).getBytes()));
            connection.writeMessage("s" + String.valueOf(score));
            Score();
        }
        String cell = sudoku.getCells().get(sudoku.getPosition()).getInput();
        if (!(cell.isEmpty() || cell.matches("\\s"))) { // checks if new solved cell
            connection.writeMessage("i" + String.valueOf(cell) + String.valueOf(sudoku.getPosition()));
        }
    }
}