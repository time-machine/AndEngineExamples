package org.anddev.andengine.examples;

import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;

public class LineExample extends BaseExample {
  // initializing the Random generator produces a comparable result over
  // different versions
  private static final long RANDOM_SEED = 1234567890;

  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private static final int LINE_COUNT = 100;

  private Camera mCamera;

  @Override
  public Scene onLoadScene() {
    getEngine().registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

    final Random random = new Random(RANDOM_SEED);
    for (int i = 0; i < LINE_COUNT; i++) {
      // top left to bottom right
//      final Line line = new Line(random.nextFloat() * CAMERA_WIDTH,
//          random.nextFloat() * CAMERA_HEIGHT, random.nextFloat() * CAMERA_WIDTH,
//          random.nextFloat() * CAMERA_HEIGHT);
      final float x1 = random.nextFloat() * CAMERA_WIDTH;
      final float x2 = random.nextFloat() * CAMERA_WIDTH;
      final float y1 = random.nextFloat() * CAMERA_HEIGHT;
      final float y2 = random.nextFloat() * CAMERA_HEIGHT;
      final float lineWidth = random.nextFloat() * 5;

      final Line line = new Line(x1, y1, x2, y2, lineWidth);
      line.setColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
      scene.getLastChild().addChild(line);
    }

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
