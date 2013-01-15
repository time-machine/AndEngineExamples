package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;

import android.graphics.Color;
import android.graphics.Typeface;

public class ChangeableTextExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mFontTexture;
  private Font mFont;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
        Typeface.BOLD), 48, true, Color.BLACK);
    getEngine().getTextureManager().loadTexture(mFontTexture);
    getEngine().getFontManager().loadFont(mFont);
  }

  @Override
  public Scene onLoadScene() {
    final FPSCounter fpsCounter = new FPSCounter();
    getEngine().registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final ChangeableText elapsedText = new ChangeableText(100, 160, mFont,
        "Seconds elapsed:", "Seconds elapsed: XXXXX".length());
    final ChangeableText fpsText = new ChangeableText(250, 240, mFont,
        "FPS:", "FPS: XXXXX".length());

    scene.getLastChild().attachChild(elapsedText);
    scene.getLastChild().attachChild(fpsText);

    scene.registerUpdateHandler(new TimerHandler(1 / 20, true,
        new ITimerCallback() {
      @Override
      public void onTimePassed(final TimerHandler pTimerHandler) {
        elapsedText.setText("Seconds elapsed:" +
            getEngine().getSecondsElapsedTotal());
        fpsText.setText("FPS:" + fpsCounter.getFPS());
      }
    }));

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
