package com.example.daagasmedia;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Visualizar extends AppCompatActivity {

    private static final int FILE_OPEN = 201;
    private static final int IMAGE_PLAY = 202;
    private static final int VIDEO_PLAY = 203;
    private static final String APP_FOLDER = Environment.getDownloadCacheDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBarVisualizar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        openFolder();

        Button botonCambiar = findViewById(R.id.cambiarElemento);
        botonCambiar.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openFolder();
            }
        } );
    }

    public void openFolder(){
        Intent intentFileDiag = new Intent( Intent.ACTION_OPEN_DOCUMENT ).addCategory( Intent.CATEGORY_OPENABLE ).setType( "*/*" );
        try {
            startActivityForResult( Intent.createChooser(intentFileDiag, "Selecciona Archivo a visualizar"), FILE_OPEN);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Open.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentData)
    {
        finishActivity(FILE_OPEN);
        super.onActivityResult( requestCode, resultCode, intentData );

        if( requestCode == FILE_OPEN && resultCode == RESULT_OK )
        {
            Uri fileUri = intentData.getData();
            ContentResolver contentResolver = this.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
            String elementType = contentResolver.getType(fileUri);
            String imageType = "image/jpeg";
            String imageNoBGType = "image/png";
            String videoType = "video/mp4";

            if ( elementType.equals(imageType) ) {
                ImageView vistaImagen = findViewById(R.id.verImagen);
                vistaImagen.setImageURI(fileUri);
                vistaImagen.setVisibility(View.VISIBLE);
                VideoView vistaVideo = findViewById(R.id.verVideo);
                vistaVideo.setVisibility(View.INVISIBLE);
            } else if ( elementType.equals(imageNoBGType) ) {
                ImageView vistaImagen = findViewById(R.id.verImagen);
                vistaImagen.setImageURI(fileUri);
                vistaImagen.setVisibility(View.VISIBLE);
                VideoView vistaVideo = findViewById(R.id.verVideo);
                vistaVideo.setVisibility(View.INVISIBLE);
            } else if ( elementType.equals(videoType) ) {
                VideoView vistaVideo = findViewById(R.id.verVideo);
                vistaVideo.setVideoURI(fileUri);
                vistaVideo.start();
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(vistaVideo);
                vistaVideo.setMediaController(mediaController);
                vistaVideo.setVisibility(View.VISIBLE);
                ImageView vistaImagen = findViewById(R.id.verImagen);
                vistaImagen.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent( getBaseContext(), MainActivity.class );
                startActivity( intent );
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
