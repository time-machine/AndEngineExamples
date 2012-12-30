package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
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
import org.anddev.andengine.util.Debug;

import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BasePhysicsJointExample extends BaseExample implements
    IAccelerometerListener, IOnSceneTouchListener {
  protected static final int CAMERA_WIDTH = 720;
  protected static final int CAMERA_HEIGHT = 480;

  private Texture mTexture;

  protected TiledTextureRegion mBoxFaceTextureRegion;
  private TiledTextureRegion mCircleFaceTextureRegion;

  protected PhysicsWorld mPhysicsWorld;

  private int mFaceCount = 0;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG)
        .show();
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
    mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "circleface_tiled.png", 0, 32, 2, 1);
    mEngine.getTextureManager().loadTexture(mTexture);
    enableAccelerometerSensor(this);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerPostFrameHandler(new FPSLogger());

    final Scene scene = new Scene(2);
    scene.setBackgroundColor(0, 0, 0);
    scene.setOnSceneTouchListener(this);

    mPhysicsWorld = new PhysicsWorld(
        new Vector2(0, 2 * SensorManager.GRAVITY_EARTH), false);

    final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
    final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
    final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
    final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

    PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyType.StaticBody);
    PhysicsFactory.createBoxBody(mPhysicsWorld, roof, BodyType.StaticBody);
    PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyType.StaticBody);
    PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyType.StaticBody);

    scene.getBottomLayer().addEntity(ground);
    scene.getBottomLayer().addEntity(roof);
    scene.getBottomLayer().addEntity(left);
    scene.getBottomLayer().addEntity(right);

    scene.registerPreFrameHandler(mPhysicsWorld);

    return scene;
  }

  @Override
  public void onLoadComplete() {
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
    mPhysicsWorld.setGravity(new Vector2(4 * pAccelerometerData.getY(),
        4 * pAccelerometerData.getX()));
  }

  private void addFace(final float pX, final float pY) {
    final Scene scene = mEngine.getScene();

    mFaceCount++;
    Debug.d("Faces:" + mFaceCount);

    final AnimatedSprite face;
    final Body body;

    if (mFaceCount % 2 == 0) {
      face = new AnimatedSprite(pX, pY, mBoxFaceTextureRegion);
      body = PhysicsFactory.createBoxBody(mPhysicsWorld, face,
          BodyType.DynamicBody);
    }
    else {
      face = new AnimatedSprite(pX, pY, mCircleFaceTextureRegion);
      body = PhysicsFactory.createCircleBody(mPhysicsWorld, face,
          BodyType.DynamicBody);
    }

    face.animate(200);
    face.setUpdatePhysics(false);

    scene.getTopLayer().addEntity(face);
    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body,
        true, true, false, false));
  }
}