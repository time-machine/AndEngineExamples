package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;

public class CustomFontExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;

  private Texture mFontTexture;
  private Font mFont;

  private Texture mCustomFontTexture;
  private Font mCustomFont;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
  }

  @Override
  public void onLoadResources() {
    mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
    mCustomFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);

    mFont = FontFactory.create(mFontTexture,
        Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, true, Color.BLACK);
    FontFactory.setAssetBasePath("fonts/");
    mCustomFont = FontFactory.createFromAsset(mCustomFontTexture, this,
        "DroidSans.ttf", 48, true, Color.BLACK);

    getEngine().getTextureManager().loadTextures(mFontTexture, mCustomFontTexture);
    getEngine().getFontManager().loadFonts(mFont, mCustomFont);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final Text textDefaultFont = new Text(100, 140, mFont,
        "Default font example... ", HorizontalAlign.CENTER);
    final Text textCustomFont = new Text(90, 280, mCustomFont,
        "Custom font example... ", HorizontalAlign.CENTER);

    scene.getTopLayer().addEntity(textDefaultFont);
    scene.getTopLayer().addEntity(textCustomFont);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
