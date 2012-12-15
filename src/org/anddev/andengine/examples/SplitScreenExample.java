package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.SingleSceneSplitScreenEngine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.ChaseCamera;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.SplitScreenEngineOptions;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.primitives.Rectangle;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.extension.physics.box2d.Box2DPhysicsSpace;
import org.anddev.andengine.extension.physics.box2d.adt.DynamicPhysicsBody;
import org.anddev.andengine.extension.physics.box2d.adt.PhysicsShape;
import org.anddev.andengine.extension.physics.box2d.adt.StaticPhysicsBody;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.widget.Toast;

public class SplitScreenExample extends BaseExampleGameActivity implements
IAccelerometerListener, IOnSceneTouchListener {
  private static final int CAMERA_WIDTH = 400;
  private static final int CAMERA_HEIGHT = 480;

  private Texture mTexture;
  private TiledTextureRegion mBoxFaceTextureRegion;

  private Box2DPhysicsSpace mPhysicsSpace;
  private int mFaceCount;
  private ChaseCamera mChaseCamera;

  @Override
  public Scene onLoadScene() {
    mPhysicsSpace = new Box2DPhysicsSpace();
    mPhysicsSpace.createWorld(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    mPhysicsSpace.setGravity(0, 2 * SensorManager.GRAVITY_EARTH);

    final Scene scene = new Scene(2);
    scene.setBackgroundColor(0, 0, 0);
    scene.setOnSceneTouchListener(this);

    getEngine().registerPostFrameHandler(new FPSCounter());

    final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 1, CAMERA_WIDTH, 1);
    scene.getLayer(0).addEntity(ground);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(ground, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
    scene.getLayer(0).addEntity(roof);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(roof, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle left = new Rectangle(0, 0, 1, CAMERA_HEIGHT);
    scene.getLayer(0).addEntity(left);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(left, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle right = new Rectangle(CAMERA_WIDTH - 1, 0, 1, CAMERA_HEIGHT);
    scene.getLayer(0).addEntity(right);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(right, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    getEngine().registerPreFrameHandler(mPhysicsSpace);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32);
    mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/boxface_tiled.png", 0, 0, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
    enableAccelerometerSensor(this);
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to add boxes.", Toast.LENGTH_LONG).show();
    final Camera firstCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    final ChaseCamera secondCamera = new ChaseCamera(0, 0, CAMERA_WIDTH / 2,
        CAMERA_HEIGHT / 2, null);
    mChaseCamera = secondCamera;
    return new SingleSceneSplitScreenEngine(new SplitScreenEngineOptions(true,
        ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH * 2, CAMERA_HEIGHT), firstCamera,
        secondCamera, false));
  }

  @Override
  public boolean onSceneTouchEvent(final Scene pScene,
      final MotionEvent pSceneMotionEvent) {
    if (mPhysicsSpace != null) {
      if (pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        addFace(pSceneMotionEvent.getX(), pSceneMotionEvent.getY());
        return true;
      }
    }
    return false;
  }

  @Override
  public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
    mPhysicsSpace.setGravity(4 * pAccelerometerData.getY(),
        4 * pAccelerometerData.getX());
  }

  private void addFace(final float pX, final float pY) {
    final Scene scene = getEngine().getScene();
    final AnimatedSprite face = new AnimatedSprite(pX, pY, mBoxFaceTextureRegion);
    face.animate(new long[]{100, 100}, 0, 1, true);
    scene.getLayer(1).addEntity(face);
    mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE, false));

    if (mFaceCount == 0) {
      mChaseCamera.setChaseEntity(face);
    }
    mFaceCount++;
  }
}
