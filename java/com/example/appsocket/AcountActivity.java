package com.example.appsocket;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
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

public class AcountActivity extends AppCompatActivity {
    private Socket socket;
    private int port = 9000;
    EditText addId;
    EditText addPw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);

        addId = (EditText) findViewById(R.id.et_ID);
        addPw = (EditText) findViewById(R.id.et_PW);
        Button NewAButton = (Button) findViewById(R.id.Button_ACC);
        addId.setFilters(new InputFilter[]{GlobalData.getInstance().filterAlphaNum});
        addPw.setFilters(new InputFilter[]{GlobalData.getInstance().filterAlphaNum});

        NewAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SockThread addT = new SockThread();
                String id = addId.getText().toString();
                String pw = addPw.getText().toString();
                if(id.equals("")){
                    Toast.makeText(getApplicationContext(), "ID를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(pw.equals("")){
                    Toast.makeText(getApplicationContext(), "PW를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(id.equals("")){
                    if(pw.equals("")){
                        Toast.makeText(getApplicationContext(), "ID와 PW가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    addT.run();
                    addT.end();
                }

            }
        });

        addId.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    addPw.requestFocus();
                    return true;
                }
                return false;
            }
        });

        addPw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    NewAButton.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(addPw.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    class SockThread extends Thread {
        String id = addId.getText().toString();
        String pw = addPw.getText().toString();
        String regist = "admin$regist$".concat(id).concat("$").concat(pw).concat("$");
        String feedback = "regist";
        String already = "sameID";

        public void run() {


            try { //소켓 생성
                socket = new Socket(GlobalData.getInstance().getIP(), port);

                PrintWriter out = new PrintWriter
                        (new BufferedWriter(new OutputStreamWriter
                                (socket.getOutputStream())), true);
                out.println(regist);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String fb = in.readLine();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fb.equals(feedback)) {
                            Toast.makeText(getApplicationContext(), "new account ".concat(fb).concat("ed"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AcountActivity.this, AdminActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            AcountActivity.this.startActivity(intent);
                        }
                        else if(fb.equals(already)){
                            Toast.makeText(getApplicationContext(), "Same ID already registered", Toast.LENGTH_SHORT).show();
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
