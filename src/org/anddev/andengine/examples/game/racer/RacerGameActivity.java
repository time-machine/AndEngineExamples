package org.anddev.andengine.examples.game.racer;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.physics.box2d.Body;

public class RacerGameActivity extends BaseGameActivity {
  private static final int CAMERA_WIDTH = 768;
  private static final int CAMERA_HEIGHT = 384;

  private static final int INSET = 128;

  private static final int LAYER_RACERTRACK = 0;
  private static final int LAYER_BORDERSS = LAYER_RACERTRACK + 1;
  private static final int LAYER_CARS = LAYER_BORDERSS + 1;

  private Camera mCamera;

  private PhysicsWorld mPhysicsWorld;

  private Texture mVehiclesTexture;
  private TiledTextureRegion mVehiclesTextureRegion;

  private Texture mRacetrackTexture;
  private TextureRegion mRacetrackStraightTextureRegion;
  private TextureRegion mRacetrackCurveTextureRegion;

  private Texture mOnScreenControlTexture;
  private TextureRegion mOnScreenControlBaseTextureRegion;
  private TextureRegion mOnScreenControlKnobTextureRegion;

  private Body mCarBody;
  private TiledSprite mCar;

  @Override
  public Engine onLoadEngine() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onLoadResources() {
    // TODO Auto-generated method stub

  }

  @Override
  public Scene onLoadScene() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onLoadComplete() {
    // TODO Auto-generated method stub

  }

}
