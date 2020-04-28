package com.example.daagasmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Cargar extends AppCompatActivity {

    private Button buttonFoto, buttonVideo;
    private static final int VIDEO_CAPTURE = 101, IMAGE_CAPTURE = 102;
    private Uri fileUri;
    private File mediaFile;
    public static final String DIR_SEPAR = File.separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar);

        buttonFoto = findViewById(R.id.buttonFoto);
        buttonVideo = findViewById(R.id.buttonVideo);

        buttonFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makePhoto(v);
            }
        });

        buttonVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRecording(v);
            }
        });
    }

    public void startRecording(View view)
    {
        File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath());
        mediaFile = new File(dir.getAbsolutePath() + DIR_SEPAR + "Video" + setDiffName() + ".mp4");

        fileUri = Uri.fromFile(mediaFile);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFile);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    public void makePhoto(View view){
        File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath());
        mediaFile = new File(dir.getAbsolutePath() + DIR_SEPAR + "Photo" + setDiffName() + ".jpg");

        fileUri = Uri.fromFile(mediaFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFile);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video has been saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == IMAGE_CAPTURE){
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Photo has been saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Photo capture cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to capture photo",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private String setDiffName(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = new Date();
        long millis = date.getTime();

        String result = ""+ millis;

        return result;
    }
}
