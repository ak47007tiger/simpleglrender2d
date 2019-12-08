package com.hpl.simpleglrender2d.contract;

/**
 * Created by Hpl on 2018/5/7 0007.
 */

public interface EngienListener {

  void onCreate();

  void onResume();

  void onSizeChange();

  void onPause();

  void onDestroy();
}
