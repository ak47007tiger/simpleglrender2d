package com.hpl.simpleglrender2d;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import com.hpl.simpleglrender2d.game.Component;
import com.hpl.simpleglrender2d.game.GameObject;
import com.hpl.simpleglrender2d.tween.TransformTween;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Hpl on 2018/1/25 0025.
 */

public class Engine {

  private static String TAG = Engine.class.getSimpleName();

  private static Engine instance;

  public final GameObject tweenGameObject;
  public final TransformTween transformTween;
  public final ArrayDeque<Runnable> beforeRender = new ArrayDeque<>();
  public final List<GameObject> gameObjectList = new ArrayList<>();
  public final ArrayDeque<Runnable> afterRender = new ArrayDeque<>();
  public final List<Component> components = new ArrayList<>();

  final Context context;
  private Comparator<Component> comparator = new Comparator<Component>() {
    @Override
    public int compare(Component o1, Component o2) {
      int order1 = o1.getOrder();
      int order2 = o2.getOrder();
      if (order1 > order2) {
        return 1;
      }
      if (order1 == order2) {
        return 0;
      }
      return -1;
    }
  };

  private Engine(Context context) {
    this.context = context.getApplicationContext();
    tweenGameObject = new GameObject();
    transformTween = tweenGameObject.addComponent(new TransformTween());
    addGameObject(tweenGameObject);
  }

  public synchronized static void init(Context context) {
    if (instance == null) {
      instance = new Engine(context);
    }
  }

  public synchronized static Engine getInstance() {
    return instance;
  }

  public Context getContext() {
    return context;
  }

  public synchronized void runBeforeRender(Runnable task) {
    beforeRender.add(task);
  }

  public synchronized void runAfterRender(Runnable task) {
    afterRender.add(task);
  }

  public synchronized void addGameObject(GameObject gameObject) {
    gameObjectList.add(gameObject);
  }

  public synchronized void destroy(GameObject gameObject) {
    gameObject.onPause();
    gameObject.onDestroy();
    gameObjectList.remove(gameObject);
  }

  public synchronized void resume() {
    for (GameObject gameObject : gameObjectList) {
      gameObject.resume();
    }
  }

  public synchronized void render() {
    int i = 0;

    while (i < beforeRender.size()) {
      beforeRender.poll().run();
      i++;
    }

    components.clear();
    i = 0;
    while (i < gameObjectList.size()) {
      GameObject gameObject = gameObjectList.get(i);
      components.add(gameObject);
      gameObject.onRender(components);
      i++;
    }

    Collections.sort(components, comparator);

    GLES20.glClearColor(0, 0, 0, 0);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

    i = 0;
    while (i < components.size()) {
      Component component = components.get(i);
      if (!component.create) {
        component.onCreate();
        component.create = true;
      }

      component.resume();

      if (!component.start) {
        component.onStart();
        component.start = true;
      }
      if (component.enable) {
        component.onRender();
      }
      i++;
    }

    i = 0;
    while (i < afterRender.size()) {
      afterRender.poll().run();
      i++;
    }
  }

  public synchronized void pause() {
    for (GameObject gameObject : gameObjectList) {
      gameObject.pause();
    }
  }

  public synchronized void clear() {
    while (gameObjectList.size() > 0) {
      GameObject gameObject = gameObjectList.get(0);
      if (gameObject != tweenGameObject) {
        gameObject.pause();
        gameObject.onDestroy();
        gameObjectList.remove(gameObject);
      }
    }
  }

  public synchronized void report() {
    Log.d(TAG, String.format("game object:%d", gameObjectList.size()));
  }
}
