package com.example.appsocket;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

public class GlobalData {

    private String IP = "192.168.181.30";
    public Vector<String> id_items = new Vector<String>();
    public String getIP(){
        return IP;
    }

    public void addList(String id_data){
        StringTokenizer st = new StringTokenizer(id_data, "$");
        while(st.hasMoreElements()){
            id_items.add(st.nextToken());
        }
    }
    public void resetList(){
        id_items.clear();
    }

    public InputFilter filterAlphaNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };



    public static GlobalData instance =null;

    public static synchronized GlobalData getInstance(){
        if(null == instance){
            instance = new GlobalData();
        }
        return instance;
    }
}
