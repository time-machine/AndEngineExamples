package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationByModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.modifier.IModifier;

import android.widget.Toast;

public class EntityModifierIrregularExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Shapes can have variable rotation and scale centers.",
        Toast.LENGTH_LONG).show();
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/face_box_tiled.png", 0, 0, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final int centerX = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int centerY = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;

    final AnimatedSprite face1 = new AnimatedSprite(centerX - 100, centerY,
        mFaceTextureRegion);
    face1.setRotationCenter(0, 0);
    face1.setScaleCenter(0, 0);
    face1.animate(100);

    final AnimatedSprite face2 = new AnimatedSprite(centerX + 100, centerY,
        mFaceTextureRegion);
    face2.animate(100);

    final SequenceEntityModifier shapeModifier = new SequenceEntityModifier(
        new IEntityModifierListener() {
          @Override
          public void onModifierFinished(
              final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Toast.makeText(EntityModifierIrregularExample.this,
                    "Sequence ended.", Toast.LENGTH_LONG).show();
              }
            });
          }
        },
        new ScaleModifier(2, 1, 0.75f, 1, 2),
        new ScaleModifier(2, 0.75f, 2, 2, 1.25f),
        new ParallelEntityModifier(
            new ScaleModifier(3, 2, 5, 1.25f, 5),
            new RotationByModifier(3, 180)
        ),
        new ParallelEntityModifier(
            new ScaleModifier(3, 5, 1),
            new RotationModifier(3, 180, 0)
        )
    );

    face1.addEntityModifier(shapeModifier);
    face2.addEntityModifier(shapeModifier.clone());

    scene.getLastChild().attachChild(face1);
    scene.getLastChild().attachChild(face2);

    // create some not-modified sprites, that act as fixed references to the
    // modified ones
    final AnimatedSprite face1Reference = new AnimatedSprite(centerX - 100,
        centerY, mFaceTextureRegion);
    final AnimatedSprite face2Reference = new AnimatedSprite(centerX + 100,
        centerY, mFaceTextureRegion);

    scene.getLastChild().attachChild(face1Reference);
    scene.getLastChild().attachChild(face2Reference);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
