package com.hpl.simpleglrender2d.glmodule;

import android.opengl.Matrix;

/**
 * Created by Hpl on 2018/1/25 0025.
 */

public class RenderDataManager {

  public static final int empty_texture = -1;

  public static RenderDataManager instance = new RenderDataManager();

  public OrthoCamera orthoCamera = new OrthoCamera();

  public GLSprite glSprite = new GLSprite();

  public float[] mProjMatrix = new float[16];//4x4矩阵 投影用

  public void setProject
      (
          float left,    //near面的left
          float right,    //near面的right
          float bottom,   //near面的bottom
          float top,      //near面的top
          float near,    //near面距离
          float far       //far面距离
      ) {
    Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
  }

  public float[] getFinalMatrix(float[] mMMatrix) {
    float[] mMVPMatrix = new float[16];
    Matrix.multiplyMM(mMVPMatrix, 0, orthoCamera.mVMatrix, 0, mMMatrix, 0);
    Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
    return mMVPMatrix;
  }

}
