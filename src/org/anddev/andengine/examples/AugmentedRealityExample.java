package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.modifier.MoveModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.augmentedreality.BaseAugmentedRealityGameActivity;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.widget.Toast;

public class AugmentedRealityExample extends BaseAugmentedRealityGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  @Override
  public Scene onLoadScene() {
    mEngine.registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0, 0, 0, 0));

    final int centerX = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int centerY = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;
    final Sprite face = new Sprite(centerX, centerY, mFaceTextureRegion);
    face.addShapeModifier(new MoveModifier(30, 0, CAMERA_WIDTH - face.getWidth(),
        0, CAMERA_HEIGHT - face.getHeight()));
    scene.getTopLayer().addEntity(face);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/box_face.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "If you don't see a sprite moving over the screen, " +
        "try starting this while already being in Landscape orientation!!",
        Toast.LENGTH_LONG).show();
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }
}
