package com.hpl.simpleglrender2d.bean;

/**
 * Created by Hpl on 2018/1/26 0026.
 */

public class Vector3 {

  public float x, y, z = 0;

  public Vector3() {
  }

  public Vector3(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Vector3(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
