package com.example.appsocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.StringTokenizer;
import java.util.Vector;

public class AdminActivity extends AppCompatActivity {
    private Socket socket;
    private int port = 9000;
    ArrayAdapter<String> adapter;
    ListView ALV;
    String checked_Item;
    int checked_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button addButton = (Button)findViewById(R.id.butrton_add);
        Button delButton = (Button)findViewById(R.id.button_del);
        Button outButton = (Button)findViewById(R.id.button_out);
        ALV = (ListView)findViewById(R.id.accList);



        ALV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checked_pos = ALV.getCheckedItemPosition();
                checked_Item = (String) adapterView.getAdapter().getItem(i);
            }

        });

        addButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent =new Intent(AdminActivity.this,AcountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                AdminActivity.this.startActivity(intent);
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DregThread dreT = new DregThread();
                dreT.run();
                dreT.end();


            }
        });

        outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutThread outT = new OutThread();
                outT.run();
                outT.end();
            }
        });

        try {//소켓 생성
            String give_me = "id_list$";
            socket = new Socket(GlobalData.getInstance().getIP(), port);

            PrintWriter out = new PrintWriter
                    (new BufferedWriter(new OutputStreamWriter
                            (socket.getOutputStream())), true);
            out.println(give_me);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            GlobalData.getInstance().resetList();
            String fb = in.readLine();
            GlobalData.getInstance().addList(fb);
            adapter = new ArrayAdapter<String>(AdminActivity.this, android.R.layout.simple_list_item_single_choice, GlobalData.getInstance().id_items);
            ALV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            ALV.setAdapter(adapter);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class DregThread extends Thread{

        public void run() {
            try { //소켓 생성
                socket = new Socket(GlobalData.getInstance().getIP(), port);

                PrintWriter out = new PrintWriter
                        (new BufferedWriter( new OutputStreamWriter
                                (socket.getOutputStream())),true);
                out.println("admin$deregist$".concat(checked_Item).concat("$"));

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String fb = in.readLine();
                String dere = "deregist";
                if(fb.equals(dere)){
                    int count;
                    count = adapter.getCount() ;

                    if (count > 0) {

                        if (checked_pos > -1 && checked_pos < count) {
                            GlobalData.getInstance().id_items.remove(checked_pos);
                            ALV.clearChoices();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), fb.concat(" account"), Toast.LENGTH_SHORT).show();
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
                            Intent intent =new Intent(AdminActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            AdminActivity.this.startActivity(intent);
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
