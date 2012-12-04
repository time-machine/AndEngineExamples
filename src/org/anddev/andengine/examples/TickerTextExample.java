package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelModifier;
import org.anddev.andengine.entity.shape.modifier.RotateModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceModifier;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.text.Text.HorizontalAlign;
import org.anddev.andengine.entity.text.TickerText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;

import android.graphics.Color;
import android.graphics.Typeface;

public class TickerTextExample extends BaseExampleGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mFontTexture;
  private Font mFont;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final Text text = new TickerText(30, 60, mFont,
        "There are also ticker texts!\n\nYou'll see the answer to life & " +
            "universe in...\n\n5 4 3 2 1...\n\n42\n\nIndeed very funny!",
            HorizontalAlign.CENTER, 5);
    text.addShapeModifier(new SequenceModifier(
        new ParallelModifier(
            new AlphaModifier(20, 0, 1), new ScaleModifier(20, 0.5f, 1)
            ),
            new RotateModifier(6, 0, 360)
        )
        );

    scene.getTopLayer().addEntity(text);

    return scene;
  }

  @Override
  public void onLoadResources() {
    mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
    mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
        Typeface.BOLD), 32, true, Color.RED);
    getEngine().getTextureManager().loadTexture(mFontTexture);
    getEngine().getFontManager().loadFont(mFont);
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
