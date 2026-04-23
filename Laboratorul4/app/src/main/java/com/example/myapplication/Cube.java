package com.example.myapplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Cube {
    private FloatBuffer mFVertexBuffer;
    private FloatBuffer mNormalBuffer; // Buffer pentru normale [cite: 406]
    private ByteBuffer mTFan1;
    private ByteBuffer mTFan2;

    public Cube() {
        // ... (vertecsii raman aceiasi) ...
        float vertices[] = {
                -1.0f, 1.0f, 1.0f,  1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f
        };

        // Normalele pentru un cub sunt adesea identice cu valorile normalizate ale vertecsilor [cite: 412]
        float[] normals = {
                -0.57f,  0.57f,  0.57f,   0.57f,  0.57f,  0.57f,
                0.57f, -0.57f,  0.57f,  -0.57f, -0.57f,  0.57f,
                -0.57f,  0.57f, -0.57f,   0.57f,  0.57f, -0.57f,
                0.57f, -0.57f, -0.57f,  -0.57f, -0.57f, -0.57f
        };

        // Setup Vertex Buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mFVertexBuffer = vbb.asFloatBuffer();
        mFVertexBuffer.put(vertices);
        mFVertexBuffer.position(0);

        // Setup Normal Buffer [cite: 409-411]
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = nbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);

        // ... (mTFan1 si mTFan2 raman la fel ca in codul tau anterior) ...
        byte tFan1[] = { 1,0,3, 1,3,2, 1,2,6, 1,6,5, 1,5,4, 1,4,0 };
        byte tFan2[] = { 7,4,5, 7,5,6, 7,6,2, 7,2,3, 7,3,0, 7,0,4 };

        mTFan1 = ByteBuffer.allocateDirect(tFan1.length);
        mTFan1.put(tFan1);
        mTFan1.position(0);

        mTFan2 = ByteBuffer.allocateDirect(tFan2.length);
        mTFan2.put(tFan2);
        mTFan2.position(0);
    }

    void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);

        // Trimitem datele normalelor catre pipeline-ul OpenGL [cite: 417-418]
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glDrawElements(gl.GL_TRIANGLE_FAN, 18, gl.GL_UNSIGNED_BYTE, mTFan1);
        gl.glDrawElements(gl.GL_TRIANGLE_FAN, 18, gl.GL_UNSIGNED_BYTE, mTFan2);

        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}