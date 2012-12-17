package org.anddev.andengine.examples.benchmark;

import org.anddev.andengine.entity.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.handler.timer.TimerHandler;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public abstract class BaseBenchmark extends BaseGameActivity {
  private static final int DIALOG_SHOW_RESULT = 1;
  private float mFPS;

  protected void showResult(final float pFPS) {
    mFPS = pFPS;
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        showDialog(DIALOG_SHOW_RESULT);
      }
    });
  }

  protected abstract float getBenchmarkDuration();
  protected abstract float getBenchmarkStartOffset();

  @Override
  public void onLoadComplete() {
    getEngine().registerPostFrameHandler(new TimerHandler(
        getBenchmarkStartOffset(), new ITimerCallback() {
          @Override
          public void onTimePassed(final TimerHandler pTimerHandler) {
            getEngine().unregisterPostFrameHandler(pTimerHandler);
            setUpBenchmarkHandling();
          }
        }));
  }

  protected void setUpBenchmarkHandling() {
    // TODO Auto-generated method stub
    final FPSCounter fpsCounter = new FPSCounter();
    getEngine().registerPreFrameHandler(fpsCounter);

    getEngine().registerPostFrameHandler(new TimerHandler(getBenchmarkDuration(),
        new ITimerCallback() {
          @Override
          public void onTimePassed(final TimerHandler pTimerHandler) {
            showResult(fpsCounter.getFPS());
          }
        }));
  }

  @Override
  protected Dialog onCreateDialog(final int pID) {
    switch (pID) {
    case DIALOG_SHOW_RESULT:
      return new AlertDialog.Builder(this)
      .setTitle(getClass().getSimpleName() + "-Results")
      .setMessage(String.format("FPS: %.2f", mFPS))
      .setPositiveButton(android.R.string.ok, new OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
          finish();
        }
      })
      .create();
    default:
      return onCreateDialog(pID);
    }
  }
}
