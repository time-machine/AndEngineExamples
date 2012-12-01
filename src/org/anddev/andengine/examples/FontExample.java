package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.text.Text.HorizontalAlign;
import org.anddev.andengine.opengl.text.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;
import android.graphics.Typeface;

public class FontExample extends BaseGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mFontTexture;
  private Font mFont;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());
    Scene scene = new Scene(1);

    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);
    Text text = new Text(100, 50, mFont,
        "Hello AndEngine!\nYou can even have multilined text!",
        HorizontalAlign.CENTER);
    scene.getTopLayer().addEntity(text);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mFontTexture = new Texture(256, 256);
    mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
        Typeface.BOLD), 24, true, Color.RED);
    getEngine().loadTexture(mFontTexture);
    getEngine().loadFont(mFont);
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }
}
