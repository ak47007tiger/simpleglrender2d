package com.hpl.simpleglrender2d.game;

import android.util.Log;

/**
 * Created by Hpl on 2018/1/22 0022.
 */

public class Component {

  public volatile boolean create;
  public volatile boolean start;
  public volatile boolean setup;

  public volatile boolean enable = true;

  public GameObject gameObject;

  public int getOrder() {
    return 0;
  }

  public void onCreate() {

  }

  public void resume() {
    enable = true;
    if (!setup) {
      setup = true;
      onResume();
    }
  }

  public void onResume() {
  }


  public void onStart() {

  }

  public void onRender() {

  }

  public void pause() {
    enable = false;
    if (setup) {
      setup = false;
      onPause();
    }
  }

  public void onPause() {

  }

  public void onDestroy() {

  }
}
