package org.anddev.andengine.examples.game.snake;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.SmoothCamera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.examples.game.snake.adt.Direction;
import org.anddev.andengine.examples.game.snake.entity.Snake;
import org.anddev.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class SnakeGameActivity extends BaseGameActivity implements
    SnakeConstants {
  private static final int CAMERA_WIDTH = 20 * CELL_WIDTH;
  private static final int CAMERA_HEIGHT = 20 * CELL_HEIGHT;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mTailPartTextureRegion;


  @Override
  public Engine onLoadEngine() {
    mCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, CELL_WIDTH,
        CELL_HEIGHT, 1);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
    mTailPartTextureRegion = TextureRegionFactory.createFromAsset(mTexture,
        this, "gfx/boxface.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    final Snake snake = new Snake(Direction.RIGHT, 5, 5, mTailPartTextureRegion,
        mTailPartTextureRegion);
    scene.getTopLayer().addEntity(snake);

    scene.registerPreFrameHandler(new TimerHandler(1, new ITimerCallback() {
      @Override
      public void onTimePassed(final TimerHandler pTimerHandler) {
        pTimerHandler.reset();
        // TODO check if move is possible
        snake.move();
      }
    }));

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
