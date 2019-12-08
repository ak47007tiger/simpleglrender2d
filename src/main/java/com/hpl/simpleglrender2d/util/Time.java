package com.hpl.simpleglrender2d.util;

/**
 * Created by Hpl on 2018/1/22 0022.
 */

public class Time {

  public static Time instance = new Time();

  public float deltaTime;
  public float drawDeltaTime;
  public long previousFrameStart;
  public long currentFrameStart;

  public void start() {
    previousFrameStart = System.currentTimeMillis();
    drawDeltaTime = 0;
  }

  public void drawFrame() {
    currentFrameStart = System.currentTimeMillis();
    deltaTime = (currentFrameStart - previousFrameStart) / 1000f;
    previousFrameStart = currentFrameStart;
  }

  public void endDrawFrame() {
    drawDeltaTime = (System.currentTimeMillis() - currentFrameStart) / 1000f;
  }
}
