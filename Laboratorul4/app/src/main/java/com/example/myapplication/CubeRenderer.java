package com.example.myapplication;

import android.opengl.GLSurfaceView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {
    private Cube mCube;
    private float mTransY = 0.0f;
    private float mAngle = 0.0f;

    // Definim lumina [cite: 423]
    public final static int SS_SUNLIGHT = GL10.GL_LIGHT0;

    public CubeRenderer() {
        mCube = new Cube();
    }

    // Metoda helper pentru a crea FloatBuffers [cite: 435]
    protected static FloatBuffer makeFloatBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(array);
        fb.position(0);
        return fb;
    }

    private void initLighting(GL10 gl) {
        // Culori [cite: 427, 456]
        float[] white = {1.0f, 1.0f, 1.0f, 1.0f}; // Folosim alb pentru a vedea materialele [cite: 463]
        float[] green = {0.0f, 1.0f, 0.0f, 1.0f};
        float[] position = {0.0f, 5.0f, 0.0f, 1.0f}; // Pozitia luminii [cite: 429]

        // Configurare Lumina [cite: 433]
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, makeFloatBuffer(position));
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_DIFFUSE, makeFloatBuffer(white));

        // Configurare Material [cite: 455, 483]
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(green));
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 25); // Material tip plastic/metal [cite: 484-485]

        gl.glShadeModel(GL10.GL_SMOOTH); // Blending fin al culorilor [cite: 447-448]
        gl.glEnable(GL10.GL_LIGHTING); // Activare iluminare [cite: 450]
        gl.glEnable(SS_SUNLIGHT); // Activare sursa de lumina specifica [cite: 451]
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        initLighting(gl); // Apelam initializarea luminii [cite: 421]
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        float aspectRatio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-aspectRatio, aspectRatio, -1, 1, 1, 10);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, (float) Math.sin(mTransY) * 0.5f, -6.0f);
        gl.glRotatef(mAngle, 1.0f, 1.0f, 0.0f);

        mCube.draw(gl);

        mTransY += 0.05f;
        mAngle += 1.2f;
    }
}