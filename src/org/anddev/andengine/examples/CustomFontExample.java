package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;

import android.graphics.Color;

public class CustomFontExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int FONT_SIZE = 48;

  private Camera mCamera;

  private Font mDroidFont;
  private Font mPlokFont;
  private Font mNeverwinterNightsFont;
  private Font mUnrealTournamentFont;
  private Font mKingdomOfHeartsFont;

  private Texture mDroidFontTexture;
  private Texture mPlokFontTexture;
  private Texture mNeverwinterNightsFontTexture;
  private Texture mUnrealTournamentFontTexture;
  private Texture mKingdomOfHeartsFontTexture;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    // the custom fonts
    mDroidFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
    mKingdomOfHeartsFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
    mNeverwinterNightsFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
    mPlokFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
    mUnrealTournamentFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);

    FontFactory.setAssetBasePath("font/");
    mDroidFont = FontFactory.createFromAsset(mDroidFontTexture, this,
        "Droid.ttf", FONT_SIZE, true, Color.BLACK);
    mKingdomOfHeartsFont = FontFactory.createFromAsset(
        mKingdomOfHeartsFontTexture, this, "KingdomOfHearts.ttf",
        FONT_SIZE + 20, true, Color.BLACK);
    mNeverwinterNightsFont = FontFactory.createFromAsset(
        mNeverwinterNightsFontTexture,
        this, "NeverwinterNights.ttf", FONT_SIZE, true, Color.BLACK);
    mPlokFont = FontFactory.createFromAsset(mPlokFontTexture, this, "Plok.ttf",
        FONT_SIZE, true, Color.BLACK);
    mUnrealTournamentFont = FontFactory.createFromAsset(
        mUnrealTournamentFontTexture, this, "UnrealTournament.ttf", FONT_SIZE,
        true, Color.BLACK);

    getEngine().getTextureManager().loadTextures(mDroidFontTexture,
        mKingdomOfHeartsFontTexture, mNeverwinterNightsFontTexture,
        mPlokFontTexture, mUnrealTournamentFontTexture);

    getEngine().getFontManager().loadFonts(mDroidFont, mKingdomOfHeartsFont,
        mNeverwinterNightsFont, mPlokFont, mUnrealTournamentFont);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final ILayer topLayer = scene.getTopLayer();

    topLayer.addEntity(new Text(230, 30, mDroidFont, "Droid Font"));
    topLayer.addEntity(new Text(160, 120, mKingdomOfHeartsFont,
        "Kingdom Of Hearts Font"));
    topLayer.addEntity(new Text(110, 210, mNeverwinterNightsFont,
        "Neverwinter Nights Font"));
    topLayer.addEntity(new Text(140, 300, mPlokFont, "Plok Font"));
    topLayer.addEntity(new Text(25, 390, mUnrealTournamentFont,
        "Unreal Tournament Font"));

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
