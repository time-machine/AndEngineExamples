package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.modifier.ease.EaseBackIn;
import org.anddev.andengine.util.modifier.ease.EaseBackInOut;
import org.anddev.andengine.util.modifier.ease.EaseBackOut;
import org.anddev.andengine.util.modifier.ease.EaseBounceIn;
import org.anddev.andengine.util.modifier.ease.EaseBounceInOut;
import org.anddev.andengine.util.modifier.ease.EaseCircularIn;
import org.anddev.andengine.util.modifier.ease.EaseCircularInOut;
import org.anddev.andengine.util.modifier.ease.EaseCircularOut;
import org.anddev.andengine.util.modifier.ease.EaseCubicIn;
import org.anddev.andengine.util.modifier.ease.EaseCubicInOut;
import org.anddev.andengine.util.modifier.ease.EaseCubicOut;
import org.anddev.andengine.util.modifier.ease.EaseElasticIn;
import org.anddev.andengine.util.modifier.ease.EaseElasticInOut;
import org.anddev.andengine.util.modifier.ease.EaseElasticOut;
import org.anddev.andengine.util.modifier.ease.EaseExponentialIn;
import org.anddev.andengine.util.modifier.ease.EaseExponentialInOut;
import org.anddev.andengine.util.modifier.ease.EaseExponentialOut;
import org.anddev.andengine.util.modifier.ease.EaseLinear;
import org.anddev.andengine.util.modifier.ease.EaseQuadIn;
import org.anddev.andengine.util.modifier.ease.EaseQuadInOut;
import org.anddev.andengine.util.modifier.ease.EaseQuadOut;
import org.anddev.andengine.util.modifier.ease.EaseQuartIn;
import org.anddev.andengine.util.modifier.ease.EaseQuartInOut;
import org.anddev.andengine.util.modifier.ease.EaseQuartOut;
import org.anddev.andengine.util.modifier.ease.EaseQuintIn;
import org.anddev.andengine.util.modifier.ease.EaseQuintInOut;
import org.anddev.andengine.util.modifier.ease.EaseQuintOut;
import org.anddev.andengine.util.modifier.ease.EaseSineIn;
import org.anddev.andengine.util.modifier.ease.EaseSineInOut;
import org.anddev.andengine.util.modifier.ease.EaseSineOut;
import org.anddev.andengine.util.modifier.ease.EaseStrongIn;
import org.anddev.andengine.util.modifier.ease.EaseStrongInOut;
import org.anddev.andengine.util.modifier.ease.EaseStrongOut;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

import android.graphics.Color;
import android.graphics.Typeface;

public class EaseFunctionExample extends BaseExample {
  private static int CAMERA_WIDTH = 720;
  private static int CAMERA_HEIGHT = 480;

  private Camera mCamera;

  private Texture mFontTexture;
  private Font mFont;

  private Texture mTexture;
  private TextureRegion mFaceTextureRegion;
  private TextureRegion mNextTextureRegion;

  private static final IEaseFunction[][] EASEFUNCTIONS = new IEaseFunction[][] {
    new IEaseFunction[] {
        EaseLinear.getInstance(),
        EaseLinear.getInstance(),
        EaseLinear.getInstance()
    },
    new IEaseFunction[] {
        EaseBackIn.getInstance(),
        EaseBackOut.getInstance(),
        EaseBackInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseBounceIn.getInstance(),
        EaseBackOut.getInstance(),
        EaseBounceInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseCircularIn.getInstance(),
        EaseCircularOut.getInstance(),
        EaseCircularInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseCubicIn.getInstance(),
        EaseCubicOut.getInstance(),
        EaseCubicInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseElasticIn.getInstance(),
        EaseElasticOut.getInstance(),
        EaseElasticInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseExponentialIn.getInstance(),
        EaseExponentialOut.getInstance(),
        EaseExponentialInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseQuadIn.getInstance(),
        EaseQuadOut.getInstance(),
        EaseQuadInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseQuartIn.getInstance(),
        EaseQuartOut.getInstance(),
        EaseQuartInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseQuintIn.getInstance(),
        EaseQuintOut.getInstance(),
        EaseQuintInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseSineIn.getInstance(),
        EaseSineOut.getInstance(),
        EaseSineInOut.getInstance()
    },
    new IEaseFunction[] {
        EaseStrongIn.getInstance(),
        EaseStrongOut.getInstance(),
        EaseStrongInOut.getInstance()
    }
  };

  private int mCurrentEaseFunctionSet = 0;

  private final Sprite[] mFaces = new Sprite[3];
  private final ChangeableText[] mEaseFunctionNameTexts = new ChangeableText[3];

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    // font
    mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
        Typeface.BOLD), 32, true, Color.WHITE);
    mEngine.getTextureManager().loadTexture(mFontTexture);
    mEngine.getFontManager().loadFont(mFont);

    // textures
    mTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mNextTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/next.png", 0, 0);
    mFaceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/badge.png", 97, 0);
    mEngine.getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);

    final HUD hud = new HUD();

    final Sprite nextSprite = new Sprite(
        CAMERA_WIDTH - 100 - mNextTextureRegion.getWidth(), 0,
        mNextTextureRegion) {
      @Override
      public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
          final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
          next();
        }
        return true;
      }
    };

    final Sprite previouSprite = new Sprite(100, 0, mNextTextureRegion.clone()) {
      @Override
      public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
          final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
          previous();
        }
        return true;
      }
    };

    previouSprite.getTextureRegion().setFlippedHorizontal(true);

    hud.getLastChild().attachChild(nextSprite);
    hud.getLastChild().attachChild(previouSprite);

    hud.registerTouchArea(nextSprite);
    hud.registerTouchArea(previouSprite);

    mCamera.setHUD(hud);

    // create the sprites that will be moving
    mFaces[0] = new Sprite(0, CAMERA_HEIGHT - 300, mFaceTextureRegion);
    mFaces[1] = new Sprite(0, CAMERA_HEIGHT - 200, mFaceTextureRegion);
    mFaces[2] = new Sprite(0, CAMERA_HEIGHT - 100, mFaceTextureRegion);

    mEaseFunctionNameTexts[0] = new ChangeableText(0, CAMERA_HEIGHT - 250,
        mFont, "Function", 20);
    mEaseFunctionNameTexts[1] = new ChangeableText(0, CAMERA_HEIGHT - 150,
        mFont, "Function", 20);
    mEaseFunctionNameTexts[2] = new ChangeableText(0, CAMERA_HEIGHT - 50,
        mFont, "Function", 20);

    final IEntity lastChild = scene.getLastChild();
    lastChild.attachChild(mFaces[0]);
    lastChild.attachChild(mFaces[1]);
    lastChild.attachChild(mFaces[2]);
    lastChild.attachChild(mEaseFunctionNameTexts[0]);
    lastChild.attachChild(mEaseFunctionNameTexts[1]);
    lastChild.attachChild(mEaseFunctionNameTexts[2]);
    lastChild.attachChild(new Line(0, CAMERA_HEIGHT - 110, CAMERA_WIDTH,
        CAMERA_HEIGHT - 110));
    lastChild.attachChild(new Line(0, CAMERA_HEIGHT - 210, CAMERA_WIDTH,
        CAMERA_HEIGHT - 210));
    lastChild.attachChild(new Line(0, CAMERA_HEIGHT - 310, CAMERA_WIDTH,
        CAMERA_HEIGHT - 310));

    return scene;
  }

  @Override
  public void onLoadComplete() {
    reanimate();
  }

  public void next() {
    mCurrentEaseFunctionSet++;
    mCurrentEaseFunctionSet %= EASEFUNCTIONS.length;
    reanimate();
  }

  public void previous() {
    mCurrentEaseFunctionSet--;

    if (mCurrentEaseFunctionSet < 0) {
      mCurrentEaseFunctionSet += EASEFUNCTIONS.length;
    }

    reanimate();
  }

  private void reanimate() {
    runOnUpdateThread(new Runnable() {
      @Override
      public void run() {
        final IEaseFunction[] currentEaseFunctionsSet =
            EASEFUNCTIONS[mCurrentEaseFunctionSet];
        final ChangeableText[] easeFunctionNameTexts = mEaseFunctionNameTexts;
        final Sprite[] faces = mFaces;

        for (int i = 0; i < 3; i++) {
          easeFunctionNameTexts[i].setText(currentEaseFunctionsSet[i].getClass()
              .getSimpleName());
          final Sprite face = faces[i];
          face.clearEntityModifiers();
          final float y = face.getY();
          face.setPosition(0, y);
          face.registerEntityModifier(new MoveModifier(3, 0,
              CAMERA_WIDTH - face.getWidth(), y, y,
              currentEaseFunctionsSet[i]));
        }
      }
    });
  }
}
