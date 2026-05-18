package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {
    private Cube mCube;
    private float mTransY = 0.0f;
    private float mAngle = 0.0f;
    private Context mContext;
    private int mTextureId;

    public final static int SS_SUNLIGHT = GL10.GL_LIGHT0;

    public CubeRenderer(Context context) {
        this.mContext = context;
        mCube = new Cube();
    }

    protected static FloatBuffer makeFloatBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(array);
        fb.position(0);
        return fb;
    }

    // Metoda din Lab 5 pentru incarcarea si generarea texturii din resurse
    private int createTexture(GL10 gl, Context context, int resourceId) {
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        int textureId = textures[0];

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        // Filtre de textura obligatorii (Lab 5)
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Incarcare imagine
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        return textureId;
    }

    private void initLighting(GL10 gl) {
        float[] white = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] position = {0.0f, 5.0f, 5.0f, 1.0f};

        gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, makeFloatBuffer(position));
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_DIFFUSE, makeFloatBuffer(white));

        // Seteaza culoarea materialului si componenta Alpha la 0.5f pentru Transparenta (Lab 6)
        float[] materialColor = {0.0f, 0.8f, 1.0f, 0.5f}; // 0.5f inseamna 50% opacitate
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(materialColor));
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 25);

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(SS_SUNLIGHT);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        // Pentru ca obiectele transparente sa se vada corect unele prin altele,
        // dezactivam structura stricta de taiere a fetelor spate (Cull Face) daca e cazul,
        // sau configuram adancimea.
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glShadeModel(GL10.GL_SMOOTH);


        gl.glEnable(GL10.GL_TEXTURE_2D);

        mTextureId = createTexture(gl, mContext, R.drawable.logo);


        gl.glEnable(GL10.GL_BLEND); // Activam blending-ul

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        initLighting(gl);
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

        gl.glTranslatef(0.0f, (float) Math.sin(mTransY) * 0.4f, -5.5f);
        gl.glRotatef(mAngle, 1.0f, 1.0f, 0.3f);

        // Mapam textura curenta inainte de desenare
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

        mCube.draw(gl);

        mTransY += 0.04f;
        mAngle += 1.0f;
    }
}