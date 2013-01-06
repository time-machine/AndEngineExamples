package org.anddev.andengine.examples;

import static org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

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
import android.view.MotionEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class PhysicsExample extends BaseExample implements
    IAccelerometerListener, IOnSceneTouchListener {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Texture mTexture;

  private TiledTextureRegion mBoxFaceTextureRegion;
  private TiledTextureRegion mCircleFaceTextureRegion;
  private TiledTextureRegion mTriangleFaceTextureRegion;
  private TiledTextureRegion mHexagonFaceTextureRegion;

  private PhysicsWorld mPhysicsWorld;

  private int mFaceCount = 0;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to add objects.",
        Toast.LENGTH_LONG).show();
    final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 128, TextureOptions.BILINEAR);
    TextureRegionFactory.setAssetBasePath("gfx/");
    mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "face_box_tiled.png", 0, 0, 2, 1);
    mCircleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "face_circle_tiled.png", 0, 32, 2, 1);
    mTriangleFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "face_triangle_tiled.png", 0, 64, 2, 1);
    mHexagonFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
        mTexture, this, "face_hexagon_tiled.png", 0, 96, 2, 1);

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

    scene.registerUpdateHandler(mPhysicsWorld);

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
    mPhysicsWorld.setGravity(new Vector2(pAccelerometerData.getY(),
        pAccelerometerData.getX()));
  }

  private void addFace(final float pX, final float pY) {
    final Scene scene = mEngine.getScene();

    mFaceCount++;
    Debug.d("Faces: " + mFaceCount);

    final AnimatedSprite face;
    final Body body;

    final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f,
        0.5f);

    if (mFaceCount % 4 == 0) {
      face = new AnimatedSprite(pX, pY, mBoxFaceTextureRegion);
      body = PhysicsFactory.createBoxBody(mPhysicsWorld, face,
          BodyType.DynamicBody, objectFixtureDef);
    }
    else if (mFaceCount % 4 == 1) {
      face = new AnimatedSprite(pX, pY, mCircleFaceTextureRegion);
      body = PhysicsFactory.createCircleBody(mPhysicsWorld, face,
          BodyType.DynamicBody, objectFixtureDef);
    }
    else if (mFaceCount % 4 == 2) {
      face = new AnimatedSprite(pX, pY, mTriangleFaceTextureRegion);
      body = createTriangleBody(mPhysicsWorld, face, BodyType.DynamicBody,
          objectFixtureDef);
    }
    else {
      face = new AnimatedSprite(pX, pY, mHexagonFaceTextureRegion);
      body = createHexagonBody(mPhysicsWorld, face,
          BodyType.DynamicBody, objectFixtureDef);
    }

    face.animate(200);
    face.setUpdatePhysics(false);

    scene.getTopLayer().addEntity(face);
    mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body,
        true, true, false, false));
  }

  /**
   * Creates a {@link Body} based {@link PolygonShape} in the form of a triangle:
   * <pre>
   *  /\
   * /__\
   * </pre>
   */
  public static Body createTriangleBody(final PhysicsWorld pPhysicsWorld,
      final Shape pShape, final BodyType pBodyType, final FixtureDef pFixtureDef) {
    // remember that the vertices are relative to the center-coordinates of the
    // Shape
    final float halfWidth = pShape.getWidthScaled() * 0.5f /
        PIXEL_TO_METER_RATIO_DEFAULT;
    final float halfHeight = pShape.getHeightScaled() * 0.5f /
        PIXEL_TO_METER_RATIO_DEFAULT;

    final float top = -halfHeight;
    final float bottom = halfHeight;
    final float left = -halfWidth;
    final float centerX = 0;
    final float right = halfWidth;

    final Vector2[] vertices = {
        new Vector2(centerX, top),
        new Vector2(right, bottom),
        new Vector2(left, bottom)
    };

    return PhysicsFactory.createPolygonBody(pPhysicsWorld, pShape, vertices,
        pBodyType, pFixtureDef);
  }

  /**
   * Creates a {@link Body} based {@link PolygonShape} in the form of a hexagon:
   * <pre>
   *  /\
   * /  \
   * |  |
   * |  |
   * \  /
   *  \/
   * </pre>
   */
  public static Body createHexagonBody(final PhysicsWorld pPhysicsWorld,
      final Shape pShape, final BodyType pBodyType, final FixtureDef pFixtureDef) {
    // remember that the vertices are relative to the center-coordinates of the
    // Shape
    final float halfWidth = pShape.getWidthScaled() * 0.5f /
        PIXEL_TO_METER_RATIO_DEFAULT;
    final float halfHeight = pShape.getHeightScaled() * 0.5f /
        PIXEL_TO_METER_RATIO_DEFAULT;

    // the top and bottom vertex of the hexagon are on the bottom and top of the
    // hexagon-sprite
    final float top = -halfHeight;
    final float bottom = halfHeight;

    final float centerX = 0;

    // the left and right vertices of the hexagon are not on the edge of the
    // hexagon-sprite, so we need to inset them a little
    final float left = -halfWidth + 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
    final float right = halfWidth - 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
    final float higher = top + 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;
    final float lower = bottom - 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;


    final Vector2[] vertices = {
        new Vector2(centerX, top),
        new Vector2(right, higher),
        new Vector2(right, lower),
        new Vector2(centerX, bottom),
        new Vector2(left, lower),
        new Vector2(left, higher)
    };

    return PhysicsFactory.createPolygonBody(pPhysicsWorld, pShape, vertices,
        pBodyType, pFixtureDef);
  }
}