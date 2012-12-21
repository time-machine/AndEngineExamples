package org.anddev.andengine.examples.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import org.anddev.andengine.entity.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.handler.timer.TimerHandler;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.examples.R;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Callback;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.StreamUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public abstract class BaseBenchmark extends BaseGameActivity {
  private static final long RANDOM_SEED = 1234567890;
  private static final int DIALOG_SHOW_RESULT = 1;

  private static final String SUBMIT_URL = "http://www.andengine.org/sys/benchmark/submit.php";

  protected static final int ANIMATIONBENCHMARK_ID = 0;
  protected static final int PARTICLESYSTEMBENCHMARK_ID = ANIMATIONBENCHMARK_ID  + 1;
  protected static final int PHYSICSBENCHMARK_ID = PARTICLESYSTEMBENCHMARK_ID + 1;
  protected static final int SHAPEMODIFIERBENCHMARK_ID = PHYSICSBENCHMARK_ID + 1;
  protected static final int SPRITEBENCHMARK_ID = SHAPEMODIFIERBENCHMARK_ID + 1;
  protected static final int TICKERTEXTBENCHMARK_ID = SPRITEBENCHMARK_ID + 1;

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

  protected abstract float getBenchmarkID();
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
          submitResults();
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

  private void submitResults() {
    doAsync(R.string.dialog_benchmark_submit_title,
        R.string.dialog_benchmark_submit_message, new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        // create a new HttpClient and Post Header
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(SUBMIT_URL);

        // add your data
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("benchmark_id",
            String.valueOf(getBenchmarkID())));
        nameValuePairs.add(new BasicNameValuePair("benchmark_versionname",
            getVersionName(BaseBenchmark.this)));
        nameValuePairs.add(new BasicNameValuePair("benchmark_versioncode",
            String.valueOf(getVersionCode(BaseBenchmark.this))));
        nameValuePairs.add(new BasicNameValuePair("benchmark_fps",
            String.valueOf(mFPS).replace(",", ".")));
        nameValuePairs.add(new BasicNameValuePair("device_model", Build.MODEL));
        nameValuePairs.add(new BasicNameValuePair("device_android_version",
            Build.VERSION.RELEASE));
        nameValuePairs.add(new BasicNameValuePair("device_sdk_version",
            String.valueOf(Build.VERSION.SDK_INT)));
        nameValuePairs.add(new BasicNameValuePair("device_manufacturer",

            Build.MANUFACTURER));
        nameValuePairs.add(new BasicNameValuePair("device_brand", Build.BRAND));
        nameValuePairs.add(new BasicNameValuePair("device_build_id", Build.ID));
        nameValuePairs.add(new BasicNameValuePair("device_build", Build.DISPLAY));
        nameValuePairs.add(new BasicNameValuePair("device_device", Build.DEVICE));
        nameValuePairs.add(new BasicNameValuePair("device_product", Build.PRODUCT));
        nameValuePairs.add(new BasicNameValuePair("device_cpuabi", Build.CPU_ABI));
        nameValuePairs.add(new BasicNameValuePair("device_board", Build.BOARD));
        nameValuePairs.add(new BasicNameValuePair("device_fingerprint",
            Build.FINGERPRINT));
        nameValuePairs.add(new BasicNameValuePair("benchmark_extension_vbo",
            GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS ? "1" : "0"));
        nameValuePairs.add(new BasicNameValuePair("benchmark_extension_drawtexture",
            GLHelper.EXTENSIONS_DRAWTEXTURE ? "1" : "0"));
        final TelephonyManager telephonyManager =
            (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        nameValuePairs.add(new BasicNameValuePair("device_imei",
            telephonyManager.getDeviceId()));

        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // execute HTTP post request
        final HttpResponse response = httpClient.execute(httpPost);
        final int statusCode = response.getStatusLine().getStatusCode();
        Debug.d(StreamUtils.readFully(response.getEntity().getContent()));

        if (statusCode == HttpStatus.SC_OK) {
          return true;
        }
        else {
          throw new RuntimeException();
        }
      }
    }, new Callback<Boolean>() {
      @Override
      public void onCallback(final Boolean pCallbackValue) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(BaseBenchmark.this, "Success",
                Toast.LENGTH_LONG).show();
            finish();
          }
        });
      }
    }, new Callback<Exception>() {
      @Override
      public void onCallback(final Exception pException) {
        Debug.e(pException);
        Toast.makeText(BaseBenchmark.this, "Exception occurred: " +
            pException.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        finish();
      }
    });
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
