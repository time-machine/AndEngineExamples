package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class TextureOptionsExample extends BaseExample {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;

  private Texture mTexture;
  private Texture mTextureBilinear;
  private Texture mTextureRepeating;

  private TextureRegion mFaceTextureRegion;
  private TextureRegion mFaceTextureRegionBilinear;
  private TextureRegion mFaceTextureRegionRepeating;

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());
    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final int centerX = (CAMERA_WIDTH - mFaceTextureRegion.getWidth()) / 2;
    final int centerY = (CAMERA_HEIGHT - mFaceTextureRegion.getHeight()) / 2;

    final Sprite face = new Sprite(centerX - 160, centerY - 40,
        mFaceTextureRegion);
    face.setScale(4);

    final Sprite faceBillinear = new Sprite(centerX + 160, centerY - 40,
        mFaceTextureRegionBilinear);
    faceBillinear.setScale(4);

    // make sure sprite has the same size as mTextureRegionRepeating
    // giving the sprite twice the height shows you'd also have to change the
    // height of the TextureRegion!
    final Sprite faceRepeating = new Sprite(centerX - 160, centerY + 100,
        mFaceTextureRegionRepeating.getWidth(),
        mFaceTextureRegionRepeating.getHeight() * 2,
        mFaceTextureRegionRepeating);

    final IEntity lastChild = scene.getLastChild();
    lastChild.attachChild(face);
    lastChild.attachChild(faceBillinear);
    lastChild.attachChild(faceRepeating);

    return scene;
  }

  @Override
  public void onLoadResources() {
    TextureRegionFactory.setAssetBasePath("gfx/");

    mTexture = new Texture(32, 32, TextureOptions.DEFAULT);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "face_box.png", 0, 0);

    mTextureBilinear = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mFaceTextureRegionBilinear = TextureRegionFactory.createFromAsset(
        mTextureBilinear, this, "face_box.png", 0, 0);

    mTextureRepeating = new Texture(32, 32, TextureOptions.REPEATING);
    mFaceTextureRegionRepeating = TextureRegionFactory.createFromAsset(
        mTextureRepeating, this, "face_box.png", 0, 0);

    // the following statement causes the Texture to be printed horizontally
    // 10x on any Sprite that uses it. so we will later increase the width of
    // such a sprite by the same factor to avoid distortion
    mFaceTextureRegionRepeating.setWidth(
        10 * mFaceTextureRegionRepeating.getWidth());
    mEngine.getTextureManager().loadTextures(mTexture, mTextureBilinear,
        mTextureRepeating);
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }
}
