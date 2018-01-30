package com.spencerstudios.plainwallpaper;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {

    private WallpaperManager wallpaperManager;
    private ColorPicker colorPicker;

    private ImageView imageView;
    private LinearLayout parentLayout;
    private Canvas canvas;
    private Bitmap bitmap;

    private final int RED = 51, GREEN = 100, BLUE = 137;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
        colorPicker = new ColorPicker(this, RED, GREEN, BLUE);
        imageView = (ImageView) findViewById(R.id.image_view);
        parentLayout = (LinearLayout) findViewById(R.id.root_layout);

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                createBitmapAndCanvas();
            }
        });

        colorPicker.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(@ColorInt int color) {
                applyColors(color);
                colorPicker.dismiss();
            }
        });
    }

    private void createBitmapAndCanvas() {
        bitmap = Bitmap.createBitmap(parentLayout.getWidth(), parentLayout.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        applyColors(Color.rgb(RED, GREEN, BLUE));
    }

    private void setWallpaper() {
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(getApplicationContext(), "Wallpaper set", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Oops, cannot set wallpaper", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyColors(int color) {
        canvas.drawColor(color);
        imageView.setImageBitmap(bitmap);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(color);
        window.setStatusBarColor(color);
    }

    public void clickEvent(View view) {
        if (view.getId() == R.id.fab) setWallpaper(); else colorPicker.show();
    }
}
