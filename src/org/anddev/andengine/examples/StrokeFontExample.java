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
import org.anddev.andengine.opengl.font.StrokeFont;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;

import android.graphics.Color;
import android.graphics.Typeface;

public class StrokeFontExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int FONT_SIZE = 48;

  private Camera mCamera;

  private Texture mFontTexture;
  private Texture mStrokeFontTexture;
  private Texture mStrokeOnlyFontTexture;

  private Font mFont;
  private StrokeFont mStrokeFont;
  private StrokeFont mStrokeOnlyFont;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mStrokeFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mStrokeOnlyFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

    mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
        Typeface.BOLD), FONT_SIZE, true, Color.BLACK);
    mStrokeFont = new StrokeFont(mStrokeFontTexture,
        Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE, true,
        Color.BLACK, 2, Color.WHITE);
    mStrokeOnlyFont = new StrokeFont(mStrokeOnlyFontTexture,
        Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE, true,
        Color.BLACK, 2, Color.WHITE, true);

    mEngine.getTextureManager().loadTextures(mFontTexture, mStrokeFontTexture,
        mStrokeOnlyFontTexture);
    mEngine.getFontManager().loadFonts(mFont, mStrokeFont, mStrokeOnlyFont);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final Text textNormal = new Text(100, 100, mFont, "Just some normal Text.");
    final Text textStroke = new Text(100, 200, mStrokeFont,
        "Text with fill and stroke.");
    final Text textStrokeOnly = new Text(100, 300, mStrokeOnlyFont,
        "Text with stroke only.");

    scene.getLastChild().addChild(textNormal);
    scene.getLastChild().addChild(textStroke);
    scene.getLastChild().addChild(textStrokeOnly);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
