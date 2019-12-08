package com.hpl.simpleglrender2d.tween;

import android.util.Log;
import com.hpl.simpleglrender2d.Config;
import com.hpl.simpleglrender2d.animation.Ease;
import com.hpl.simpleglrender2d.animation.EaseProvider;
import com.hpl.simpleglrender2d.bean.Vector4;
import com.hpl.simpleglrender2d.game.Component;
import com.hpl.simpleglrender2d.game.Transform;
import com.hpl.simpleglrender2d.util.MathEx;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hpl on 2018/1/22 0022.
 */

public class TransformTween extends Component {

  private static final String TAG = "TransformTween";

  private final List<TweenParams> translateList = new ArrayList<>();

  public void translateBy(Transform transform, float x, float y, float duration,
      TweenListener callback) {
    translateBy(transform, x, y, duration, Config.default_ease, 1, callback);
  }

  public void translateBy(Transform transform, float x, float y, float duration, Ease ease,
      int loop, TweenListener callback) {
    translate(transform, transform.translate.x + x, transform.translate.y + y, duration, ease, loop,
        callback);
  }

  public void translate(Transform transform, float x, float y, float duration,
      TweenListener callback) {
    translate(transform, x, y, duration, Config.default_ease, 1, callback);
  }

  public void translate(Transform transform, float x, float y, float duration, Ease ease, int loop,
      TweenListener callback) {
    Log.i(TAG, "translate");

    TweenParams tweenParams = new TweenParams();
    tweenParams.loop = loop;
    tweenParams.ease = ease;
    tweenParams.callback = callback;
    tweenParams.transform = transform;
    tweenParams.translate = new Vector4(transform.translate.x, transform.translate.y, x, y);
    tweenParams.duration = duration;
    tweenParams.totalCount = Config.targetFps * duration;

    synchronized (this) {
      translateList.add(tweenParams);
    }
  }

  @Override
  public synchronized void onRender() {
    List<TweenParams> removeParamList = new ArrayList<>();
    for (TweenParams tweenParams : translateList) {
      TweenParams params = updateTranslateFixedFrame(tweenParams);
      if (params != null) {
        removeParamList.add(params);
      }
    }

    translateList.removeAll(removeParamList);
  }

  private TweenParams updateTranslateFixedFrame(TweenParams params) {
    Log.i(TAG, "updateTranslateFixedFrame");

    Transform transform = params.transform;
    Vector4 translate = params.translate;

    if (params.reset) {
      params.reset = false;
      params.frameIndex = 0;
      transform.translate(translate.x, translate.y);
    }

    if (params.execStart) {
      params.execStart = false;

      if (params.callback != null) {
        params.callback.onStart();
      }
    }

    params.frameIndex++;

    float p = Math.min(params.frameIndex / params.totalCount, 1);
    float easeP = EaseProvider.get(params.ease, p);

    float newCx = MathEx.lerp(translate.x, translate.z, easeP);
    float newCy = MathEx.lerp(translate.y, translate.w, easeP);
    transform.translate(newCx, newCy);

    if (p == 1) {
      if (params.loop == -1) {
        params.reset = true;
        return null;
      }

      if (params.loop > 0) {
        params.loop--;
      }

      if (params.loop > 0) {
        params.reset = true;
      } else {
        if (params.callback != null) {
          params.callback.onEnd();
        }

        return params;
      }
    }

    return null;
  }

  class TweenParams {

    public int loop = 1;
    public boolean reset = false;
    public Ease ease;
    public float useTime = 0;
    public float duration = 0;
    public int frameIndex = -1;
    public float totalCount;
    public TweenListener callback;
    public Transform transform;
    public Vector4 translate;
    boolean execStart = true;

  }
}
