package com.example.srsfinalandroidproject;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import 	android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech mTTS;
    private EditText mEditText;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private Button mButtonSpeak;
    private ImageView imgVw;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonSpeak = findViewById(R.id.speakButton);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        mButtonSpeak.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String arg0) {
                imgVw.setImageResource(R.drawable.background);
            }
            @Override
            public void onError(String arg0) {
            }
            @Override
            public void onStart(String arg0) {
            }
        });
        mEditText = (EditText) findViewById(R.id.et);
        mSeekBarPitch = findViewById(R.id.pitchseek);
        mSeekBarSpeed = findViewById(R.id.speedseek);
        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mEditText.getText().toString().equals(""))) {
                    imgVw = findViewById(R.id.imageView);
                    imgVw.setImageResource(R.drawable.audio);
                    speak();
                }
            }
        });
    }
    private void speak() {
        String text = mEditText.getText().toString();
        float pitch = (float) mSeekBarPitch.getProgress() / 50;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (float) mSeekBarSpeed.getProgress() / 50;
        if (speed < 0.1) speed = 0.1f;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        i=1;
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            imgVw.setImageResource(R.drawable.audio);
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}