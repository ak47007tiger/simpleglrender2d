package com.hpl.simpleglrender2d.glmodule;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.hpl.simpleglrender2d.Engine;
import com.hpl.simpleglrender2d.R;
import com.hpl.simpleglrender2d.util.MathEx;
import com.hpl.simpleglrender2d.util.ShaderUtil;
import com.hpl.simpleglrender2d.util.TextureUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Hpl on 2018/1/25 0025.
 */

public class GLObject {

  int mProgram;//自定义渲染管线程序id
  int muMVPMatrixHandle;//总变换矩阵引用id
  int maPositionHandle; //顶点位置属性引用id
  int maTexCoorHandle; //顶点纹理坐标属性引用id
  String mVertexShader;//顶点着色器
  String mFragmentShader;//片元着色器

  FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
  FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲

  int textureId;

  //1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1
  float[] mMMatrix = new float[16];
  private float[] vertices;
  private float[] texCoor;

  public void setup() {

    Matrix.setRotateM(mMMatrix, 0, 0, 1, 0, 0);
    Matrix.rotateM(mMMatrix, 0, 0, -1, 0, 0);
    //Matrix.translateM(mMMatrix, 0, 0, 0, 0);

    //初始化顶点坐标与着色数据
    initVertexData();
    //初始化着色器
    initShader();

    Bitmap bitmap = BitmapFactory.decodeResource(
        Engine.getInstance().getContext().getResources(), R.drawable.wall);
    textureId = TextureUtil.createSpriteTexture(bitmap);
    bitmap.recycle();
  }

  //初始化顶点坐标与着色数据的方法
  public void initVertexData() {
    //顶点坐标数据的初始化================begin============================
    vertices = new float[]
        {
            -1, -1, 0,
            1, -1, 0,
            1, 1, 0,

            1, 1, 0,
            -1, 1, 0,
            -1, -1, 0,
        };
    MathEx.mulArray(0.1f, vertices);

    //创建顶点坐标数据缓冲
    //vertices.length*4是因为一个整数四个字节
    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
    vbb.order(ByteOrder.nativeOrder());//设置字节顺序
    mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
    mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
    mVertexBuffer.position(0);//设置缓冲区起始位置
    //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
    //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
    //顶点坐标数据的初始化================end============================

    //顶点纹理坐标数据的初始化================begin============================
    //顶点颜色值数组，每个顶点4个色彩值RGBA
    texCoor = new float[]//顶点颜色值数组，每个顶点4个色彩值RGBA
        {
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
    //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
    //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
    //顶点纹理坐标数据的初始化================end============================

  }

  //初始化着色器
  public void initShader() {
    Resources resources = Engine.getInstance().getContext().getResources();
    //加载顶点着色器的脚本内容
    mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.glsl", resources);
    //加载片元着色器的脚本内容
    mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.glsl", resources);
    //基于顶点着色器与片元着色器创建程序
    mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
    //获取程序中顶点位置属性引用id
    maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
    //获取程序中顶点纹理坐标属性引用id
    maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
    //获取程序中总变换矩阵引用id
    muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
  }

  public void destroy() {

  }

  public void draw() {
    draw(textureId);
  }

  public void draw(int texId) {
    //制定使用某套shader程序
    GLES20.glUseProgram(mProgram);

    //将最终变换矩阵传入shader程序
    GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
        RenderDataManager.instance.getFinalMatrix(mMMatrix), 0);
    //为画笔指定顶点位置数据
    GLES20.glVertexAttribPointer
        (
            maPositionHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            3 * 4,
            mVertexBuffer
        );
    //允许顶点位置数据数组
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
    GLES20.glEnableVertexAttribArray(maTexCoorHandle);

    //绑定纹理
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

    //绘制纹理矩形
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
    //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length);
  }
}
