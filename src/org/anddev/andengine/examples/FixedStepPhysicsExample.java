package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

public class FixedStepPhysicsExample extends BaseExample implements
    IAccelerometerListener, IOnSceneTouchListener {

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

  @Override
  public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
    // TODO Auto-generated method stub

  }

}
