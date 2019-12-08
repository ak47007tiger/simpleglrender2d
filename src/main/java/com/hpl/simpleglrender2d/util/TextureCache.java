package com.hpl.simpleglrender2d.util;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;
import com.hpl.simpleglrender2d.glmodule.RenderDataManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class TextureCache {

  ArrayList<CacheItem> list = new ArrayList<>(64);

  public void clear() {
    for (CacheItem item : list) {
      item.free();
    }
    list.clear();
  }

  public int addTexture(Bitmap bitmap) {
    if (list.size() == 32) {
      CacheItem item = list.remove(0);
      item.free();
    }

    CacheItem item = new CacheItem();
    item.malloc(bitmap);

    list.add(item);
    return item.textureId;
  }

  public int getTexture(Bitmap bitmap) {
    for (int i = 0; i < list.size(); i++) {
      CacheItem item = list.get(i);
      Bitmap cacheBitmap = item.reference.get();
      if (cacheBitmap != null && cacheBitmap == bitmap) {
        return item.textureId;
      }
    }
    return RenderDataManager.empty_texture;
  }

  public void report() {
    Log.d("Cache", "cache size : " + list.size());
  }

  private static class CacheItem {

    WeakReference<Bitmap> reference;
    int textureId;

    void malloc(Bitmap bitmap) {
      reference = new WeakReference<>(bitmap);
      if (bitmap != null && !bitmap.isRecycled()) {
        textureId = TextureUtil.createSpriteTexture(bitmap);
      } else {
        textureId = RenderDataManager.empty_texture;
      }
    }

    void free() {
      if (textureId != RenderDataManager.empty_texture) {
        GLES20.glDeleteTextures(1, new int[textureId], 0);
      }
    }
  }
}
