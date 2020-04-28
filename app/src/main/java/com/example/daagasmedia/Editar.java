package com.example.daagasmedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

public class Editar extends AppCompatActivity {

    private static final int FILE_OPEN = 301;
    private static final int IMAGE_EDIT = 302;
    private static final int VIDEO_EDIT = 303;
    public static final String DIR_SEPAR = File.separator;
    private static final String APP_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath();

    private static Bitmap originalImage;
    private static int trimStartPoint;
    private static int trimEndPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBarEditar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton botonStartPoint = findViewById(R.id.videoStart);
        botonStartPoint.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                VideoView vistaVideo = findViewById(R.id.verVideo);
                trimStartPoint = vistaVideo.getCurrentPosition();
                Toast.makeText(Editar.this, "Set START point in " +trimStartPoint, Toast.LENGTH_SHORT).show();
            }
        } );

        ImageButton botonEndPoint = findViewById(R.id.videoEnd);
        botonEndPoint.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                VideoView vistaVideo = findViewById(R.id.verVideo);
                trimEndPoint = vistaVideo.getCurrentPosition();
                Toast.makeText(Editar.this, "Set END point in " +trimEndPoint, Toast.LENGTH_SHORT).show();
            }
        } );

        ImageButton botonVideoSave = findViewById(R.id.videoSave);
        botonVideoSave.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (trimStartPoint>trimEndPoint) {
                    Toast.makeText(Editar.this, "Error: el punto de inicio es mayor que el final ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Editar.this, "El vídeo debería guardarse correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        } );

        ImageButton botonVideoCancel = findViewById(R.id.videoCancel);
        botonVideoCancel.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                VideoView vistaVideo = findViewById(R.id.verVideo);
                trimStartPoint = 0;
                trimEndPoint = vistaVideo.getDuration();
                Toast.makeText(Editar.this, "RESET: Start in " +trimStartPoint+ " and end in " +trimEndPoint, Toast.LENGTH_SHORT).show();
            }
        } );

        ImageButton botonZoomIn = findViewById(R.id.zoomIn);
        botonZoomIn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ImageView vistaImagen = findViewById(R.id.verImagen);
                Bitmap imagenWork = ((BitmapDrawable)vistaImagen.getDrawable()).getBitmap();
                if (imagenWork!=null) {
                    int workX = imagenWork.getWidth() * 2;
                    int workY = imagenWork.getHeight() * 2;
                    if ( workX<5000 && workY<5000 ) {
                        Bitmap imagenFinal = Bitmap.createScaledBitmap(imagenWork, workX, workY, false);
                        vistaImagen.setImageBitmap(imagenFinal);
                        Toast.makeText(Editar.this, "Image enlarged (x2)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Editar.this, "Error: size great than max allowed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Editar.this, "Error: null image", Toast.LENGTH_SHORT).show();
                }
            }
        } );

        ImageButton botonZoomOut = findViewById(R.id.zoomOut);
        botonZoomOut.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ImageView vistaImagen = findViewById(R.id.verImagen);
                Bitmap imagenWork = ((BitmapDrawable)vistaImagen.getDrawable()).getBitmap();
                if (imagenWork!=null) {
                    int workX = imagenWork.getWidth() / 2;
                    int workY = imagenWork.getHeight() / 2;
                    if (workX>100 && workY>100) {
                        Bitmap imagenFinal = Bitmap.createScaledBitmap(imagenWork, workX, workY, false);
                        vistaImagen.setImageBitmap(imagenFinal);
                        Toast.makeText(Editar.this, "Image reduced (/2)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Editar.this, "Error: size less than max allowed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Editar.this, "Error: null image", Toast.LENGTH_SHORT).show();
                }
            }
        } );

        ImageButton botonSave = findViewById(R.id.imageSave);
        botonSave.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ImageView vistaImagen = findViewById(R.id.verImagen);
                Bitmap imagenWork = ((BitmapDrawable)vistaImagen.getDrawable()).getBitmap();
                if (imagenWork!=null) {
                    String setDif = setDiffName();
                    File newimageFile = new File(APP_FOLDER, "EditedPhoto" + setDif + ".jpg");
                    try {
                        FileOutputStream out = new FileOutputStream(newimageFile);
                        imagenWork.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) { e.printStackTrace(); }
                    Toast.makeText(Editar.this, "Image saved as "+APP_FOLDER+DIR_SEPAR+"EditedPhoto" + setDif + ".jpg", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Editar.this, "Error: null image", Toast.LENGTH_SHORT).show();
                }
            }
        } );

        ImageButton botonCancel = findViewById(R.id.imageCancel);
        botonCancel.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (originalImage!=null) {
                    ImageView vistaImagen = findViewById(R.id.verImagen);
                    vistaImagen.setImageBitmap(originalImage);
                    Toast.makeText(Editar.this, "Image restored to original size", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Editar.this, "Error: null bitmap", Toast.LENGTH_SHORT).show();
                }
            }
        } );

        if (savedInstanceState == null) {
            selectFile();
        }
    }

    public void selectFile()
    {
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
            ContentResolver cR = this.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getExtensionFromMimeType(cR.getType(fileUri));
            mime.getExtensionFromMimeType(cR.getType(fileUri));
            String elementType = cR.getType(fileUri);
            String imageType = "image/jpeg";
            String imageNoBGType = "image/png";
            String videoType = "video/mp4";
            if ( elementType.equals(imageType) ) {
                ImageView vistaImagen = findViewById(R.id.verImagen);
                vistaImagen.setImageURI(fileUri);
                LinearLayout imageBox = findViewById(R.id.ImageBox);
                imageBox.setVisibility(View.VISIBLE);
                originalImage = ((BitmapDrawable)vistaImagen.getDrawable()).getBitmap();
                LinearLayout videoBox = findViewById(R.id.VideoBox);
                videoBox.setVisibility(View.INVISIBLE);
            } else if (elementType.equals(imageNoBGType)){
                ImageView vistaImagen = findViewById(R.id.verImagen);
                vistaImagen.setImageURI(fileUri);
                LinearLayout imageBox = findViewById(R.id.ImageBox);
                imageBox.setVisibility(View.VISIBLE);
                originalImage = ((BitmapDrawable)vistaImagen.getDrawable()).getBitmap();
                LinearLayout videoBox = findViewById(R.id.VideoBox);
                videoBox.setVisibility(View.INVISIBLE);
            }else if ( elementType.equals(videoType) ) {
                VideoView vistaVideo = findViewById(R.id.verVideo);
                vistaVideo.setVideoURI(fileUri);
                vistaVideo.start();

                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(vistaVideo);
                vistaVideo.setMediaController(mediaController);
                LinearLayout videoBox = findViewById(R.id.VideoBox);
                videoBox.setVisibility(View.VISIBLE);

                LinearLayout imageBox = findViewById(R.id.ImageBox);
                imageBox.setVisibility(View.INVISIBLE);

                trimStartPoint = 0;
                trimEndPoint = vistaVideo.getDuration();
                Toast.makeText(Editar.this, "Start in " +trimStartPoint+ " and end in " +trimEndPoint, Toast.LENGTH_SHORT).show();
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

    private String setDiffName(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = new Date();
        long millis = date.getTime();

        String result = ""+ millis;

        return result;
    }
}