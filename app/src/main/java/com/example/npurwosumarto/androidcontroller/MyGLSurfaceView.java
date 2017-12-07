package com.example.npurwosumarto.androidcontroller;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;


/**
 * Created by 301968 on 11/27/2017.
 */

class MyGLSurfaceView extends GLSurfaceView {

    GLRenderer mRenderer;

    public MyGLSurfaceView(Context context, GLRenderer renderer) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = renderer;

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        final int pointerCount = e.getPointerCount();

        // get pointer index from the event object
        int pointerIndex = e.getActionIndex();

        float x = e.getX(pointerIndex);
        float y = e.getY(pointerIndex);

        switch (e.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                mRenderer.updateTargetPos(x, y);

        }

        return true;
    }

    public void sendAngle(double angle){
        mRenderer.setAngle((float) angle);
    }

}
