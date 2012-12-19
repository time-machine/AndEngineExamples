package org.anddev.andengine.examples.benchmark;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Layer;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.text.TickerText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;

public class TickerTextBenchmark extends BaseBenchmark {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int TEXT_COUNT = 200;

  private Camera mCamera;
  private Texture mFontTexture;
  private Font mFont;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
  }

  @Override
  public void onLoadResources() {
    mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
    mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
        Typeface.BOLD), 22, true, Color.WHITE);
    getEngine().getTextureManager().loadTexture(mFontTexture);
    getEngine().getFontManager().loadFont(mFont);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);
    final Layer topLayer = scene.getTopLayer();

    for (int i = 0; i < TEXT_COUNT; i++) {
      final Text text = new TickerText(mRandom.nextInt(30),
          mRandom.nextFloat() * (CAMERA_HEIGHT - 20), mFont,
          "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
          HorizontalAlign.CENTER, 5 + 5 * mRandom.nextFloat());
      text.setColor(mRandom.nextFloat(), mRandom.nextFloat(), mRandom.nextFloat());
      topLayer.addEntity(text);
    }

    return scene;
  }

  @Override
  protected float getBenchmarkDuration() {
    return 10;
  }

  @Override
  protected float getBenchmarkStartOffset() {
    return 2;
  }
}