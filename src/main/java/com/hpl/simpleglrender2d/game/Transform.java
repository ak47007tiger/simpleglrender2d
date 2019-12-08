package com.hpl.simpleglrender2d.game;

import android.opengl.Matrix;
import com.hpl.simpleglrender2d.bean.Vector2;
import com.hpl.simpleglrender2d.glmodule.GLRect;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Hpl on 2018/1/22 0022.
 */

public class Transform extends Component {

  public FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
  //1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1
  public float[] mMMatrix = new float[16];
  public float[] vertices;

  public Vector2 verticesTranslate = new Vector2();
  public Vector2 translate = new Vector2();
  public Vector2 scale = new Vector2(1, 1);

  public Transform() {
  }

  public void setBounds(float l, float t, float r, float b) {
    vertices = GLRect.getVertices(l, t, r, b);
    setupVerticesBuf();
    updateMMatrix();
  }

/*    public void setBoundsWithViewSize(float x,float y,float w, float h){
        RectF rectF = new RectF(x,y,x+w,y+h);
        float cx = rectF.centerX();
        float cy = rectF.centerY();
        rectF.offsetTo(0.5f- w/2f, 0.5f-h/2f);
        verticesTranslate.x = (x - rectF.left) * 2;
        verticesTranslate.y = -(y - rectF.top) * 2;
        vertices = GLRect.getVerticesWithViewSize(rectF.left,rectF.top,rectF.width(),rectF.height());
        setupVerticesBuf();
        updateMMatrix();
    }*/

  private void setupVerticesBuf() {
    //创建顶点坐标数据缓冲
    //vertices.length*4是因为一个整数四个字节
    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
    vbb.order(ByteOrder.nativeOrder());//设置字节顺序
    mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
    mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
    mVertexBuffer.position(0);//设置缓冲区起始位置
  }

  private void updateMMatrix() {
    Matrix.setRotateM(mMMatrix, 0, 0, 1, 0, 0);
    Matrix.rotateM(mMMatrix, 0, 0, -1, 0, 0);
    //Matrix.translateM(mMMatrix, 0, verticesTranslate.x, verticesTranslate.y,0);
    Matrix.translateM(mMMatrix, 0,
        verticesTranslate.x + translate.x,
        verticesTranslate.y + translate.y,
        0);
    //Matrix.translateM(mMMatrix, 0, translate.x, translate.y,0);
    Matrix.scaleM(mMMatrix, 0, scale.x, scale.y, 0);
  }

  public void translate(float x, float y) {
    translate.x = x;
    translate.y = y;
    updateMMatrix();
  }

  public void translateBy(float x, float y) {
    translate.x += x;
    translate.y += y;
    updateMMatrix();
  }

  public void scale(float x, float y) {
    scale.x = x;
    scale.y = y;
    updateMMatrix();
  }
}
