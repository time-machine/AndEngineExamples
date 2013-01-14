package org.anddev.andengine.examples;

import java.util.HashMap;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.examples.adt.card.Card;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.anddev.andengine.input.touch.detector.SurfaceScrollDetector;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.widget.Toast;

public class PinchZoomExample extends BaseExample implements
    IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private ZoomCamera mZoomCamera;
  private Texture mCardDeckTexture;

  private HashMap<Card, TextureRegion> mCardToTextureRegionMap;
  private SurfaceScrollDetector mScrollDetector;
  private PinchZoomDetector mPinchZoomDetector;
  private float mPinchZoomStartedCameraZoomFactor;

  @Override
  public Engine onLoadEngine() {
    mZoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    final Engine engine = new Engine(new EngineOptions(true,
        ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mZoomCamera));

    try {
      if (MultiTouch.isSupported(this)) {
        engine.setTouchController(new MultiTouchController());
      }
      else {
        Toast.makeText(this, "Sorry your device does NOT support MultiTouch!" +
            "\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
      }
    }
    catch (final MultiTouchException e) {
      Toast.makeText(this, "Sorry your Android Version does NOT support" +
          "MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG)
          .show();
    }

    return engine;
  }

  @Override
  public void onLoadResources() {
    mCardDeckTexture = new Texture(1024, 512,
        TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    TextureRegionFactory.createFromAsset(mCardDeckTexture, this,
        "gfx/carddeck_tiled.png", 0, 0);
    mCardToTextureRegionMap = new HashMap<Card, TextureRegion>();

    // extract the TextureRegion of each card in the whole deck
    for (final Card card : Card.values()) {
      final TextureRegion cardTextureRegion = TextureRegionFactory.extractFromTexture(
          mCardDeckTexture, card.getTexturePositionX(),
          card.getTexturePositionY(), Card.CARD_WIDTH, Card.CARD_HEIGHT);
      mCardToTextureRegionMap.put(card, cardTextureRegion);
    }

    mEngine.getTextureManager().loadTexture(mCardDeckTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setOnAreaTouchTraversalFrontToBack();

    addCard(scene, Card.CLUB_ACE, 200, 100);
    addCard(scene, Card.HEART_ACE, 200, 260);
    addCard(scene, Card.DIAMOND_ACE, 440, 100);
    addCard(scene, Card.SPADE_ACE, 440, 260);

    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    mScrollDetector = new SurfaceScrollDetector(this);
    if (MultiTouch.isSupportedByAndroidVersion()) {
      try {
        mPinchZoomDetector = new PinchZoomDetector(this);
      }
      catch (final MultiTouchException e) {
        mPinchZoomDetector = null;
      }
    }
    else {
      mPinchZoomDetector = null;
    }

    scene.setOnSceneTouchListener(this);
    scene.setTouchAreaBindingEnabled(true);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  public void onScroll(final ScrollDetector pScollDetector,
      final TouchEvent pTouchEvent, final float pDistanceX,
      final float pDistanceY) {
    final float zoomFactor = mZoomCamera.getZoomFactor();
    mZoomCamera.offsetCenter(-pDistanceX / zoomFactor,
        -pDistanceY / zoomFactor);
  }

  @Override
  public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector,
      final TouchEvent pSceneTouchEvent) {
    mPinchZoomStartedCameraZoomFactor = mZoomCamera.getZoomFactor();
  }

  @Override
  public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector,
      final TouchEvent pTouchEvent, final float pZoomFactor) {
    mZoomCamera.setZoomFactor(mPinchZoomStartedCameraZoomFactor * pZoomFactor);
  }

  @Override
  public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector,
      final TouchEvent pTouchEvent, final float pZoomFactor) {
    mZoomCamera.setZoomFactor(mPinchZoomStartedCameraZoomFactor * pZoomFactor);
  }

  @Override
  public boolean onSceneTouchEvent(final Scene pScene,
      final TouchEvent pSceneTouchEvent) {
    if (mPinchZoomDetector != null) {
      if (mPinchZoomDetector.isZooming()) {
        mScrollDetector.setEnabled(false);
      }
      else {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
          mScrollDetector.setEnabled(true);
        }
        mScrollDetector.onTouchEvent(pSceneTouchEvent);
      }
    }
    else {
      mScrollDetector.onTouchEvent(pSceneTouchEvent);
    }

    return true;
  }

  private void addCard(final Scene pScene, final Card pCard, final int pX,
      final int pY) {
    final Sprite sprite = new Sprite(pX, pY,
        mCardToTextureRegionMap.get(pCard)) {
      boolean mGrabbed = false;

      @Override
      public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
          final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        switch (pSceneTouchEvent.getAction()) {
        case TouchEvent.ACTION_DOWN:
          setScale(1.25f);
          mGrabbed = true;
          break;
        case TouchEvent.ACTION_MOVE:
          if (mGrabbed) {
            setPosition(pSceneTouchEvent.getX() - Card.CARD_WIDTH / 2,
                pSceneTouchEvent.getY() - Card.CARD_HEIGHT / 2);
          }
          break;
        case TouchEvent.ACTION_UP:
          if (mGrabbed) {
            mGrabbed = false;
            setScale(1);
          }
          break;
        }
        return true;
      }
    };

    pScene.getTopLayer().addEntity(sprite);
    pScene.registerTouchArea(sprite);
  }
}
