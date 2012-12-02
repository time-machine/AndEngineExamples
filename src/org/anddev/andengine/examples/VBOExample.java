package org.anddev.andengine.examples;

import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.Menu;
import android.view.MenuItem;

public class VBOExample extends BaseGameActivity {
  private static final long RANDOM_SEED = 1234567890;

  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int SPRITE_COUNT = 500;

  private static final int MENU_VBO_TOGGLE = Menu.FIRST;

  private Camera mCamera;
  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final Random random = new Random(RANDOM_SEED);
    for (int i = 0; i < SPRITE_COUNT; i++) {
      final AnimatedSprite face = new AnimatedSprite(
          random.nextFloat() * (CAMERA_WIDTH - 32),
          random.nextFloat() * (CAMERA_HEIGHT - 32), mFaceTextureRegion);
      scene.getTopLayer().addEntity(face);
    }

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/boxface_tiled.png", 0, 0, 2, 1);
    TextureManager.loadTexture(mTexture);
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

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    menu.add(Menu.NONE, MENU_VBO_TOGGLE, Menu.NONE, "Toggle VBO");
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
    switch (item.getItemId()) {
    case MENU_VBO_TOGGLE:
      GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS =
      !GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS;
      break;
    default:
      break;
    }
    return super.onMenuItemSelected(featureId, item);
  }

  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    menu.findItem(MENU_VBO_TOGGLE).setTitle(
        GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS ?
            "Disable VBOs" : "Enable VBOs");
    return super.onPrepareOptionsMenu(menu);
  }
}
