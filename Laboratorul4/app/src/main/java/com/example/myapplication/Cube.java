package com.example.myapplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Cube {
    private FloatBuffer mFVertexBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mTextureBuffer; // Buffer-ul pentru maparea texturii
    private ByteBuffer mTFan1;
    private ByteBuffer mTFan2;

    public Cube() {
        float vertices[] = {
                -1.0f, 1.0f, 1.0f,  1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f
        };

        float[] normals = {
                -0.57f,  0.57f,  0.57f,   0.57f,  0.57f,  0.57f,
                0.57f, -0.57f,  0.57f,  -0.57f, -0.57f,  0.57f,
                -0.57f,  0.57f, -0.57f,   0.57f,  0.57f, -0.57f,
                0.57f, -0.57f, -0.57f,  -0.57f, -0.57f, -0.57f
        };

        // Coordonate de textura (s, t) pentru fiecare dintre cei 8 vertecsi
        float textureCoords[] = {
                0.0f, 0.0f,   1.0f, 0.0f,
                1.0f, 1.0f,   0.0f, 1.0f,
                0.0f, 1.0f,   1.0f, 1.0f,
                1.0f, 0.0f,   0.0f, 0.0f
        };

        // Setup Vertex Buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mFVertexBuffer = vbb.asFloatBuffer();
        mFVertexBuffer.put(vertices);
        mFVertexBuffer.position(0);

        // Setup Normal Buffer
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = nbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);

        // Setup Texture Buffer
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asFloatBuffer();
        mTextureBuffer.put(textureCoords);
        mTextureBuffer.position(0);

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

        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        // Activam si trimitem coordonatele de textura
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 18, GL10.GL_UNSIGNED_BYTE, mTFan1);
        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 18, GL10.GL_UNSIGNED_BYTE, mTFan2);

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}