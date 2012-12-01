package org.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.FPSCounter;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.primitives.Line;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class LineExample extends BaseGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSCounter());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.09804f, 0.6274f, 0.8784f);

    // top left to bottom right
    final Line line = new Line(240, 320, 480, 160);
    line.setColor(1, 0, 0);
    line.setAngle(90);
    scene.getTopLayer().addEntity(line);

    return scene;
  }

  @Override
  public void onLoadResources() {
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
