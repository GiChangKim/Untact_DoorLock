package com.example.appsocket;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private Socket socket;
    private int port = 9000;
    EditText loginId;
    EditText loginPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginId = (EditText) findViewById(R.id.ID);
        loginPw = (EditText) findViewById(R.id.PW);
        Button loginButton = (Button) findViewById(R.id.button_In);
        loginId.setFilters(new InputFilter[]{GlobalData.getInstance().filterAlphaNum});
        loginPw.setFilters(new InputFilter[]{GlobalData.getInstance().filterAlphaNum});

        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SockThread sockT = new SockThread();
                sockT.run();
                sockT.end();

            }
        });

        loginId.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    loginPw.requestFocus();
                    return true;
                }
                return false;
            }
        });

        loginPw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    loginButton.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(loginPw.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    class SockThread extends Thread {
        public void run() {
            String id = loginId.getText().toString();
            String pw = loginPw.getText().toString();
            String login = "login$".concat(id).concat("$").concat(pw).concat("$");
            String admin = "adminstrator";
            String general = "general";
            String inv_pw = "invalid_pwd";
            String inv_acc = "invalid_acc";
            String fb;


            try { //소켓 생성
                socket = new Socket(GlobalData.getInstance().getIP(), port);

                PrintWriter out = new PrintWriter
                        (new BufferedWriter(new OutputStreamWriter
                                (socket.getOutputStream())), true);
                out.println(login);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                        fb = in.readLine();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fb.equals(admin)) {
                            Toast.makeText(getApplicationContext(), "Welcome " + fb, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            MainActivity.this.startActivity(intent);
                        } else if (fb.equals(general)) {
                            Toast.makeText(getApplicationContext(), "Welcome " + fb.concat(" user"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, FuncActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            MainActivity.this.startActivity(intent);
                        } else if (fb.equals(inv_acc)){
                            Toast.makeText(getApplicationContext(), " 존재하지 않는 계정 입니다.", Toast.LENGTH_SHORT).show();
                        } else if (fb.equals(inv_pw)){
                            Toast.makeText(getApplicationContext(), "잘못된 비밀번호 입니다.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            } catch (UnknownHostException uhe) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "호스트 식별 불가", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException ioe) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "네트워크 응답 없음", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (SecurityException se) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "보안 위반 발생", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IllegalArgumentException le) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "범위 밖의 포트", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }

        public void end() {
            try {//소켓 종료

                socket.close();
            } catch (IOException ioe) {
            }
        }
    }
}
