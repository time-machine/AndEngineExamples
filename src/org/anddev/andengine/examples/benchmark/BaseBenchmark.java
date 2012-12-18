package org.anddev.andengine.examples.benchmark;

import java.util.Random;

import org.anddev.andengine.entity.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.handler.timer.TimerHandler;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public abstract class BaseBenchmark extends BaseGameActivity {
  private static final long RANDOM_SEED = 1234567890;
  private static final int DIALOG_SHOW_RESULT = 1;
  private float mFPS;
  protected final Random mRandom = new Random(RANDOM_SEED);

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
      .setTitle(getClass().getSimpleName())
      .setMessage(String.format("Result: %.2f FPS", mFPS))
      .setPositiveButton("Submit (Please!)", new OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
          sendResultMail();
          finish();
        }
      })
      .setNegativeButton(android.R.string.cancel, new OnClickListener() {
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

  private void sendResultMail() {
    final StringBuilder mailBodyBuilder = new StringBuilder();
    mailBodyBuilder
    .append("------ AndEngine Info ------\n")
    .append("AndEngineExamples (").append(getVersionName(this)).append(" | v.")
    .append(getVersionCode(this)).append(")").append("\n\n")
    .append("Example: ").append(this.getClass().getSimpleName()).append("\n")
    .append("FPS: ").append(mFPS).append("\n\n")
    .append("VBO-Extension: ").append(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS).append("\n")
    .append("DrawTexture-Extension: ").append(GLHelper.EXTENSIONS_DRAWTEXTURE).append("\n\n")
    .append("------ Device Info ------\n")
    .append("Model: ").append(Build.MODEL).append("\n")
    .append("Android-Version: ").append(Build.VERSION.RELEASE).append("\n")
    .append("SDK: ").append(Build.VERSION.SDK).append("\n")
    .append("Manufacturer: ").append(Build.MANUFACTURER).append("\n")
    .append("Brand: ").append(Build.BRAND).append("\n")
    .append("Build-ID: ").append(Build.ID).append("\n")
    .append("Build: ").append(Build.DISPLAY).append("\n")
    .append("Device: ").append(Build.DEVICE).append("\n")
    .append("Product: ").append(Build.PRODUCT).append("\n")
    .append("CPU-ABI: ").append(Build.CPU_ABI).append("\n")
    .append("Board: ").append(Build.BOARD).append("\n")
    .append("Fingerprint: ").append(Build.FINGERPRINT);

    final String mailBody = mailBodyBuilder.toString();

    // pseudo-checksum that makes faking this a little harder
    final int checksum = mailBody.hashCode();

    sendMail(this, mailBody + "\n\nChecksum:" + checksum, "AndEngine Benchmark: " +
    getClass().getSimpleName(), "benchmark@andengine.org");
  }

  private static void sendMail(final Context pCtx, final String pBody, final String pSubject,
      final String ... pReceivers) {
    final Intent mailIntent = new Intent(android.content.Intent.ACTION_SEND);
    mailIntent.setType("plain/text");

    if (pReceivers != null && pReceivers.length > 0) {
      mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, pReceivers);
    }

    if (pSubject != null) {
      mailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, pSubject);
    }

    if (pBody != null) {
      mailIntent.putExtra(android.content.Intent.EXTRA_TEXT, pBody);
    }

    pCtx.startActivity(Intent.createChooser(mailIntent, "Select Mail Client"));
  }

  public static String getVersionName(final Context ctx) {
    try {
      final PackageInfo pi = ctx.getPackageManager().getPackageInfo(
          ctx.getPackageName(), 0);
      return pi.versionName;
    }
    catch (final PackageManager.NameNotFoundException e) {
      Debug.e("Package name not found", e);
      return "?";
    }
  }

  public static int getVersionCode(final Context ctx) {
    try {
      final PackageInfo pi = ctx.getPackageManager().getPackageInfo(
          ctx.getPackageName(), 0);
      return pi.versionCode;
    }
    catch (final PackageManager.NameNotFoundException e) {
      Debug.e("Package name not found", e);
      return -1;
    }
  }
}
