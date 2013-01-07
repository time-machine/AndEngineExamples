package org.anddev.andengine.examples;

import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_X;
import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_Y;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.modifier.LoopModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.MathUtils;

import android.widget.Toast;
public class CoordinateConversionExample extends BaseExample {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;

  private Camera mCamera;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;

  private Texture mOnScreenControlTexture;
  private TextureRegion mOnScreenControlBaseTextureRegion;
  private TextureRegion mOnScreenControlKnobTextureRegion;

  private boolean mPlaceOnScreenControlsAtDifferentVerticalLocations = false;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "The arrow will always point to the left eye of the " +
        "face!", Toast.LENGTH_LONG).show();
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    final Engine engine = new Engine(new EngineOptions(true,
        ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));

    try {
      if (MultiTouch.isSupported(this)) {
        engine.setTouchController(new MultiTouchController());

        if (MultiTouch.isSupportedDistinct(this)) {
          Toast.makeText(this, "MultiTouch detected --> Both controls will work" +
              "properly!", Toast.LENGTH_LONG).show();
        }
        else {
          mPlaceOnScreenControlsAtDifferentVerticalLocations = true;

          Toast.makeText(this, "MultiTouch detected, but your device has " +
              "problems distinguishing between fingers.\n\nControls are " +
              "placed at different vertical locations.",
              Toast.LENGTH_LONG).show();
        }
      }
      else {
        Toast.makeText(this, "Sorry your device does NOT support MultiTouch!" +
            "\n\n(Falling back to SingleTouch.)", Toast.LENGTH_LONG).show();
      }
    }
    catch (final MultiTouchException e) {
      Toast.makeText(this, "Sorry your Android Version does NOT support" +
          "MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are" +
          "placed at different vertical locations.", Toast.LENGTH_LONG).show();
    }

    return engine;
  }

  @Override
  public void onLoadResources() {
    TextureRegionFactory.setAssetBasePath("gfx/");

    mTexture = new Texture(32, 32, TextureOptions.BILINEAR);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "face_box.png", 0, 0);

    mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR);
    mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(
        mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
    mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(
        mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);

    mEngine.getTextureManager().loadTextures(mTexture, mOnScreenControlTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(2);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    // create three lines that will form an arrow pointing to the eye
    final Line arrowLineMain = new Line(0, 0, 0, 0, 3);
    final Line arrowLineWingLeft = new Line(0, 0, 0, 0, 3);
    final Line arrowLineWingRight = new Line(0, 0, 0, 0, 3);

    arrowLineMain.setColor(1, 0, 0);
    arrowLineWingLeft.setColor(1, 0, 0);
    arrowLineWingRight.setColor(1, 0, 0);

    scene.getTopLayer().addEntity(arrowLineMain);
    scene.getTopLayer().addEntity(arrowLineWingLeft);
    scene.getTopLayer().addEntity(arrowLineWingRight);

    final int centerX = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int centerY = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;

    final Sprite face = new Sprite(centerX, centerY, mFaceTextureRegion) {
      @Override
      protected void onManagedUpdate(final float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        final float[] eyeCoordinates = convertLocalToSceneCoordinates(11, 13);
        final float eyeX = eyeCoordinates[VERTEX_INDEX_X];
        final float eyeY = eyeCoordinates[VERTEX_INDEX_Y];

        arrowLineMain.setPosition(eyeX, eyeY, eyeX, eyeY - 50);
        arrowLineWingLeft.setPosition(eyeX, eyeY, eyeX - 10, eyeY - 10);
        arrowLineWingRight.setPosition(eyeX, eyeY, eyeX + 10, eyeY - 10);
      }
    };

    face.addShapeModifier(new LoopModifier(new SequenceModifier(
        new ScaleModifier(3, 1, 1.75f), new ScaleModifier(3, 1.75f, 1))));

    scene.getBottomLayer().addEntity(face);

    // velocity control (left)
    final int x1 = 0;
    final int y1 = CAMERA_HEIGHT - mOnScreenControlBaseTextureRegion.getHeight();

    final AnalogOnScreenControl velocityOnScreenControl =
        new AnalogOnScreenControl(x1, y1, mCamera,
            mOnScreenControlBaseTextureRegion,
            mOnScreenControlKnobTextureRegion, 0.1f,
            new IAnalogOnScreenControlListener() {
          @Override
          public void onControlChange(
              final BaseOnScreenControl pBaseOnScreenControl,
              final float pValueX, final float pValueY) {
            face.setVelocity(pValueX * 100, pValueY * 100);
          }

          @Override
          public void onControlClick(
              final AnalogOnScreenControl pAnalogOnScreenControl) {
            // nothing
          }
        });

    velocityOnScreenControl.getControlBase().setAlpha(0.5f);

    scene.setChildScene(velocityOnScreenControl);

    // Rotation control (right)
    final int x2 = CAMERA_WIDTH - mOnScreenControlBaseTextureRegion.getWidth();
    final int y2 = (mPlaceOnScreenControlsAtDifferentVerticalLocations)? 0 : y1;

    final AnalogOnScreenControl rotationOnScreenControl =
        new AnalogOnScreenControl(x2, y2, mCamera,
            mOnScreenControlBaseTextureRegion,
            mOnScreenControlKnobTextureRegion, 0.1f,
            new IAnalogOnScreenControlListener() {
          @Override
          public void onControlChange(
              final BaseOnScreenControl pBaseOnScreenControl,
              final float pValueX, final float pValueY) {
            if (pValueX == x1 && pValueX == x1) {
              face.setRotation(x1);
            }
            else {
              face.setRotation(MathUtils.radToDeg((float)Math.atan2(pValueX,
                  -pValueY)));
            }
          }

          @Override
          public void onControlClick(
              final AnalogOnScreenControl pAnalogOnScreenControl) {
            // nothing
          }
        });

    rotationOnScreenControl.getControlBase().setAlpha(0.5f);

    velocityOnScreenControl.setChildScene(rotationOnScreenControl);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
