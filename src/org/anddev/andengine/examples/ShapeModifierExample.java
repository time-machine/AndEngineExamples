package org.anddev.andengine.examples;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.DelayModifier;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier.IShapeModifierListener;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier.ILoopShapeModifierListener;
import org.anddev.andengine.entity.shape.modifier.ParallelShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationByModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.LoopModifier;

import android.widget.Toast;

public class ShapeModifierExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final int centerX = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int centerY = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;

    final Rectangle rect = new Rectangle(centerX + 100, centerY, 32, 32);
    rect.setColor(1, 0, 0);
    rect.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

    final AnimatedSprite face = new AnimatedSprite(centerX - 100, centerY,
        mFaceTextureRegion);
    face.animate(100);

    final LoopShapeModifier shapeModifier = new LoopShapeModifier(
        new IShapeModifierListener() {
          @Override
          public void onModifierFinished(final IModifier<IShape> pShapeModifier,
              final IShape pShape) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Toast.makeText(ShapeModifierExample.this, "Sequence ended.",
                    Toast.LENGTH_LONG).show();
              }
            });
          }
        },
        1,
        new ILoopShapeModifierListener() {
          @Override
          public void onLoopFinished(
              final LoopModifier<IShape> pLoopShapeModifier,
              final int pLoopsRemaining) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                Toast.makeText(ShapeModifierExample.this, "Loops remaining:" +
                    pLoopsRemaining, Toast.LENGTH_SHORT).show();
              }
            });
          }
        },
        new SequenceShapeModifier(
            new RotationModifier(1, 0, 90),
            new AlphaModifier(2, 1, 0),
            new AlphaModifier(1, 0, 1),
            new ScaleModifier(2, 1, 0.5f),
            new DelayModifier(0.5f),
            new ParallelShapeModifier(
                new ScaleModifier(3, 0.5f, 5),
                new RotationByModifier(3, 90)
            ),
            new ParallelShapeModifier(
                new ScaleModifier(3, 5, 1),
                new RotationModifier(3, 180, 0)
            )
        )
    );

    face.addShapeModifier(shapeModifier);
    rect.addShapeModifier(shapeModifier.clone());

    scene.getTopLayer().addEntity(face);
    scene.getTopLayer().addEntity(rect);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/face_box_tiled.png", 0, 0, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public void onLoadComplete() {
  }
}
