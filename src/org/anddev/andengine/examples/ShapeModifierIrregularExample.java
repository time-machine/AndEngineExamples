package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier.IShapeModifierListener;
import org.anddev.andengine.entity.shape.modifier.ParallelModifier;
import org.anddev.andengine.entity.shape.modifier.RotationByModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceModifier;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.widget.Toast;

public class ShapeModifierIrregularExample extends BaseExample {
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
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
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

    final SequenceModifier shapeModifier = new SequenceModifier(
        new IShapeModifierListener() {
          @Override
          public void onModifierFinished(final IShapeModifier pShapeModifier,
              final IShape pShape) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Toast.makeText(ShapeModifierIrregularExample.this,
                    "Sequence ended.", Toast.LENGTH_LONG).show();
              }
            });
          }
        },
        new ScaleModifier(2, 1, 0.75f, 1, 2),
        new ScaleModifier(2, 0.75f, 2, 2, 1.25f),
        new ParallelModifier(
            new ScaleModifier(3, 2, 5, 1.25f, 5),
            new RotationByModifier(3, 180)
        ),
        new ParallelModifier(
            new ScaleModifier(3, 5, 1),
            new RotationModifier(3, 180, 0)
        )
    );

    face1.addShapeModifier(shapeModifier);
    face2.addShapeModifier(shapeModifier.clone());

    scene.getTopLayer().addEntity(face1);
    scene.getTopLayer().addEntity(face2);

    // create some not-modified sprites, that act as fixed references to the
    // modified ones
    final AnimatedSprite face1Reference = new AnimatedSprite(centerX - 100,
        centerY, mFaceTextureRegion);
    final AnimatedSprite face2Reference = new AnimatedSprite(centerX + 100,
        centerY, mFaceTextureRegion);

    scene.getTopLayer().addEntity(face1Reference);
    scene.getTopLayer().addEntity(face2Reference);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
