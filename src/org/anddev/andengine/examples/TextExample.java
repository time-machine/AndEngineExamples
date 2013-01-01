package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;

public class TextExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mFontTexture;
  private Font mFont;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());
    final Scene scene = new Scene(1);

    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
    final Text textCenter = new Text(100, 60, mFont,
        "Hello AndEngine!\nYou can even have multilined text!",
        HorizontalAlign.CENTER);
    final Text textLeft = new Text(100, 200, mFont,
        "Also left aligned!\nLorem ipsum dolor sit amat...",
        HorizontalAlign.LEFT);
    final Text textRight = new Text(100, 340, mFont,
        "And right aligned!\nLorem ipsum dolor sit amat...",
        HorizontalAlign.RIGHT);

    scene.getTopLayer().addEntity(textCenter);
    scene.getTopLayer().addEntity(textLeft);
    scene.getTopLayer().addEntity(textRight);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
    mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
        Typeface.BOLD), 32, true, Color.BLACK);
    getEngine().getTextureManager().loadTexture(mFontTexture);
    getEngine().getFontManager().loadFont(mFont);
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
