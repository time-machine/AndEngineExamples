package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class AutoParallaxBackgroundExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;

  private Texture mTexture;
  private TiledTextureRegion mPlayerTextureRegion;
  private TiledTextureRegion mEnemyTextureRegion;

  private Texture mAutoParallaxBackgroundTexture;

  private TextureRegion mParallaxLayerBack;
  private TextureRegion mParallaxLayerMid;
  private TextureRegion mParallaxLayerFront;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/player.png", 0, 0, 3, 4);
    mEnemyTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/enemy.png", 73, 0, 3, 4);

    mAutoParallaxBackgroundTexture = new Texture(1024, 1024,
        TextureOptions.DEFAULT);
    mParallaxLayerFront = TextureRegionFactory.createFromAsset(
        mAutoParallaxBackgroundTexture, this,
        "gfx/parallax_background_layer_front.png", 0, 0);
    mParallaxLayerBack = TextureRegionFactory.createFromAsset(
        mAutoParallaxBackgroundTexture, this,
        "gfx/parallax_background_layer_back.png", 0, 188);
    mParallaxLayerMid = TextureRegionFactory.createFromAsset(
        mAutoParallaxBackgroundTexture, this,
        "gfx/parallax_background_layer_mid.png", 0, 669);

    getEngine().getTextureManager().loadTextures(mTexture,
        mAutoParallaxBackgroundTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    final AutoParallaxBackground autoParallaxBackground =
        new AutoParallaxBackground(0, 0, 0, 5);
    autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0,
        new Sprite(0, CAMERA_HEIGHT - mParallaxLayerBack.getHeight(),
            mParallaxLayerBack)));
    autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5,
        new Sprite(0, 80, mParallaxLayerMid)));
    autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10,
        new Sprite(0, CAMERA_HEIGHT - mParallaxLayerFront.getHeight(),
            mParallaxLayerFront)));

    scene.setBackground(autoParallaxBackground);

    final int playerX = (CAMERA_WIDTH - mPlayerTextureRegion.getTileWidth())
        / 2;
    final int playerY = (CAMERA_HEIGHT - mPlayerTextureRegion.getTileHeight())
        - 5;

    // create two sprites and add it to the scene
    final AnimatedSprite player = new AnimatedSprite(playerX, playerY,
        mPlayerTextureRegion);
    player.setScaleCenterY(mPlayerTextureRegion.getTileHeight());
    player.setScale(2);
    player.animate(new long[]{200, 200, 200}, 3, 5, true);

    final AnimatedSprite enemy = new AnimatedSprite(playerX - 80, playerY,
        mEnemyTextureRegion);
    enemy.setScaleCenterY(mEnemyTextureRegion.getTileHeight());
    enemy.setScale(2);
    enemy.animate(new long[]{200, 200, 200}, 3, 5, true);

    scene.getLastChild().attachChild(player);
    scene.getLastChild().attachChild(enemy);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
