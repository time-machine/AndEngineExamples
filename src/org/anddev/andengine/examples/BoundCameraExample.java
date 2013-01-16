package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.SingleSceneSplitScreenEngine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class BoundCameraExample extends BaseExample implements
    IAccelerometerListener, IOnSceneTouchListener {
  private static final int CAMERA_WIDTH = 400;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private BoundCamera mBoundChaseCamera;

  private PhysicsWorld mPhysicsWorld;

  private Texture mTexture;
  private TiledTextureRegion mBoxFaceTextureRegion;

  private Texture mHUDTexture;
  private TiledTextureRegion mToggleButtonTextureRegion;

  private int mFaceCount;

  private final Vector2 mTempVector = new Vector2();

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to add boxes.", Toast.LENGTH_LONG)
        .show();
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH / 2,
        CAMERA_HEIGHT / 2, 0, CAMERA_WIDTH, 0, CAMERA_HEIGHT);

    return new SingleSceneSplitScreenEngine(new EngineOptions(true,
        ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH * 2,
            CAMERA_HEIGHT), mCamera), mBoundChaseCamera);
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/face_box_tiled.png", 0, 0, 2, 1);
    mEngine.getTextureManager().loadTexture(mTexture);

    mHUDTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mToggleButtonTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mHUDTexture, this, "gfx/toggle_button.png", 0, 0, 2, 1);
    mEngine.getTextureManager().loadTexture(mHUDTexture);

    enableAccelerometerSensor(this);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(2);
    scene.setOnSceneTouchListener(this);

    mPhysicsWorld = new PhysicsWorld(new Vector2(0,
        SensorManager.GRAVITY_EARTH), false);

    final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
    final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
    final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
    final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

    final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f,
        0.5f);
    PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyType.StaticBody,
        wallFixtureDef);
    PhysicsFactory.createBoxBody(mPhysicsWorld, roof, BodyType.StaticBody,
        wallFixtureDef);
    PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyType.StaticBody,
        wallFixtureDef);
    PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyType.StaticBody,
        wallFixtureDef);

    scene.getFirstChild().attachChild(ground);
    scene.getFirstChild().attachChild(roof);
    scene.getFirstChild().attachChild(left);
    scene.getFirstChild().attachChild(right);

    getEngine().registerUpdateHandler(mPhysicsWorld);

    final HUD hud = new HUD();
    final TiledSprite toggleButton = new TiledSprite(
        CAMERA_WIDTH / 2 - mToggleButtonTextureRegion.getTileWidth(),
        CAMERA_HEIGHT / 2 - mToggleButtonTextureRegion.getTileHeight(),
        mToggleButtonTextureRegion) {
      @Override
      public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
          final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
          final boolean boundsEnabled = mBoundChaseCamera.isBoundsEnabled();
          if (boundsEnabled) {
            mBoundChaseCamera.setBoundsEnabled(false);
            setCurrentTileIndex(1);
            Toast.makeText(BoundCameraExample.this, "Bounds Disabled.",
                Toast.LENGTH_SHORT).show();
          }
          else {
            mBoundChaseCamera.setBoundsEnabled(true);
            setCurrentTileIndex(0);
            Toast.makeText(BoundCameraExample.this, "Bounds Enabled.",
                Toast.LENGTH_SHORT).show();
          }
        }
        return true;
      }
    };

    hud.registerTouchArea(toggleButton);
    hud.getFirstChild().attachChild(toggleButton);

    mBoundChaseCamera.setHUD(hud);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public boolean onSceneTouchEvent(final Scene pScene,
      final TouchEvent pSceneTouchEvent) {
    if (mPhysicsWorld != null) {
      if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
        runOnUpdateThread(new Runnable() {
          @Override
          public void run() {
            addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
          }
        });
        return true;
      }
    }
    return false;
  }

  @Override
  public void onAccelerometerChanged(
      final AccelerometerData pAccelerometerData) {
    mTempVector.set(pAccelerometerData.getY(), pAccelerometerData.getX());
    mPhysicsWorld.setGravity(mTempVector);
  }

  private void addFace(final float pX, final float pY) {
    final Scene scene = mEngine.getScene();

    final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f,
        0.5f);

    final AnimatedSprite face = new AnimatedSprite(pX, pY,
        mBoxFaceTextureRegion).animate(100);
    final Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, face,
        BodyType.DynamicBody, objectFixtureDef);

    scene.getLastChild().attachChild(face);
    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body,
        true, true));

    if (mFaceCount == 0) {
      mBoundChaseCamera.setChaseEntity(face);
    }

    mFaceCount++;
  }
}
