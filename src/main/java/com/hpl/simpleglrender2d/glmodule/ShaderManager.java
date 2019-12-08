package com.hpl.simpleglrender2d.glmodule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hpl on 2018/1/26 0026.
 */

public class ShaderManager {

  public static ShaderManager instance = new ShaderManager();
  Map<ShaderType, Integer> type2programe = new HashMap<>();

  public void setup() {

  }

  public enum ShaderType {
    Sprite
  }
}
