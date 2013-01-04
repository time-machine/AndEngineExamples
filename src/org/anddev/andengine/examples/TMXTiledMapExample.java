package org.anddev.andengine.examples;

import java.io.IOException;
import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.LoopModifier;
import org.anddev.andengine.entity.shape.modifier.PathModifier;
import org.anddev.andengine.entity.shape.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.Path;

import android.widget.Toast;

public class TMXTiledMapExample extends BaseExample {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;

  private Camera mChaseCamera;

  private Texture mTexture;
  private TiledTextureRegion mPlayerTextureRegion;
  private TMXTiledMap mTmxTiledMap;
  protected int mCactusCount;

  @Override
  public Engine onLoadEngine() {
    mChaseCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mChaseCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(128, 128, TextureOptions.DEFAULT);
    mPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/player.png", 0, 0, 3, 4);

    mEngine.getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(2);

    try {
      final TMXLoader tmxLoader = new TMXLoader(this,
          mEngine.getTextureManager(), new ITMXTilePropertiesListener() {
        @Override
        public void onTMXTileWithPropertiesCreated(
            final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer,
            final ArrayList<TMXTileProperty> pTMXTileProperties,
            final int pTileRow, final int pTileColumn, final int pTileWidth,
            final int pTileHeight) {
          final int tmxTilePropertyCount = pTMXTileProperties.size();
          for (int i = 0; i < tmxTilePropertyCount; i++) {
            final TMXTileProperty tmxTileProperty = pTMXTileProperties.get(i);

            if (tmxTileProperty.getName().equals("cactus") &&
                tmxTileProperty.getValue().equals("true")) {
              mCactusCount++;
            }
          }
        }
      });

      mTmxTiledMap = tmxLoader.load(getAssets().open("tmx/desert.tmx"));

      Toast.makeText(this, "Cactus count in this TMXTiledMap: " + mCactusCount,
          Toast.LENGTH_LONG).show();
    }
    catch (final IOException e) {
      Debug.e(e);
    }

    scene.getBottomLayer().addEntity(mTmxTiledMap.getTMXLayers().get(0));

    final int centerX = (CAMERA_WIDTH - mPlayerTextureRegion.getTileWidth())
        / 2;
    final int centerY = (CAMERA_HEIGHT - mPlayerTextureRegion.getTileHeight())
        / 2;

    // create the sprite and add it to the scene
    final AnimatedSprite player = new AnimatedSprite(centerX, centerY,
        mPlayerTextureRegion);
    mChaseCamera.setChaseShape(player);

    final Path path = new Path(5).to(240, 160).to(240, 600).to(600, 600)
        .to(600, 160).to(240, 160);

    // add the proper animation when a waypoint of the path is passed
    player.addShapeModifier(new LoopModifier(new PathModifier(30, path, null,
        new IPathModifierListener() {
          @Override
          public void onWaypointPassed(final PathModifier pPathModifier,
              final IShape pShape, final int pWaypointIndex) {
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

    scene.getTopLayer().addEntity(player);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
