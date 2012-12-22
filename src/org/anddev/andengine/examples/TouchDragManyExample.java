package org.anddev.andengine.examples;

import java.util.HashMap;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.examples.adt.card.Card;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.MotionEvent;
import android.widget.Toast;

public class TouchDragManyExample extends BaseGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mCardDeckTexture;

  protected Sprite mSelectedSprite;

  private HashMap<Card, TextureRegion> mCardToTextureRegionMap;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "This is NOT meant to be MultiTouch!",
        Toast.LENGTH_LONG).show();
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mCardDeckTexture = new Texture(1024, 512, TextureOptions.BILINEAR);
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

    getEngine().getTextureManager().loadTexture(mCardDeckTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setOnAreaTouchTraversalFrontToBack();

    addCard(scene, Card.CLUB_ACE, 200, 100);
    addCard(scene, Card.HEART_ACE, 200, 260);
    addCard(scene, Card.DIAMOND_ACE, 440, 100);
    addCard(scene, Card.SPADE_ACE, 440, 260);

    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
      @Override
      public boolean onSceneTouchEvent(final Scene pScene,
          final MotionEvent pSceneMotionEvent) {
        return updateSelectedCardPosition(pSceneMotionEvent);
      }
    });

    scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
      @Override
      public boolean onAreaTouched(final ITouchArea pTouchArea,
          final MotionEvent pSceneMotionEvent) {
        if (pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
          mSelectedSprite = (Sprite)pTouchArea;
          mSelectedSprite.setScale(1.2f);
          return true;
        }
        else {
          return updateSelectedCardPosition(pSceneMotionEvent);
        }
      }
    });

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  protected boolean updateSelectedCardPosition(final MotionEvent pSceneMotionEvent) {
    if (mSelectedSprite != null) {
      if (pSceneMotionEvent.getAction() == MotionEvent.ACTION_MOVE) {
        mSelectedSprite.setPosition(pSceneMotionEvent.getX() - Card.CARD_WIDTH / 2,
            pSceneMotionEvent.getY() - Card.CARD_HEIGHT / 2);
      }
      else if (pSceneMotionEvent.getAction() == MotionEvent.ACTION_UP) {
        mSelectedSprite.setScale(1.0f);
        mSelectedSprite = null;
      }
      return true;
    }
    else {
      return false;
    }
  }

  private void addCard(final Scene pScene, final Card pCard, final int pX,
      final int pY) {
    final Sprite sprite = new Sprite(pX, pY, mCardToTextureRegionMap.get(pCard));
    pScene.getTopLayer().addEntity(sprite);
    pScene.registerTouchArea(sprite);
  }
}
