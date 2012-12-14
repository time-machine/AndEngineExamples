package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.primitives.Rectangle;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.Box2DPhysicsSpace;
import org.anddev.andengine.extension.physics.box2d.adt.DynamicPhysicsBody;
import org.anddev.andengine.extension.physics.box2d.adt.PhysicsShape;
import org.anddev.andengine.extension.physics.box2d.adt.StaticPhysicsBody;
import org.anddev.andengine.input.touch.IOnSceneTouchListener;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.widget.Toast;

public class PhysicsExample extends BaseExampleGameActivity implements
IAccelerometerListener, IOnSceneTouchListener {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Texture mTexture;
  private TiledTextureRegion mBoxFaceTextureRegion;
  private TiledTextureRegion mCircleFaceTextureRegion;

  private Box2DPhysicsSpace mPhysicsSpace;
  private int mFaceCount = 0;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPostFrameHandler(new FPSCounter());

    mPhysicsSpace = new Box2DPhysicsSpace();
    mPhysicsSpace.createWorld(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    mPhysicsSpace.setGravity(0, 2 * SensorManager.GRAVITY_EARTH);

    final Scene scene = new Scene(2);
    scene.setBackgroundColor(0, 0, 0);
    scene.setOnSceneTouchListener(this);

    final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 1, CAMERA_WIDTH, 1);
    scene.getBottomLayer().addEntity(ground);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(ground, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
    scene.getBottomLayer().addEntity(roof);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(roof, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle left = new Rectangle(0, 0, 1, CAMERA_HEIGHT);
    scene.getBottomLayer().addEntity(left);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(left, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle right = new Rectangle(CAMERA_WIDTH - 1, 0, 1, CAMERA_HEIGHT);
    scene.getBottomLayer().addEntity(right);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(right, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    scene.registerPreFrameHandler(mPhysicsSpace);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32);
    TextureRegionFactory.setAssetBasePath("gfx/");
    mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "boxface_tiled.png", 0, 0, 2, 1);
    mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "circleface_tiled.png", 0, 32, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
    enableAccelerometerSensor(this);
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG)
        .show();
    final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera, false));
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
    mFaceCount++;
    final AnimatedSprite face;

    if (mFaceCount % 2 == 1) {
      face = new AnimatedSprite(pX, pY, mBoxFaceTextureRegion);
      mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f,
          PhysicsShape.RECTANGLE, false));
    }
    else {
      face = new AnimatedSprite(pX, pY, mCircleFaceTextureRegion);
      mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f,
          PhysicsShape.CIRCLE, false));
    }

    final Scene scene = getEngine().getScene();
    face.animate(new long[] { 200, 200 }, 0, 1, true);
    scene.getTopLayer().addEntity(face);
  }
}
