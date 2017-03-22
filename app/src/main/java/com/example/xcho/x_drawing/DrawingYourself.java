package com.example.xcho.x_drawing;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class DrawingYourself extends AppCompatActivity implements View.OnClickListener {

    private static int RESULT_LOAD_IMAGE = 1;

    ImageView drawing1, drawing2, drawing3, drawing4, drawing5, drawing6, drawing7, drawing8, drawing9;
    ImageView addNewImage;

    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_yourself);

        initViews();
    }

    private void initViews() {
        drawing1 = (ImageView) findViewById(R.id.drawing1);
        drawing1.setOnClickListener(this);
        drawing2 = (ImageView) findViewById(R.id.drawing2);
        drawing2.setOnClickListener(this);
        drawing3 = (ImageView) findViewById(R.id.drawing3);
        drawing3.setOnClickListener(this);
        drawing4 = (ImageView) findViewById(R.id.drawing4);
        drawing4.setOnClickListener(this);
        drawing5 = (ImageView) findViewById(R.id.drawing5);
        drawing5.setOnClickListener(this);
        drawing6 = (ImageView) findViewById(R.id.drawing6);
        drawing6.setOnClickListener(this);
        drawing7 = (ImageView) findViewById(R.id.drawing7);
        drawing7.setOnClickListener(this);
        drawing8 = (ImageView) findViewById(R.id.drawing8);
        drawing8.setOnClickListener(this);
        drawing9 = (ImageView) findViewById(R.id.drawing9);
        drawing9.setOnClickListener(this);


        addNewImage = (ImageView) findViewById(R.id.addNewImage);
        addNewImage.setOnClickListener(this);

        drawingView = (DrawingView) findViewById(R.id.drawingCanvas);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.drawing1:
                intent.putExtra("image", R.drawable.drawing1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.drawing2:
                intent.putExtra("image", R.drawable.drawing2);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.drawing3:
                intent.putExtra("image", R.drawable.drawing3);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.drawing4:
                intent.putExtra("image", R.drawable.drawing4);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.drawing5:
                intent.putExtra("image", R.drawable.drawing5);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.drawing6:
                intent.putExtra("image", R.drawable.drawing6);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.drawing7:
                intent.putExtra("image", R.drawable.drawing7);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.drawing8:
                intent.putExtra("image", R.drawable.drawing8);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.drawing9:
                intent.putExtra("image", R.drawable.drawing9);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.addNewImage:
                startChoosePhotoIntent();
                break;
        }
    }

    private void startChoosePhotoIntent() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(pickIntent, "Choose Your Photo");
        startActivityForResult(chooserIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (requestCode == RESULT_LOAD_IMAGE) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    Intent intent = new Intent();
                    intent.putExtra("imagePath", getRealPathFromURI(DrawingYourself.this, getImageUri(bitmap)));
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] realPath = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, realPath, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
