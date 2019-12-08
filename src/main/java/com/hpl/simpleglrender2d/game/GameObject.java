package com.hpl.simpleglrender2d.game;

import com.hpl.simpleglrender2d.Engine;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hpl on 2018/1/22 0022.
 */

public class GameObject extends Component {

  public final Transform transform;
  public ArrayList<Component> components = new ArrayList<>();

  public GameObject() {
    transform = new Transform();
    addComponent(transform);
  }

  public void add() {
    Engine.getInstance().addGameObject(this);
  }

  public <T extends Component> T addComponent(T component) {
    component.gameObject = this;
    components.add(component);
    return component;
  }

  public <T extends Component> T getComponent(Class<T> type) {
    for (int i = 0; i < components.size(); i++) {
      Component component = components.get(i);
      if (component.getClass() == type) {
        return (T) component;
      }
    }
    return null;
  }

  public void destroy() {
    Engine.getInstance().destroy(this);
  }

  @Override
  public void onResume() {
    for (Component component : components) {
      component.resume();
    }
  }

  public void onRender(List<Component> list) {
    if (!create) {
      onCreate();
    }
    if (!start) {
      onStart();
    }

    if (enable) {
      list.addAll(components);
    }
  }

  @Override
  public void onPause() {
    for (Component component : components) {
      component.pause();
    }
  }

  @Override
  public void onDestroy() {
    for (Component component : components) {
      component.onDestroy();
    }
  }

}
