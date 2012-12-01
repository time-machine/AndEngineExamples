package org.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.input.touch.IOnSceneTouchListener;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.MotionEvent;

public class PhysicsExample extends BaseGameActivity implements
    IAccelerometerListener, IOnSceneTouchListener {

  @Override
  public Scene onLoadScene() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onLoadResources() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onLoadComplete() {
    // TODO Auto-generated method stub

  }

  @Override
  public Engine onLoadEngine() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean onSceneTouchEvent(Scene pScene, MotionEvent pSceneMotionEvent) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
    // TODO Auto-generated method stub

  }

}
