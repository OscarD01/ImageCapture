package com.example.imagecapture;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private Button btnPhoto;
    private Button btnLoad;
    private ImageView imgView;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView) findViewById(R.id.imgView);

        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fes també un botó per recuperar la imatge guardada i mostrar-la a la ImageView.
                // La forma més simple és amb la funció ImageView.setImageURI i assignar-li la Uri de l'arxiu.
                imgView.setImageURI(Uri.parse(getFilesDir().getPath() + "/photo.jpeg"));
            }
        });

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imageBitmap);
            savePhoto(imageBitmap);
        }
    }

    private void savePhoto(Bitmap imageBitmap){
        // Crea un File a la carpeta local de la app: la carpeta local la pots aconseguir amb Context.getDataDir.
        File photoFile = new File(getApplicationContext().getFilesDir(), "photo" + ".jpeg");
        try {
            // Crea un FileOutputStream associat al File
            FileOutputStream fos = new FileOutputStream(photoFile);
            // Volca el contingut del thumbnail amb Bitmap.compress
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}