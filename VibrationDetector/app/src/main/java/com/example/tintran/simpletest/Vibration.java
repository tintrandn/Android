package com.example.tintran.simpletest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.LinkedList;

import ca.uol.aig.fftpack.ComplexDoubleFFT;

public class Vibration extends Activity{
    String TAG = "Vibration";
    int sample_rate = 44100;
    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    double max_magnitude = 0;
    double max_frequency = 0;
    Vibrator vibrator;
    AudioRecord audioRecord;
    Paint xAxisPencil = new Paint();
    Paint barPencilFirst = new Paint();
    Paint barPencilSecond = new Paint();
    Paint peakPencilFirst = new Paint();
    Paint peakPencilSecond = new Paint();
    SimpleWaveform simpleWaveform;
    private ComplexDoubleFFT transformer;
    int blockSize;//1024
    Button vibra_start;
    Button vibra_stop;
    TextView max_freq;
    TextView max_mag;
    CheckBox checkBox;
    boolean isRecoding = false;
    boolean CANCELLED_FLAG = false;
    // Vibrate for 2000 milliseconds
    // Sleep for 3000 milliseconds
    long[] pattern = { 0, 1500, 2000 };
    RecordAudio recordTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vibration);
        blockSize = 1024;
        transformer = new ComplexDoubleFFT(blockSize);
        vibrator = (Vibrator)getSystemService(Vibration.VIBRATOR_SERVICE);
        simpleWaveform = (SimpleWaveform)findViewById(R.id.simplewaveform);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        max_freq = (TextView)findViewById(R.id.max_freq);
        max_mag = (TextView)findViewById(R.id.max_mag);
        max_freq.setTextColor(Color.BLUE);
        max_freq.setTextSize(30);
        max_mag.setTextColor(Color.BLUE);
        max_mag.setTextSize(30);
        Button vibra_start = (Button) findViewById(R.id.vibra_start);
        vibra_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecoding == false) {
                    isRecoding = true;

                    if (checkBox.isChecked())
                        //vibrator.vibrate(15000);
                        vibrator.vibrate(pattern,0);
                    recordTask = new RecordAudio();
                    recordTask.execute();
                    Log.i(TAG, "Start Button");
                }
            }
        });

        Button vibra_stop = (Button)findViewById(R.id.vibra_stop);
        vibra_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecoding) {
                    isRecoding = false;
                    vibrator.cancel();
                    try {
                        audioRecord.stop();
                        Log.i(TAG,"Stop recording");
                    } catch (IllegalStateException e) {
                        Log.e("Stop recording failed", e.toString());
                    }
                    recordTask.cancel(true);
                    Log.i(TAG, "Stop Button");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"On pause");
        if (isRecoding) {
            isRecoding = false;
            vibrator.cancel();
            try {
                audioRecord.stop();
                Log.i(TAG, "Stop recording");
            } catch (IllegalStateException e) {
                Log.e("Stop recording failed", e.toString());
            }
            recordTask.cancel(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class RecordAudio extends AsyncTask<Void, int[], Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            int bufferSize = AudioRecord.getMinBufferSize(sample_rate,
                    channelConfiguration, audioEncoding);
            audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.DEFAULT, sample_rate,
                    channelConfiguration, audioEncoding, bufferSize);
            int bufferReadResult;
            short[] buffer = new short[blockSize];
            double[] toTransform = new double[blockSize*2];
            //Start recording
            try {
                audioRecord.startRecording();
                Log.i(TAG,"Start recording");
            } catch (IllegalStateException e) {
                Log.e("Recording failed", e.toString());
            }
            while (isRecoding ) {
                if (isCancelled()) {

                    isRecoding = false;
                    //publishProgress(cancelledResult);
                    Log.d("doInBackground", "Cancelling the RecordTask");
                    break;
                } else {
                    bufferReadResult = audioRecord.read(buffer, 0, blockSize);
                    Log.i("BufferReadresult", Integer.toString(bufferReadResult));
                    //Apply hann window function
                    for (int i = 0; i < bufferReadResult; i++) {
                        buffer[i] = (short) (buffer[i] * 0.5 * (1.0 - Math.cos(2.0 * Math.PI * i / (buffer.length))));//here i'm getting all data is zero.
                    }
                    //convert buffer to ComplexDoubleFFT
                    for (int i = 0; i < blockSize; i++) {
                        //toTransform[2 * i] = (double) buffer[i];
                        toTransform[2 * i] = (double) buffer[i] / 32768.0; // signed 16 bit
                        toTransform[2 * i + 1] = 0;
                    }
                    transformer.ft(toTransform);
                    //calculate magnitude and frequency
                    StringBuilder sb = new StringBuilder();
                    double magnitude[] = new double[blockSize / 2];
                    int data[] = new int[blockSize /2];
                    for (int i = 0; i < magnitude.length; i++) {
                        double R = toTransform[2 * i] * toTransform[2 * i];
                        double I = toTransform[2 * i + 1] * toTransform[2 * i * 1];
                        magnitude[i] = Math.sqrt(I + R);
                        data[i] = (int)magnitude[i];
                    }
                    int maxIndex = 0;
                    max_magnitude = magnitude[0];
                    for (int i = 1; i < magnitude.length; i++) {
                        if (magnitude[i] > max_magnitude) {
                            max_magnitude = magnitude[i];
                            maxIndex = i;
                        }
                    }
                    max_magnitude = Math.round(magnitude[maxIndex]*100.0)/100.0;
                    max_frequency = maxIndex * sample_rate / blockSize;
                    //display sound wave
                    publishProgress(data);
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(int[]... progress) {
            Log.i("RecordingProgress", "Displaying in progress");
            displaywave(progress[0]);
            max_freq.setText(Double.toString(max_frequency));
            max_mag.setText(Double.toString(max_magnitude));
            Log.i("Check box", Boolean.toString(checkBox.isChecked()));
            Log.i("Max frequency", Double.toString(max_frequency));
            Log.i("Max magnitude", Double.toString(max_magnitude));
        }
    }
    //By maxyou
    private void displaywave(int [] data) {

        //restore default setting, you can omit all following setting and goto the final refresh() show
        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for (int i = 0; i < data.length; i++) {
            ampList.add(data[i]);
        }
        simpleWaveform.setDataList(ampList);//input data to show

        //define bar gap
        simpleWaveform.barGap = 30;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_PARALLEL;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //show x-axis
        simpleWaveform.showXAxis = true;
        xAxisPencil.setStrokeWidth(1);
        xAxisPencil.setColor(0x88ffffff);//the first 0x88 is transparency, the next 0xffffff is color
        simpleWaveform.xAxisPencil = xAxisPencil;

        //define pencil to draw bar
        barPencilFirst.setStrokeWidth(15);
        barPencilFirst.setColor(0xff1dcf0f);
        simpleWaveform.barPencilFirst = barPencilFirst;
        barPencilSecond.setStrokeWidth(15);
        barPencilSecond.setColor(0xff1dcfcf);
        simpleWaveform.barPencilSecond = barPencilSecond;

        //define pencil to draw peaks outline
        peakPencilFirst.setStrokeWidth(5);
        peakPencilFirst.setColor(0xfffe2f3f);
        simpleWaveform.peakPencilFirst = peakPencilFirst;
        peakPencilSecond.setStrokeWidth(5);
        peakPencilSecond.setColor(0xfffeef3f);
        simpleWaveform.peakPencilSecond = peakPencilSecond;

        //the first part will be draw by PencilFirst
        simpleWaveform.firstPartNum = 20;//first 20 bars will be draw by first pencil

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        // set touch listener
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
                simpleWaveform.firstPartNum = progress;//set touch position back to its progress
                simpleWaveform.refresh();
            }
        };
        //show...
        simpleWaveform.refresh();
    }
}