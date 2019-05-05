package com.example.tintran.simpletest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SimpleTest extends Activity {
    String TAG = "SimpleTest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
    Button st_but = (Button) findViewById(R.id.st_but);
        st_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rbgv_intent = new Intent(SimpleTest.this,RBG.class);
                startActivity(rbgv_intent);
                Log.i(TAG,"Start Button");
            }
        });
    }
}
