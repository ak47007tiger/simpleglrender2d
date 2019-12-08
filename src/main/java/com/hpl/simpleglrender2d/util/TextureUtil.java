package com.hpl.simpleglrender2d.util;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Hpl on 2018/1/25 0025.
 */

public class TextureUtil {

  public static int createSpriteTexture(Bitmap bitmap) {
    int[] textures = new int[1];
    GLES20.glGenTextures(1, textures, 0);
    int texture = textures[0];
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

    GLUtils.texImage2D
        (
            GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
            0,            //纹理的层次，0表示基本图像层，可以理解为直接贴图
            bitmap,        //纹理图像
            0            //纹理边框尺寸
        );

    return texture;
  }
}
