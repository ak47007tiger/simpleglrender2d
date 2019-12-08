package com.hpl.simpleglrender2d.glmodule;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Created by Hpl on 2018/1/25 0025.
 */

public class OrthoCamera {

  public float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵

  public float ratio;

  public void updateSize(int width, int height) {
    //设置视窗大小及位置
    GLES20.glViewport(0, 0, width, height);
    //计算GLSurfaceView的宽高比
    ratio = (float) width / height;
    //调用此方法计算产生透视投影矩阵
    //RenderDataManager.instance.setProject(-ratio, ratio, -1, 1, 1, 50);
    RenderDataManager.instance.setProject(-1, 1, -1, 1, 1, 20);
    //调用此方法产生摄像机9参数位置矩阵
    Matrix.setLookAtM
        (
            mVMatrix,
            0,
            0,
            0,
            10,
            0,
            0,
            0,
            0,
            1,
            0
        );
  }

}
