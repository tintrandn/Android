package com.example.tintran.barcodereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.w3c.dom.Text;

/**
 * Created by tintran on 24-Dec-17.
 */

public class MainActivity extends Activity {

    TextView barcodeResult;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button scanBarcode = (Button) findViewById(R.id.scan_barcode);
        barcodeResult = (TextView) findViewById(R.id.barcode_result);
        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ScanBarcodeActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if (data !=null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeResult.setText(barcode.displayValue);
                }
                else{
                    barcodeResult.setText("No detect");
                }
            }
        }
    }
}
