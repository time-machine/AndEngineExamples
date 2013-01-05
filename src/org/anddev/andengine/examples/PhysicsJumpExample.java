package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
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
import android.view.MotionEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class PhysicsJumpExample extends BaseExample implements
    IAccelerometerListener, IOnSceneTouchListener, IOnAreaTouchListener {
  private static final int CAMERA_WIDTH = 360;
  private static final int CAMERA_HEIGHT = 240;

  private Texture mTexture;

  private TiledTextureRegion mBoxFaceTextureRegion;
  private TiledTextureRegion mCircleFaceTextureRegion;

  private int mFaceCount = 0;

  private PhysicsWorld mPhysicsWorld;

  private float mGravityX;
  private float mGravityY;

  private final Vector2 mTempVector = new Vector2();

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to add objects. Touch an object to shoot it up into the air.",
        Toast.LENGTH_LONG).show();
    final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 64, TextureOptions.BILINEAR);
    TextureRegionFactory.setAssetBasePath("gfx/");
    mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "boxface_tiled.png", 0, 0, 2, 1);
    mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "circleface_tiled.png", 0, 32, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
    enableAccelerometerSensor(this);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPostFrameHandler(new FPSLogger());

    mPhysicsWorld = new PhysicsWorld(new Vector2(0,
        SensorManager.GRAVITY_EARTH), false);

    final Scene scene = new Scene(2);
    scene.setBackground(new ColorBackground(0, 0, 0));
    scene.setOnSceneTouchListener(this);

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

    scene.getBottomLayer().addEntity(ground);
    scene.getBottomLayer().addEntity(roof);
    scene.getBottomLayer().addEntity(left);
    scene.getBottomLayer().addEntity(right);

    scene.registerPreFrameHandler(mPhysicsWorld);

    scene.setOnAreaTouchListener(this);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
      final ITouchArea pTouchArea, final float pTouchAreaLocalX,
      final float pTouchAreaLocalY) {
    if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
      mPhysicsWorld.postRunnable(new Runnable() {
        @Override
        public void run() {
          final AnimatedSprite face = (AnimatedSprite)pTouchArea;
          jumpFace(face);
        }
      });
      return true;
    }
    return false;
  }

  @Override
  public boolean onSceneTouchEvent(final Scene pScene,
      final TouchEvent pSceneTouchEvent) {
    if (mPhysicsWorld != null) {
      if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
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
  public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
    mGravityX = pAccelerometerData.getY();
    mGravityY = pAccelerometerData.getX();
    mTempVector.set(mGravityX, mGravityY);
    mPhysicsWorld.setGravity(mTempVector);
  }

  private void addFace(final float pX, final float pY) {
    final Scene scene = mEngine.getScene();

    mFaceCount++;
    final AnimatedSprite face;
    final Body body;

    final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f,
        0.5f);

    if (mFaceCount % 2 == 1) {
      face = new AnimatedSprite(pX, pY, mBoxFaceTextureRegion);
      body = PhysicsFactory.createBoxBody(mPhysicsWorld, face,
          BodyType.DynamicBody, objectFixtureDef);
    }
    else {
      face = new AnimatedSprite(pX, pY, mCircleFaceTextureRegion);
      body = PhysicsFactory.createCircleBody(mPhysicsWorld, face,
          BodyType.DynamicBody, objectFixtureDef);
    }

    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body,
        true, true, false, false));

    face.animate(new long[] { 200, 200 }, 0, 1, true);
    face.setUpdatePhysics(false);
    scene.registerTouchArea(face);
    scene.getTopLayer().addEntity(face);
  }

  private void jumpFace(final AnimatedSprite face) {
    final Body faceBody = mPhysicsWorld.getPhysicsConnectorManager()
        .findBodyByShape(face);
    faceBody.setLinearVelocity(mTempVector.set(mGravityX * -50, mGravityY * -50));
  }
}
