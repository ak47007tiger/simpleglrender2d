package com.hpl.simpleglrender2d.util;


import com.hpl.simpleglrender2d.bean.Vector4;

/**
 * Created by Hpl on 2018/1/22 0022.
 */

public class MathEx {

  public static boolean reach(float src, float dst, float value) {
    if (src < dst) {
      return value >= dst;
    } else if (src > dst) {
      return value <= dst;
    }
    return value == dst;
  }

  public static float lerp(float a, float b, float t) {
    return a + (b - a) * t;
  }

  public static Vector4 lerp(Vector4 a, Vector4 b, float t) {
    return null;
  }

  public static void mulArray(float v, float[] array) {
    for (int i = 0; i < array.length; i++) {
      array[i] *= v;
    }
  }
}
