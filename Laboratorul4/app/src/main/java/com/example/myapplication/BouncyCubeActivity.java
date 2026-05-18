package com.example.myapplication;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BouncyCubeActivity extends AppCompatActivity {
    private GLSurfaceView gLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gLView = new GLSurfaceView(this);


        gLView.setRenderer(new CubeRenderer(this));

        setContentView(gLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gLView.onResume();
    }
}