package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.modifier.PathModifier.Path;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;

public class RepeatingSpriteBackgroundExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;

  private RepeatingSpriteBackground mGrassBackground;

  private Texture mTexture;

  private TiledTextureRegion mPlayerTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(128, 128, TextureOptions.DEFAULT);
    mPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/player.png", 0, 0, 3, 4);
    mGrassBackground = new RepeatingSpriteBackground(CAMERA_WIDTH,
        CAMERA_HEIGHT, mEngine.getTextureManager(), new AssetTextureSource(
            this, "gfx/background_grass.png"));

    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(mGrassBackground);

    final int centerX = (CAMERA_WIDTH - mPlayerTextureRegion.getTileWidth())
        / 2;
    final int centerY = (CAMERA_HEIGHT - mPlayerTextureRegion.getTileHeight())
        / 2;

    // create the sprite and add it to the scene
    final AnimatedSprite player = new AnimatedSprite(centerX, centerY, 48, 64,
        mPlayerTextureRegion);

    final Path path = new Path(5).to(10, 10).to(10, CAMERA_HEIGHT - 74)
        .to(CAMERA_WIDTH - 58, CAMERA_HEIGHT - 74).to(CAMERA_WIDTH - 58, 10)
        .to(10, 10);

    // add the proper animation when a waypoint of the path is passed
    player.registerEntityModifier(new LoopEntityModifier(new PathModifier(30, path, null,
        new IPathModifierListener() {
          @Override
          public void onWaypointPassed(final PathModifier pPathModifier,
              final IEntity pEntity, final int pWaypointIndex) {
            switch (pWaypointIndex) {
            case 0:
              player.animate(new long[]{200, 200, 200}, 6, 8, true);
              break;
            case 1:
              player.animate(new long[]{200, 200, 200}, 3, 5, true);
              break;
            case 2:
              player.animate(new long[]{200, 200, 200}, 0, 2, true);
              break;
            case 3:
              player.animate(new long[]{200, 200, 200}, 9, 11, true);
              break;
            }
          }
        })));

    scene.getLastChild().attachChild(player);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
