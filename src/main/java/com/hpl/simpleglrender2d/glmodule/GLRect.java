package com.hpl.simpleglrender2d.glmodule;

import android.graphics.RectF;
import android.view.View;
import com.hpl.simpleglrender2d.bean.Vector2;
import com.hpl.simpleglrender2d.bean.Vector3;
import com.hpl.simpleglrender2d.util.CoordinateUtil;

/**
 * Created by Hpl on 2018/1/26 0026.
 */

public class GLRect {

  public static float[] getVertices(View view, View parent) {
    int viewPortW = parent.getWidth();
    int viewPortH = parent.getHeight();

    float x = view.getX();
    float y = view.getY();
    float right = x + view.getWidth();
    float bottom = y + view.getHeight();

    x = x / viewPortW;
    y = y / viewPortH;
    right = right / viewPortW;
    bottom = bottom / viewPortH;

    return getVerticesWithViewBounds(x, y, right, bottom);
  }

  public static RectF getViewBounds(View view) {
    return getViewBounds(view, (View) view.getParent());
  }

  public static RectF getViewBounds(View view, View parent) {
    int viewPortW = parent.getWidth();
    int viewPortH = parent.getHeight();

    float x = view.getX();
    float y = view.getY();
    float right = x + view.getWidth();
    float bottom = y + view.getHeight();

    x = x / viewPortW;
    y = y / viewPortH;
    right = right / viewPortW;
    bottom = bottom / viewPortH;

    return new RectF(x, y, right, bottom);
  }

  public static float[] getVerticesWithViewBounds(float left, float top, float right,
      float bottom) {
    float gl_left = left * 2 - 1;
    float gl_top = 1 - top * 2;
    float gl_right = right * 2 - 1;
    float gl_bottom = 1 - bottom * 2;

    return getVertices(gl_left, gl_top, gl_right, gl_bottom);
  }

  public static float[] getVerticesWithViewSize(float x, float y, float w, float h) {
    return getVerticesWithViewBounds(x, y, x + w, y + h);
  }

  public static float[] getVertices(float left, float top, float right, float bottom) {
    Vector3 lb = new Vector3(left, bottom);
    Vector3 lt = new Vector3(left, top);
    Vector3 rt = new Vector3(right, top);
    Vector3 rb = new Vector3(right, bottom);
    return CoordinateUtil.convertToVertices(new Vector3[]{
        lb, rb, rt,
        rt, lt, lb,
    });
        /*
        return CoordinateUtil.convertToVertices(new Vector3[]{
            lb,rb,rt,
            lb,rt,lt,
        });
        * */
  }

  public static float[] getVerticesWithSize(float x, float y, float w, float h) {
    return getVertices(x, y + h, x + w, y);
  }

  public static Vector2 convertToGlSize(Vector2 viewSize, Vector2 viewPortSize) {
    return new Vector2(viewSize.x / viewPortSize.x, viewSize.y / viewPortSize.y);
  }
}
