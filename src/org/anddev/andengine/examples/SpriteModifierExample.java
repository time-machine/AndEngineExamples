package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.entity.sprite.IModifierListener;
import org.anddev.andengine.entity.sprite.ISpriteModifier;
import org.anddev.andengine.entity.sprite.modifier.AlphaModifier;
import org.anddev.andengine.entity.sprite.modifier.DelayModifier;
import org.anddev.andengine.entity.sprite.modifier.RotateByModifier;
import org.anddev.andengine.entity.sprite.modifier.RotateModifier;
import org.anddev.andengine.entity.sprite.modifier.ScaleModifier;
import org.anddev.andengine.entity.sprite.modifier.SequenceModifier;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.widget.Toast;

public class SpriteModifierExample extends BaseGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mTexture;
  private TiledTextureRegion mFaceTextureRegion;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final int x = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;
    final AnimatedSprite face = new AnimatedSprite(x, y, mFaceTextureRegion);
    face.animate(100);

    face.addSpriteModifier(new SequenceModifier(new IModifierListener() {
      @Override
      public void onModifierFinished(final ISpriteModifier pSpriteModifier,
          final BaseSprite pBaseSprite) {
        SpriteModifierExample.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(SpriteModifierExample.this, "Sequence ended",
                Toast.LENGTH_LONG).show();
          }
        });
      }
    },
    new RotateByModifier(5, 90),
    new DelayModifier(2),
    new AlphaModifier(3, 1, 0),
    new AlphaModifier(3, 0, 1),
    new ScaleModifier(3, 1, 0.5f),
    new ScaleModifier(3, 0.5f, 5),
    new ScaleModifier(3, 5, 1),
    new RotateModifier(5, 45, 90),
    new RotateByModifier(5, -90)));

    scene.getTopLayer().addEntity(face);
    scene.getTopLayer().addEntity(new AnimatedSprite(x, y, mFaceTextureRegion));

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32);
    mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/boxface_tiled.png", 0, 0, 2, 1);
    getEngine().loadTexture(mTexture);
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
}
