package org.anddev.andengine.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;

public class PinchZoomExample extends BaseExample implements
    IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {

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
  public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
      TouchEvent pSceneTouchEvent) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
      TouchEvent pTouchEvent, float pZoomFactor) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
      TouchEvent pTouchEvent, float pZoomFactor) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onScroll(ScrollDetector pScollDetector, TouchEvent pTouchEvent,
      float pDistanceX, float pDistanceY) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
    // TODO Auto-generated method stub
    return false;
  }

}
