package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.Texture.ITextureStateListener;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.widget.Toast;

public class ImageFormatsExample extends BaseExample {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mPngTextureRegion;
  private TextureRegion mJpgTextureRegion;
  private TextureRegion mGifTextureRegion;
  private TextureRegion mBmpTextureRegion;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "GIF is not supported yet. " +
        "Use PNG instead, it's a better format anyway!",
        Toast.LENGTH_LONG).show();
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(128, 128, TextureOptions.BILINEAR,
        new ITextureStateListener.TextureStateAdapter() {
      @Override
      public void onTextureSourceLoadExeption(final Texture pTexture,
          final ITextureSource pTextureSource, final Throwable pThrowable) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(ImageFormatsExample.this,
                "Failed loading TextureSource: " + pTextureSource.toString(),
                Toast.LENGTH_LONG).show();
          }
        });
      }
    });

    mPngTextureRegion = TextureRegionFactory.createFromAsset(mTexture,
        this, "gfx/imageformat_png.png", 0, 0);
    mJpgTextureRegion = TextureRegionFactory.createFromAsset(mTexture,
        this, "gfx/imageformat_jpg.jpg", 49, 0);
    mGifTextureRegion = TextureRegionFactory.createFromAsset(mTexture,
        this, "gfx/imageformat_gif.gif", 0, 49);
    mBmpTextureRegion = TextureRegionFactory.createFromAsset(mTexture,
        this, "gfx/imageformat_bmp.bmp", 49, 49);

    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    // create the icons and add them to the scene
    final ILayer topLayer = scene.getTopLayer();

    topLayer.addEntity(new Sprite(160 - 24, 106 - 24, mPngTextureRegion));
    topLayer.addEntity(new Sprite(160 - 24, 213 - 24, mJpgTextureRegion));
    topLayer.addEntity(new Sprite(320 - 24, 106 - 24, mGifTextureRegion));
    topLayer.addEntity(new Sprite(320 - 24, 213 - 24, mBmpTextureRegion));

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
