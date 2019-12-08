package com.hpl.simpleglrender2d.game;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import com.hpl.simpleglrender2d.glmodule.RenderDataManager;
import com.hpl.simpleglrender2d.util.TextureUtil;

/**
 * Created by Hpl on 2018/1/25 0025.
 */

public class Sprite extends Component {

  Bitmap bitmap;

  int textureId = RenderDataManager.empty_texture;

  public void setBitmap(Bitmap bitmap) {
    release();
    this.bitmap = bitmap;
  }

  private void updateTextureId() {
    if (bitmap != null && !bitmap.isRecycled() && textureId == RenderDataManager.empty_texture) {
      textureId = TextureUtil.createSpriteTexture(bitmap);
    }
  }

  @Override
  public void onResume() {
    updateTextureId();
  }

  @Override
  public void onRender() {
    Transform transform = gameObject.transform;
    float[] vertices = transform.vertices;
    if (vertices != null) {
      RenderDataManager.instance.glSprite.draw(
          textureId, transform.mMMatrix, transform.mVertexBuffer, vertices.length / 3);
    }
  }

  @Override
  public void onPause() {
    release();
  }

  public void release() {
    if (textureId != RenderDataManager.empty_texture) {
      GLES20.glDeleteTextures(1, new int[textureId], 0);
    }
    textureId = RenderDataManager.empty_texture;
  }

}
