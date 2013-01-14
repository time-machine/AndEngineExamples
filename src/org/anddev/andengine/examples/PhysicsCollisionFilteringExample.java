package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
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
import org.anddev.andengine.util.Debug;

import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class PhysicsCollisionFilteringExample extends BaseExample implements
    IAccelerometerListener, IOnSceneTouchListener {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  // the categories
  public static final short CATEGORYBIT_WALL = 1;
  public static final short CATEGORYBIT_BOX = 2;
  public static final short CATEGORYBIT_CIRCLE = 4;

  // and what should collide with what
  public static final short MASKBITS_WALL = CATEGORYBIT_WALL + CATEGORYBIT_BOX +
      CATEGORYBIT_CIRCLE;
  public static final short MASKBITS_BOX = CATEGORYBIT_WALL + CATEGORYBIT_BOX;
  public static final short MASKBITS_CIRCLE = CATEGORYBIT_WALL +
      CATEGORYBIT_CIRCLE;

  public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory
      .createFixtureDef(0, 0.5f, 0.5f, false, CATEGORYBIT_WALL, MASKBITS_WALL,
          (short)0);
  public static final FixtureDef BOX_FIXTURE_DEF = PhysicsFactory
      .createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_BOX, MASKBITS_BOX,
          (short)0);
  public static final FixtureDef CIRCLE_FIXTURE_DEF = PhysicsFactory
      .createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_CIRCLE,
          MASKBITS_CIRCLE, (short)0);

  private Texture mTexture;

  private TiledTextureRegion mBoxFaceTextureRegion;
  private TiledTextureRegion mCircleFaceTextureRegion;

  private PhysicsWorld mPhysicsWorld;

  private int mFaceCount = 0;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG)
        .show();
    Toast.makeText(this, "Boxes will only collide with boxes.\n" +
        "Circles will only collide with circles.", Toast.LENGTH_LONG).show();
    final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera));
  }

  @Override
  public void onLoadResources() {
    // textures
    mTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    TextureRegionFactory.setAssetBasePath("gfx/");

    // texture regions
    mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "face_box_tiled.png", 0, 0, 2, 1);
    mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "face_circle_tiled.png", 0, 32, 2, 1);
    mEngine.getTextureManager().loadTexture(mTexture);
    enableAccelerometerSensor(this);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(2);
    scene.setBackground(new ColorBackground(0, 0, 0));
    scene.setOnSceneTouchListener(this);

    mPhysicsWorld = new PhysicsWorld(new Vector2(0,
        SensorManager.GRAVITY_EARTH), false);

    final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
    final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
    final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
    final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);


    PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyType.StaticBody,
        WALL_FIXTURE_DEF);
    PhysicsFactory.createBoxBody(mPhysicsWorld, roof, BodyType.StaticBody,
        WALL_FIXTURE_DEF);
    PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyType.StaticBody,
        WALL_FIXTURE_DEF);
    PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyType.StaticBody,
        WALL_FIXTURE_DEF);

    scene.getBottomLayer().addEntity(ground);
    scene.getBottomLayer().addEntity(roof);
    scene.getBottomLayer().addEntity(left);
    scene.getBottomLayer().addEntity(right);

    scene.registerUpdateHandler(mPhysicsWorld);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
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
    mPhysicsWorld.setGravity(new Vector2(pAccelerometerData.getY(),
        pAccelerometerData.getX()));
  }

  private void addFace(final float pX, final float pY) {
    final Scene scene = mEngine.getScene();

    mFaceCount++;
    Debug.d("Faces: " + mFaceCount);

    AnimatedSprite face;
    Body body;

    if (mFaceCount % 2 == 0) {
      face = new AnimatedSprite(pX, pY, mBoxFaceTextureRegion);
      body = PhysicsFactory.createBoxBody(mPhysicsWorld, face,
          BodyType.DynamicBody, BOX_FIXTURE_DEF);
    }
    else {
      face = new AnimatedSprite(pX, pY, mCircleFaceTextureRegion);
      body = PhysicsFactory.createCircleBody(mPhysicsWorld, face,
          BodyType.DynamicBody, CIRCLE_FIXTURE_DEF);
    }

    face.animate(200);
    face.setUpdatePhysics(false);

    scene.getTopLayer().addEntity(face);
    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body,
        true, true, false, false));
  }
}
