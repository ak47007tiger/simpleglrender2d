package com.hpl.simpleglrender2d.glmodule;

import android.graphics.RectF;
import com.hpl.simpleglrender2d.bean.Vector2;
import com.hpl.simpleglrender2d.game.Transform;

/**
 * Created by Hpl on 2018/1/26 0026.
 */

public class TransformHelper {

  private static final RectF tempRectF = new RectF();

  public static void setupTransform(float vl, float vt, float vr, float vb, Transform transform) {
    Vector2 lt = new Vector2(vl, vt);
    toW1(lt);
    //toW(lt);

    Vector2 rt = new Vector2(vr, vb);
    toW1(rt);
    //toW(rt);

    synchronized (tempRectF) {
      tempRectF.set(lt.x, lt.y, rt.x, rt.y);
      transform.verticesTranslate.x = tempRectF.centerX();
      transform.verticesTranslate.y = tempRectF.centerY();
      tempRectF.offset(-tempRectF.centerX(), -tempRectF.centerY());
      transform.setBounds(tempRectF.left, tempRectF.top, tempRectF.right, tempRectF.bottom);
    }
  }

  static void toW1(Vector2 point) {
    point.x = point.x * 2 - 1;
    point.y = 1 - point.y * 2;
  }

  public static void setupTransformWithSize(float vl, float vt, float vw, float vh,
      Transform transform) {
    setupTransform(vl, vt, vl + vw, vt + vh, transform);
  }

  public static Vector2 computeTranslateDxDy(RectF src, RectF dst) {
    Vector2 srcLt = new Vector2(src.left, src.top);
    toW1(srcLt);
    Vector2 srcRb = new Vector2(src.right, src.bottom);
    toW1(srcRb);

    Vector2 dstLt = new Vector2(dst.left, dst.top);
    toW1(dstLt);

    Vector2 dstRb = new Vector2(dst.right, dst.bottom);
    toW1(dstRb);
    src.set(srcLt.x, srcLt.y, srcRb.x, srcRb.y);
    dst.set(dstLt.x, dstLt.y, dstRb.x, dstRb.y);
    return new Vector2(dst.centerX() - src.centerX(), dst.centerY() - src.centerY());
  }
}
