package com.hpl.simpleglrender2d.glmodule;

import android.content.res.Resources;
import android.opengl.GLES20;
import com.hpl.simpleglrender2d.BuildConfig;
import com.hpl.simpleglrender2d.Engine;
import com.hpl.simpleglrender2d.util.ShaderUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Hpl on 2018/1/26 0026.
 */

public class GLSprite {

  int mProgram;//自定义渲染管线程序id
  int muMVPMatrixHandle;//总变换矩阵引用id
  int maPositionHandle; //顶点位置属性引用id
  int maTexCoorHandle; //顶点纹理坐标属性引用id
  String mVertexShader;//顶点着色器
  String mFragmentShader;//片元着色器

  FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲

  private float[] texCoor;

  public void setup() {
    //初始化顶点坐标与着色数据
    initVertexData();
    //初始化着色器
    initShader();
  }

  //初始化顶点坐标与着色数据的方法
  public void initVertexData() {
/*        texCoor = new float[]{
                0, 1,
                1, 1,
                1, 0,

                0, 1,
                1, 0,
                0, 0
        };*/
    texCoor = new float[]{
        0, 1,
        1, 1,
        1, 0,

        1, 0,
        0, 0,
        0, 1,
    };
    //创建顶点纹理坐标数据缓冲
    ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
    cbb.order(ByteOrder.nativeOrder());//设置字节顺序
    mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
    mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
    mTexCoorBuffer.position(0);//设置缓冲区起始位置
  }

  //初始化着色器
  public void initShader() {
    Resources resources = Engine.getInstance().getContext().getResources();
    if (BuildConfig.DEBUG) {
      mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.glsl", resources);
      mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.glsl", resources);
    } else {
      mVertexShader = ShaderUtil.loadFromAssetsFile("shaders/GLObjectVertex.glsl", resources);
      mFragmentShader = ShaderUtil.loadFromAssetsFile("shaders/GLObjectFrag.glsl", resources);
    }
    //基于顶点着色器与片元着色器创建程序
    mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
    //获取程序中顶点位置属性引用id
    maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
    //获取程序中顶点纹理坐标属性引用id
    maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
    //获取程序中总变换矩阵引用id
    muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
  }

  public void draw(int texId, float[] mMMatrix, FloatBuffer mVertexBuffer, int vCount) {
    if (texId == RenderDataManager.empty_texture || mMMatrix == null || mVertexBuffer == null
        || vCount < 3) {
      return;
    }

    //制定使用某套shader程序
    GLES20.glUseProgram(mProgram);

    //将最终变换矩阵传入shader程序
    GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
        RenderDataManager.instance.getFinalMatrix(mMMatrix), 0);
    //为画笔指定顶点位置数据
    GLES20.glVertexAttribPointer(
        maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer
        //maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer
        //maPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer
    );
    GLES20.glEnableVertexAttribArray(maPositionHandle);

    //为画笔指定顶点纹理坐标数据
    GLES20.glVertexAttribPointer
        (
            maTexCoorHandle,
            2,
            GLES20.GL_FLOAT,
            false,
            2 * 4,
            mTexCoorBuffer
        );

    //允许顶点位置数据数组
    GLES20.glEnableVertexAttribArray(maTexCoorHandle);

    //绑定纹理
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

    //绘制纹理矩形
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
  }
}
