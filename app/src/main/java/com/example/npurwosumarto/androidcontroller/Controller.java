package com.example.npurwosumarto.androidcontroller;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controller extends AppCompatActivity {

    ServerSocket serverSocket;
    Socket clientSocket;
    public static final String TABLET_IP = "172.30.71.2";
    private static final int PORT1 = 1300;
    private static final int PORT2 = 1301;
    PrintWriter out;
    BufferedReader in;
    double rotation = 0;

    String command = "STOP";
    //    Transciever tr;
    boolean done = false;
    boolean isConnected = false;

    private GLRenderer mRenderer = new GLRenderer();
    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            serverSocket = new ServerSocket(PORT2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!isConnected) {
            try {
                Log.d("test", "attempting to connect");
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                isConnected = true;
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        Log.d("test", "onCreate: connected successfully");

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this, mRenderer);
        setContentView(mGLView);

        mRenderer.setConnection(out, in);
        Log.d("test", "finished");

    }


//    class ClientThread implements Runnable {
//
//        @Override
//        public void run() {
//            PrintWriter out = null;
//            BufferedReader in = null;
//            while(true) {
//                try {
//                    Log.d("test", "attempting to connect");
//                    clientSocket = serverSocket.accept();
//                    out = new PrintWriter(clientSocket.getOutputStream(), true);
//                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                } catch (UnknownHostException e1) {
//                    e1.printStackTrace();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//                while (true) {
//                    try {
//                        Log.d("test", "read line");
//                        readLine(in.readLine());
//                        SystemClock.sleep(100);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
}


