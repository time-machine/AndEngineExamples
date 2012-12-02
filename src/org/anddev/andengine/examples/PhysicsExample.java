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
import org.anddev.andengine.input.touch.IOnSceneTouchListener;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.physics.DynamicPhysicsBody;
import org.anddev.andengine.physics.IPhysicsSpace;
import org.anddev.andengine.physics.PhysicsShape;
import org.anddev.andengine.physics.StaticPhysicsBody;
import org.anddev.andengine.physics.box2d.Box2DPhysicsSpace;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.hardware.SensorManager;
import android.view.MotionEvent;

public class PhysicsExample extends BaseGameActivity implements
IAccelerometerListener, IOnSceneTouchListener {
  private static final int GAME_WIDTH = 720;
  private static final int GAME_HEIGHT = 480;

  private Texture mTexture;
  private TiledTextureRegion mBoxFaceTextureRegion;
  private IPhysicsSpace mPhysicsSpace;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPostFrameHandler(new FPSCounter());
    //
    mPhysicsSpace = new Box2DPhysicsSpace();
    mPhysicsSpace.createWorld(0, 0, GAME_WIDTH, GAME_HEIGHT);
    mPhysicsSpace.setGravity(0, 2 * SensorManager.GRAVITY_EARTH);

    final Scene scene = new Scene(2);
    scene.setBackgroundColor(0, 0, 0);
    scene.setOnSceneTouchListener(this);

    final Rectangle ground = new Rectangle(0, GAME_HEIGHT - 1, GAME_WIDTH, 1);
    scene.getLayer(0).addEntity(ground);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(ground, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle roof = new Rectangle(0, 0, GAME_WIDTH, 2);
    scene.getLayer(0).addEntity(roof);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(roof, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle left = new Rectangle(0, 0, 1, GAME_HEIGHT);
    scene.getLayer(0).addEntity(left);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(left, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    final Rectangle right = new Rectangle(GAME_WIDTH - 1, 0, 1, GAME_HEIGHT);
    scene.getLayer(0).addEntity(right);
    mPhysicsSpace.addStaticBody(new StaticPhysicsBody(right, 0, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE));

    scene.registerPreFrameHandler(mPhysicsSpace);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32);
    mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(mTexture,
        this, "gfx/boxface_tiled.png", 0, 0, 2, 1);
    TextureManager.loadTexture(mTexture);
    enableAccelerometer(this);
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public Engine onLoadEngine() {
    final Camera camera = new Camera(0, 0, GAME_WIDTH, GAME_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(GAME_WIDTH, GAME_HEIGHT), camera));
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
    final AnimatedSprite face = new AnimatedSprite(pX, pY,
        mBoxFaceTextureRegion);
    face.animate(new long[] { 100, 100 }, 0, 1, true);
    scene.getLayer(1).addEntity(face);
    mPhysicsSpace.addDynamicBody(new DynamicPhysicsBody(face, 1, 0.5f, 0.5f,
        PhysicsShape.RECTANGLE, false));
  }
}
