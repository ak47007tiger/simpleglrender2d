package com.hpl.simpleglrender2d.glmodule;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import com.hpl.simpleglrender2d.Config;
import com.hpl.simpleglrender2d.Engine;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hpl on 2018/1/22 0022.
 */

public class GLSurfaceView2D extends GLSurfaceView {

  private static final String TAG = "GLSurfaceView2D";

  private static final ScheduledExecutorService RENDER_EXECUTOR =
      Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
          return new Thread(r, "GLSurfaceView2D-Render-Thread");
        }
      });

  public Renderer2D renderer;
  private volatile ScheduledFuture timer;
  private Engine engine;

  public GLSurfaceView2D(Context context) {
    super(context);
    init();
  }

  public GLSurfaceView2D(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    Engine.init(getContext());
    engine = Engine.getInstance();

    setEGLContextClientVersion(2);
    setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    renderer = new Renderer2D();
    setRenderer(renderer);
    getHolder().setFormat(PixelFormat.TRANSLUCENT);
    setZOrderOnTop(true);
    //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
  }

  @Override
  public void onResume() {
    super.onResume();
    start();
    engine.resume();
  }

  @Override
  public void onPause() {
    engine.pause();
    stop();
    super.onPause();
  }

  private void start() {
    if (isStart()) {
      return;
    }
    timer = RENDER_EXECUTOR.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        requestRender();
      }
    }, 0, 1000 / Config.targetFps, TimeUnit.MILLISECONDS);
  }

  private void stop() {
    if (!isStart()) {
      return;
    }
    timer.cancel(true);
    timer = null;
  }

  private boolean isStart() {
    return timer != null;
  }
}
