package com.example.npurwosumarto.androidcontroller;

/**
 * Created by 301968 on 11/20/2017.
 */

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Config;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 301968 on 6/15/2017.
 */

public class GLRenderer implements GLSurfaceView.Renderer {

    private PrintWriter out = null;
    private BufferedReader in = null;

    private Triangle currentPos;
    private Triangle targetPos1;
    private Triangle targetPos2;

    private boolean targetSet = false;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    private float target_windowx = -1;
    private float target_windowy = -1;
    private float lastX = -1;
    private float lastZ = -1;
    private float currentX = -1;
    private float currentZ = -1;


    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        currentPos = new Triangle();
        currentPos.setVertices(-0.1f,0f,0f,0f,0.25f,0f,0.1f,0f,0f);
        targetPos1 = new Triangle();
        targetPos1.setVertices(0,0,0,0,0,0,0,0,0);
        targetPos2 = new Triangle();
        targetPos2.setVertices(0,0,0,0,0,0,0,0,0);
    }

    //private float[] mTranslationMatrix = new float[16];
    float x_offset = 0;
    float y_offset = 0;
    float angle = 0;
    int window_width = 0;
    int window_height = 0;
    float window_ratio = 0f;

    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.translateM(mMVPMatrix, 0, x_offset, y_offset, 0);

        recieveData();

        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0f, -1f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Draw triangle
        currentPos.draw(scratch);
        targetPos1.draw(mMVPMatrix);
        targetPos2.draw(mMVPMatrix);


    }

    public void updateTargetPos(float x_raw, float y_raw){
        targetSet = true;
        target_windowx = ((x_raw / (window_width / 2)) - 1) * window_ratio;
        target_windowy = ((y_raw / (window_height / 2)) - 1) * -1;
        float width = 0.05f;
        targetPos1.setVertices(target_windowx-width, target_windowy-width,0,target_windowx-width,target_windowy+width, 0, target_windowx+width, target_windowy+width,0);
        targetPos2.setVertices(target_windowx+width, target_windowy+width, 0, target_windowx+width, target_windowy-width, 0, target_windowx-width,target_windowy-width,0);

        float targetx_scaled = ((x_raw - (window_width/2)) / (window_width/2)) * 5;
        float targety_scaled = ((y_raw - (window_height/2)) / (window_width/2)) * 5;

        out.println(targetx_scaled);
        out.println(targety_scaled);
    }

    public void setAngle(float newangle){
        angle = newangle;
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        window_width = width;
        window_height = height;

        Log.d("mtag", "width: " + width + " height: " + height);

        window_ratio = (float) width / height;


        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -window_ratio, window_ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void setConnection(PrintWriter newout, BufferedReader newin){
        out = newout;
        in = newin;
    }

    public void recieveData(){
        String inputLine = null;
        try {
            if((inputLine = in.readLine()) != null) {
                angle = (float) Math.toDegrees(-Double.parseDouble(inputLine));
                lastX = currentX;
                lastZ = currentZ;
                currentX = Float.parseFloat(in.readLine());
                currentZ = Float.parseFloat(in.readLine());
                if(lastX != -1 && lastZ != -1 && targetSet){
                    float xStep = currentX - lastX;
                    float zStep = currentZ - lastZ;
                    target_windowx -= xStep/5;
                    target_windowy += zStep/5;
                    float width = 0.05f;
                    targetPos1.setVertices(target_windowx-width, target_windowy-width,0,target_windowx-width,target_windowy+width, 0, target_windowx+width, target_windowy+width,0);
                    targetPos2.setVertices(target_windowx+width, target_windowy+width, 0, target_windowx+width, target_windowy-width, 0, target_windowx-width,target_windowy-width,0);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
