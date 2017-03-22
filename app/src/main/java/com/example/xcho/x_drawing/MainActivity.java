package com.example.xcho.x_drawing;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton undoBtn, colorBtn, brushBtn, eraserBtn, moreBtn;
    private DrawingView drawingView;
    private Bitmap bitmap;
    private int lastBrushColor = Color.BLACK;
    private int lastBrushSize = 15;
    private int lastEraserSize = 20;

    private static final int IMAGE_KEY = 12345;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        verifyStoragePermissions(this);
    }


    private void initViews() {
        undoBtn = (ImageButton) findViewById(R.id.undo);
        undoBtn.setOnClickListener(this);

        eraserBtn = (ImageButton) findViewById(R.id.eraser);
        eraserBtn.setOnClickListener(this);

        brushBtn = (ImageButton) findViewById(R.id.brush);
        brushBtn.setOnClickListener(this);

        colorBtn = (ImageButton) findViewById(R.id.color);
        colorBtn.setOnClickListener(this);

        moreBtn = (ImageButton) findViewById(R.id.more);
        moreBtn.setOnClickListener(this);

        drawingView = (DrawingView) findViewById(R.id.drawingCanvas);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.undo:
                drawingView.undoExtion();
                break;
            case R.id.eraser:
                drawingView.setBrushSize(lastEraserSize);
                drawingView.setBrushColor(Color.WHITE);
                showEraserMenu();
                break;
            case R.id.brush:
                drawingView.setBrushSize(lastBrushSize);
                drawingView.setBrushColor(lastBrushColor);
                showBrushSizeMenu();
                break;
            case R.id.color:
                showColorMenu();
                break;
            case R.id.more:
                showMoreMenu();
                break;
        }
    }

    private void showMoreMenu() {
        PopupMenu popup = new PopupMenu(MainActivity.this, moreBtn);
        popup.getMenuInflater().inflate(R.menu.more_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newPage:
                        if (drawingView.isCanvasUse()) {
                            openSaveDialog();
                        } else {
                            Toast.makeText(MainActivity.this, "Canvas is Empty", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.open:
                        drawingView.setCanvasEmpty(false);
                        Intent intent = new Intent(MainActivity.this, DrawingYourself.class);
                        startActivityForResult(intent, IMAGE_KEY);
                        break;
                    case R.id.save:
                        saveImage(drawingView.asBitmap());
                        break;
                    case R.id.share:
                        shareBitmap(drawingView.asBitmap(), "image");
                        break;
                    case R.id.about:
                        aboutApp();
                        break;
                    case R.id.exit:
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    private void showBrushSizeMenu() {
        PopupMenu popup = new PopupMenu(MainActivity.this, brushBtn);
        popup.getMenuInflater().inflate(R.menu.brush_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.last_size:
                        drawingView.setBrushSize(lastBrushSize);
                        lastBrushSize = drawingView.getBrushSize();
                        break;
                    case R.id.size1:
                        drawingView.setBrushSize(10);
                        lastBrushSize = drawingView.getBrushSize();
                        break;
                    case R.id.size2:
                        drawingView.setBrushSize(15);
                        lastBrushSize = drawingView.getBrushSize();
                        break;
                    case R.id.size3:
                        drawingView.setBrushSize(20);
                        lastBrushSize = drawingView.getBrushSize();
                        break;
                    case R.id.size4:
                        drawingView.setBrushSize(25);
                        lastBrushSize = drawingView.getBrushSize();
                        break;
                    case R.id.size5:
                        drawingView.setBrushSize(30);
                        lastBrushSize = drawingView.getBrushSize();
                        break;
                    case R.id.size6:
                        drawingView.setBrushSize(35);
                        lastBrushSize = drawingView.getBrushSize();
                        break;
                    case R.id.size7:
                        drawingView.setBrushSize(40);
                        lastBrushSize = drawingView.getBrushSize();
                        break;
                }
                return false;
            }
        });
    }

    private void showEraserMenu() {
        PopupMenu popup = new PopupMenu(MainActivity.this, eraserBtn);
        popup.getMenuInflater().inflate(R.menu.eraser_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.last_size:
                        drawingView.setBrushSize(lastEraserSize);
                        lastEraserSize = drawingView.getBrushSize();
                        break;
                    case R.id.size1:
                        drawingView.setBrushSize(15);
                        lastEraserSize = drawingView.getBrushSize();
                        break;
                    case R.id.size2:
                        drawingView.setBrushSize(25);
                        lastEraserSize = drawingView.getBrushSize();
                        break;
                    case R.id.size3:
                        drawingView.setBrushSize(35);
                        lastEraserSize = drawingView.getBrushSize();
                        break;
                    case R.id.size4:
                        drawingView.setBrushSize(45);
                        lastEraserSize = drawingView.getBrushSize();
                        break;
                    case R.id.size5:
                        drawingView.setBrushSize(55);
                        lastEraserSize = drawingView.getBrushSize();
                        break;
                    case R.id.size6:
                        drawingView.setBrushSize(65);
                        lastEraserSize = drawingView.getBrushSize();
                        break;
                    case R.id.size7:
                        drawingView.setBrushSize(75);
                        lastEraserSize = drawingView.getBrushSize();
                        break;
                }
                return false;
            }
        });
    }

    private void showColorMenu() {
        PopupMenu popup = new PopupMenu(MainActivity.this, colorBtn);
        popup.getMenuInflater().inflate(R.menu.color_popup, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.red:
                        drawingView.setBrushColor(Color.RED);
                        lastBrushColor = drawingView.getBrushColor();
                        break;
                    case R.id.blue:
                        drawingView.setBrushColor(Color.BLUE);
                        lastBrushColor = drawingView.getBrushColor();
                        break;
                    case R.id.green:
                        drawingView.setBrushColor(Color.GREEN);
                        lastBrushColor = drawingView.getBrushColor();
                        break;
                    case R.id.black:
                        drawingView.setBrushColor(Color.BLACK);
                        lastBrushColor = drawingView.getBrushColor();
                        break;
                    case R.id.yellow:
                        drawingView.setBrushColor(Color.YELLOW);
                        lastBrushColor = drawingView.getBrushColor();
                        break;
                    case R.id.pink:
                        drawingView.setBrushColor(Color.MAGENTA);
                        lastBrushColor = drawingView.getBrushColor();
                        break;
                    case R.id.gray:
                        drawingView.setBrushColor(Color.GRAY);
                        lastBrushColor = drawingView.getBrushColor();
                        break;
                }
                return false;
            }
        });
    }

    private void aboutApp() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = dialog.create();
        dialog.setMessage("This app was built by  Khachatur Kakoyan");
        dialog.setTitle("ABOUT");

        Drawable icon = getResources().getDrawable(R.drawable.icon);
        dialog.setIcon(icon);

        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openSaveDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = dialog.create();
        dialog.setMessage("Do you want to save drawing image?");
        dialog.setTitle("Drawing");

        Drawable icon = getResources().getDrawable(R.drawable.save);
        dialog.setIcon(icon);

        dialog.setNegativeButton("Don't Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawingView.newPage();
            }
        });

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage(drawingView.asBitmap());
                drawingView.newPage();
            }
        });
        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_KEY && resultCode == RESULT_OK) {
            drawingView.newPage();
            if (data.getStringExtra("imagePath") != null) {
                String path = data.getStringExtra("imagePath");
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(path)));
                    drawingView.setBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                drawingView.setBackgroundResource(data.getIntExtra("image", R.color.backgroundColor));
            }
        }
    }

    private void shareBitmap(Bitmap bitmap, String fileName) {
        try {
            File file = new File(MainActivity.this.getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean saveImage(Bitmap bitmap) {
        //get path to external storage (SD card)
        String filename = "drawingImage" + (int) (Math.random() * 1000);
        String iconsStoragePath = Environment.getExternalStorageDirectory() + "/Drawing/";
        File sdIconStorageDir = new File(iconsStoragePath);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + "/" + filename + ".png";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            sdIconStorageDir.setReadable(true, false);
            Toast.makeText(MainActivity.this, "Image save in \"Drawing\" folder", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }
        return true;
    }
}
