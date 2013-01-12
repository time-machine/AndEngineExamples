package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.opengl.texture.source.decorator.ColorKeyTextureSourceDecorator;

import android.graphics.Color;

public class ColorKeyTextureSourceDecoratorExample extends BaseExample {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;

  private Camera mCamera;

  private Texture mTexture;

  private TextureRegion mChromaticCircleTextureRegion;
  private TextureRegion mChromaticCircleColorKeyTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

    // the actual AssetTextureSource
    final AssetTextureSource baseTextureSource = new AssetTextureSource(this,
        "gfx/chromatic_circle.png");

    mChromaticCircleTextureRegion = TextureRegionFactory.createFromSource(
        mTexture, baseTextureSource, 0, 0);

    // we will remove both the red and the green segment of the chromatic
    // circle, by nesting two ColorKeyTextureSourceDecorators around the actual
    // baseTextureSource
    final int colorKeyRed = Color.rgb(255, 0, 51); // red segment
    final int colorKeyGreen = Color.rgb(0,  179, 0); // green segment
    final ColorKeyTextureSourceDecorator colorKeyTextureSource =
        new ColorKeyTextureSourceDecorator(new ColorKeyTextureSourceDecorator(
            baseTextureSource, colorKeyRed), colorKeyGreen);

    mChromaticCircleColorKeyTextureRegion = TextureRegionFactory
        .createFromSource(mTexture, colorKeyTextureSource, 128, 0);

    mEngine.getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);

    final int centerX = (CAMERA_WIDTH -
        mChromaticCircleTextureRegion.getWidth()) / 2;
    final int centerY = (CAMERA_HEIGHT -
        mChromaticCircleTextureRegion.getHeight()) / 2;

    final Sprite chromaticCircle = new Sprite(centerX - 80, centerY,
        mChromaticCircleTextureRegion);

    final Sprite chromaticCircleColorKeyed = new Sprite(centerX + 80, centerY,
        mChromaticCircleColorKeyTextureRegion);

    final ILayer topLayer = scene.getTopLayer();
    topLayer.addEntity(chromaticCircle);
    topLayer.addEntity(chromaticCircleColorKeyed);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
