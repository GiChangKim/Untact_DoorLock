package com.example.appsocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class FuncActivity extends AppCompatActivity {
    private Socket socket;
    private int port = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func);

        Button OpenButton = (Button)findViewById(R.id.button_Open);
        Button CloseButton = (Button)findViewById(R.id.button_Close);
        Button OutButton = (Button)findViewById(R.id.button_Out);

        OpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenThread openT = new OpenThread();
                openT.run();
                openT.end();

            }
        });

        CloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseThread cloT = new CloseThread();
                cloT.run();
                cloT.end();

            }
        });

        OutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutThread outT = new OutThread();
                outT.run();
                outT.end();

            }
        });
    }


    class OpenThread extends Thread{

        public void run() {
            try { //소켓 생성

                socket = new Socket(GlobalData.getInstance().getIP(), port);

                PrintWriter out = new PrintWriter
                        (new BufferedWriter( new OutputStreamWriter
                                (socket.getOutputStream())),true);
                out.println("general$open$");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String fb = in.readLine();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "door ".concat(fb), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            catch (UnknownHostException uhe) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "호스트 식별 불가", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (IOException ioe) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "네트워크 응답 없음", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (SecurityException se) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "보안 위반 발생", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (IllegalArgumentException le) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "범위 밖의 포트", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

        public void end(){
            try{//소켓 종료
                socket.close();
            }
            catch (IOException ioe){
            }
        }
    }

    class CloseThread extends Thread{

        public void run() {
            try { //소켓 생성
                socket = new Socket(GlobalData.getInstance().getIP(), port);

                PrintWriter out = new PrintWriter
                        (new BufferedWriter( new OutputStreamWriter
                                (socket.getOutputStream())),true);
                out.println("general$close$");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String fb = in.readLine();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "door ".concat(fb), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (UnknownHostException uhe) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "호스트 식별 불가", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (IOException ioe) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "네트워크 응답 없음", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (SecurityException se) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "보안 위반 발생", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (IllegalArgumentException le) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "범위 밖의 포트", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

        public void end(){
            try{//소켓 종료
                socket.close();
            }
            catch (IOException ioe){
            }
        }
    }

    class OutThread extends Thread{

        public void run() {
            try { //소켓 생성
                socket = new Socket(GlobalData.getInstance().getIP(), port);

                PrintWriter out = new PrintWriter
                        (new BufferedWriter( new OutputStreamWriter
                                (socket.getOutputStream())),true);
                out.println("general$logout$");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String fb = in.readLine();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), fb, Toast.LENGTH_SHORT).show();
                        if(fb.equals("logout")){
                            Intent intent =new Intent(FuncActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            FuncActivity.this.startActivity(intent);
                        }

                    }
                });
            }

            catch (UnknownHostException uhe) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "호스트 식별 불가", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (IOException ioe) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "네트워크 응답 없음", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (SecurityException se) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "보안 위반 발생", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (IllegalArgumentException le) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "범위 밖의 포트", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

        public void end(){
            try{//소켓 종료
                socket.close();
            }
            catch (IOException ioe){
            }
        }
    }
}