package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class DigitalOnScreenControlExample extends BaseExample {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;

  private Camera mCamera;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  private Texture mOnScreenControlTexture;
  private TextureRegion mOnScreenControlBaseTextureRegion;
  private TextureRegion mOnScreenControlKnobTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    TextureRegionFactory.setAssetBasePath("gfx/");

    mTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "face_box.png", 0, 0);

    mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(
        mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
    mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(
        mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);

    getEngine().getTextureManager().loadTextures(mTexture, mOnScreenControlTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final int centerX = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int centerY = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;
    final Sprite face = new Sprite(centerX, centerY, mFaceTextureRegion);

    scene.getTopLayer().addEntity(face);

    final DigitalOnScreenControl digitalOnScreenControl = new DigitalOnScreenControl(
        0, CAMERA_HEIGHT - mOnScreenControlBaseTextureRegion.getHeight(),
        mCamera, mOnScreenControlBaseTextureRegion,
        mOnScreenControlKnobTextureRegion, 0.1f, new IOnScreenControlListener() {
          @Override
          public void onControlChange(
              final BaseOnScreenControl pBaseOnScreenControl,
              final float pValueX, final float pValueY) {
            face.setVelocity(pValueX * 100, pValueY * 100);
          }
        });

    digitalOnScreenControl.getControlBase().setAlpha(0.5f);
    digitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
    digitalOnScreenControl.getControlBase().setScale(1.25f);
    digitalOnScreenControl.getControlKnob().setScale(1.25f);
    digitalOnScreenControl.refreshControlKnobPosition();

    scene.setChildScene(digitalOnScreenControl);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
