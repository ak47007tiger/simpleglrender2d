package com.hpl.simpleglrender2d.util;

import com.hpl.simpleglrender2d.bean.Vector3;

/**
 * Created by Hpl on 2018/1/26 0026.
 */

public class CoordinateUtil {

  public static float[] convertToVertices(Vector3[] array) {
    float[] vertices = new float[array.length * 3];
    for (int i = 0; i < array.length; i++) {
      vertices[i * 3] = array[i].x;
      vertices[i * 3 + 1] = array[i].y;
      vertices[i * 3 + 2] = array[i].z;
    }
    return vertices;
  }
}
