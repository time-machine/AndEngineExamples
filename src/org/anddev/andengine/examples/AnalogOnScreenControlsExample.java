package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.OnScreenControlListener;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.MathUtils;

public class AnalogOnScreenControlsExample extends BaseExample {
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
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/boxface.png", 0, 0);

    mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR);
    mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(
        mOnScreenControlTexture, this, "gfx/onscreen_control_base.png",
        0, 0);
    mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(
        mOnScreenControlTexture, this, "gfx/onscreen_control_knob.png",
        128, 0);

    mEngine.getTextureManager().loadTextures(mTexture, mOnScreenControlTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final int x = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int y = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;
    final Sprite face = new Sprite(x, y, mFaceTextureRegion);

    scene.getTopLayer().addEntity(face);

    // velocity control (left)
    final AnalogOnScreenControl velocityOnScreenControl =
        new AnalogOnScreenControl(
            0, CAMERA_HEIGHT - mOnScreenControlBaseTextureRegion.getHeight(),
            mCamera, mOnScreenControlBaseTextureRegion,
            mOnScreenControlKnobTextureRegion, 0.1f,
            new OnScreenControlListener() {
          @Override
          public void onControlChange(
              final BaseOnScreenControl pBaseOnScreenControl,
              final float pValueX, final float pValueY) {
            face.setVelocity(pValueX * 100, pValueY * 100);
          }
        });

    velocityOnScreenControl.getControlBase().setAlpha(0.5f);
    velocityOnScreenControl.getControlKnob().setAlpha(0.5f);

    scene.setChildScene(velocityOnScreenControl);

    // rotation control (right)
    final AnalogOnScreenControl rotationOnScreenControl =
        new AnalogOnScreenControl(
            CAMERA_WIDTH - mOnScreenControlBaseTextureRegion.getWidth(),
            CAMERA_HEIGHT - mOnScreenControlBaseTextureRegion.getHeight(),
            mCamera, mOnScreenControlBaseTextureRegion,
            mOnScreenControlKnobTextureRegion, 0.1f,
            new OnScreenControlListener() {
          @Override
          public void onControlChange(
              final BaseOnScreenControl pBaseOnScreenControl,
              final float pValueX, final float pValueY) {
            if (pValueX == 0 && pValueY == 0) {
              face.setRotation(0);
            }
            else {
              face.setRotation(MathUtils.radToDeg(
                  (float)Math.atan2(pValueX, -pValueY)));
            }
          }
        });

    rotationOnScreenControl.getControlBase().setAlpha(0.5f);
    rotationOnScreenControl.getControlKnob().setAlpha(0.5f);

    velocityOnScreenControl.setChildScene(rotationOnScreenControl);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}