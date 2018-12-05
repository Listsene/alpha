package com.a.hilaris.alpha.utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ConnectionToServer extends AsyncTask<Void, Void, Void> {
    SocketChannel channel;
    Selector selector;

    //final static String HOSTNAME = "10.0.2.2"; // emulator
    final static String HOSTNAME = "ec2-13-209-98-37.ap-northeast-2.compute.amazonaws.com";
    final static int PORT = 3000;
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
