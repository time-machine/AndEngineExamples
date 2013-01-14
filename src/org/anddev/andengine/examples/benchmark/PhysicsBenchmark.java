package org.anddev.andengine.examples.benchmark;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class PhysicsBenchmark extends BaseBenchmark implements
    IOnSceneTouchListener {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;
  private static final int COUNT_HORIZONTAL = 17;
  private static final int COUNT_VERTICAL = 15;

  private Texture mTexture;
  private TiledTextureRegion mBoxFaceTextureRegion;
  private TiledTextureRegion mCircleFaceTextureRegion;

  private PhysicsWorld mPhysicsWorld;
  private int mFaceCount = 0;

  @Override
  public Engine onLoadEngine() {
    final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(32, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    TextureRegionFactory.setAssetBasePath("gfx/");
    mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "face_box_tiled.png", 0, 0, 2, 1);
    mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "face_circle_tiled.png", 0, 32, 2, 1);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    final Scene scene = new Scene(2, true, 4,
        (COUNT_VERTICAL - 1) * (COUNT_HORIZONTAL - 1));
    scene.setBackground(new ColorBackground(0, 0, 0));
    scene.setOnSceneTouchListener(this);

    mPhysicsWorld = new PhysicsWorld(new Vector2(0,
        2 * SensorManager.GRAVITY_EARTH /
            PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT), false);

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

    for (int x = 1; x < COUNT_HORIZONTAL; x++) {
      for (int y = 1; y < COUNT_VERTICAL; y++) {
        final float pX = (((float)CAMERA_WIDTH) / COUNT_HORIZONTAL) * x + y;
        final float pY = (((float)CAMERA_HEIGHT) / COUNT_VERTICAL) * y;
        addFace(scene, pX - 16, pY - 16);
      }
    }

    scene.registerUpdateHandler(new TimerHandler(2, new ITimerCallback() {
      @Override
      public void onTimePassed(final TimerHandler pTimerHandler) {
        scene.unregisterUpdateHandler(pTimerHandler);
        scene.registerUpdateHandler(mPhysicsWorld);
        scene.registerUpdateHandler(new TimerHandler(10, new ITimerCallback() {
          @Override
          public void onTimePassed(final TimerHandler pTimerHandler) {
            mPhysicsWorld.setGravity(new Vector2(0,
                -SensorManager.GRAVITY_EARTH /
                    PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
          }
        }));
      }
    }));

    return scene;
  }

  @Override
  public boolean onSceneTouchEvent(final Scene pScene,
      final TouchEvent pSceneTouchEvent) {
    if (mPhysicsWorld != null) {
      if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
        addFace(pScene, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
        return true;
      }
    }
    return false;
  }

  @Override
  protected float getBenchmarkDuration() {
    return 20;
  }

  @Override
  protected float getBenchmarkStartOffset() {
    return 2;
  }

  private void addFace(final Scene pScene, final float pX, final float pY) {
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

    face.setUpdatePhysics(false);

    pScene.getTopLayer().addEntity(face);
    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body,
        true, true, false, false));
  }

  @Override
  protected float getBenchmarkID() {
    return PARTICLESYSTEMBENCHMARK_ID;
  }
}
