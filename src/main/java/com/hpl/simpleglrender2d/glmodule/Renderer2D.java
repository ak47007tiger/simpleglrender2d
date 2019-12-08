package com.hpl.simpleglrender2d.glmodule;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.hpl.simpleglrender2d.Engine;
import com.hpl.simpleglrender2d.util.Time;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Hpl on 2018/1/25 0025.
 */

public class Renderer2D implements GLSurfaceView.Renderer {

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    Time.instance.start();
    //打开深度检测
    //GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    GLES20.glEnable(GLES20.GL_CULL_FACE);
    //关闭背面剪裁
    //GLES20.glDisable(GLES20.GL_CULL_FACE);

    RenderDataManager.instance.glSprite.setup();
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    RenderDataManager.instance.orthoCamera.updateSize(width, height);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    Time.instance.drawFrame();
    Engine.getInstance().render();
  }
}
