package com.example.tintran.simpletest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by TIN TRAN on 23-Apr-17.
 */
public class RBG extends Activity {
    LinearLayout linearLayout;
    LinearLayout linearLayout1;
    Handler handler;
    String TAG = "RBG";
    String Status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Start RBG test");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rbgv);
        linearLayout = (LinearLayout) findViewById(R.id.line_lay);
        linearLayout1 = (LinearLayout) findViewById(R.id.line_lay1);
        Status = "Start Red";
        //process to change background color
         handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.obj.toString()) {
                    case "RED":
                        Log.i(TAG,Status);
                        linearLayout.setBackgroundColor(Color.RED);
                        Status = "Start Blue";
                        break;
                    case "BLUE":
                        Log.i(TAG,Status);
                        linearLayout.setBackgroundColor(Color.BLUE);
                        Status = "Start Green";
                        break;
                    case "GREEN":
                        Log.i(TAG,Status);
                        linearLayout.setBackgroundColor(Color.GREEN);
                        Status = "Start Bla/Whi";
                        break;
                    case "BLA/WHI":
                        Log.i(TAG,Status);
                        linearLayout1.setVisibility(View.VISIBLE);
                        Status = "Finish";
                        break;
                    case "FINISH":
                        Log.i(TAG,Status);
                        Intent vibra_intent = new Intent(RBG.this,Vibration.class);
                        startActivity(vibra_intent);
                        break;
                }
            }
        };
        startTest();
    }
    //handler key press

    @Override
    protected void onResume() {
        Status = "Start Red";
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            Log.i(TAG,"Volume down press");
            startTest();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Log.i(TAG,"Key back press");
            finish();
            return true;
        }
        return false;
    }


    private void startTest() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                switch (Status) {
                    case "Start Red":
                        msg.obj = "RED";
                        break;
                    case "Start Blue":
                        msg.obj = "BLUE";
                        break;
                    case "Start Green":
                        msg.obj = "GREEN";
                        break;
                    case "Start Bla/Whi":
                        msg.obj = "BLA/WHI";
                        break;
                    case "Finish":
                        msg.obj = "FINISH";
                        break;
                }
                //send message to Mainthread
                handler.sendMessage(msg);
            }
        });thread.start();
    }
}
