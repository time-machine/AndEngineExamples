package org.anddev.andengine.examples.launcher;

import java.util.Arrays;

import org.anddev.andengine.examples.R;
import org.anddev.andengine.util.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;

public class ExampleLauncher extends ExpandableListActivity {
  private static final String PREF_LAST_APP_LAUNCH_VERSIONCODE_ID =
      "last.app.launch.versioncode";
  private static final int DIALOG_FIRST_APP_LAUNCH = 0;
  private static final int DIALOG_NEW_IN_THIS_VERSION = DIALOG_FIRST_APP_LAUNCH + 1;

  private ExpandableExampleLauncherListAdapter mExpandableExampleLauncherListAdapter;
  private int mVersionCodeCurrent;
  private int mVersionCodeLastLaunch;

  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.list_examples);

    mExpandableExampleLauncherListAdapter =
        new ExpandableExampleLauncherListAdapter(this);

    setListAdapter(mExpandableExampleLauncherListAdapter);

    findViewById(R.id.btn_get_involved).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(final View v) {
        ExampleLauncher.this.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.andengine.org")));
      }
    });

    final SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
    mVersionCodeCurrent = getVersionCode();
    mVersionCodeLastLaunch = prefs.getInt(PREF_LAST_APP_LAUNCH_VERSIONCODE_ID, -1);

    if (isFirstTime("first.app.launch")) {
      showDialog(DIALOG_FIRST_APP_LAUNCH);
    }
    else if (mVersionCodeLastLaunch != -1 &&
        mVersionCodeLastLaunch < mVersionCodeCurrent) {
      showDialog(DIALOG_NEW_IN_THIS_VERSION);
    }

    prefs.edit().putInt(PREF_LAST_APP_LAUNCH_VERSIONCODE_ID, mVersionCodeCurrent)
        .commit();
  }

  @Override
  @Deprecated
  protected Dialog onCreateDialog(final int id) {
    switch (id) {
    case DIALOG_FIRST_APP_LAUNCH:
      return new AlertDialog.Builder(this)
        .setTitle(R.string.dialog_first_app_launch_title)
        .setMessage(R.string.dialog_first_app_launch_message)
        .setIcon(android.R.drawable.ic_dialog_info)
        .setPositiveButton(android.R.string.ok, null)
        .create();
    case DIALOG_NEW_IN_THIS_VERSION:
      final int[] versionCodes =
          getResources().getIntArray(R.array.new_in_version_versioncode);

      final int versionDescriptionStartIndex = Math.max(0,
          Arrays.binarySearch(versionCodes, mVersionCodeLastLaunch) + 1);

      final String[] versionDescriptions =
          getResources().getStringArray(R.array.new_in_version_changes);

      final StringBuilder sb = new StringBuilder();

      for (int i = versionDescriptions.length - 1;
          i >= versionDescriptionStartIndex; i--) {
        sb.append("-------------------------\n");
        sb.append(">>> Version: " + versionCodes[i] + "\n");
        sb.append("-------------------------\n");
        sb.append(versionDescriptions[i]);

        if (i > versionDescriptionStartIndex) {
          sb.append("\n\n");
        }
      }

      return new AlertDialog.Builder(this)
        .setTitle(R.string.dialog_new_in_this_version_title)
        .setMessage(sb.toString())
        .setIcon(android.R.drawable.ic_dialog_info)
        .setPositiveButton(android.R.string.ok, null)
        .create();
    default:
      return super.onCreateDialog(id);
    }
  }

  @Override
  public boolean onChildClick(final ExpandableListView parent, final View v,
      final int groupPosition, final int childPosition, final long id) {
    final Example example = mExpandableExampleLauncherListAdapter.getChild(
        groupPosition, childPosition);
    startActivity(new Intent(this, example.CLASS));
    return super.onChildClick(parent, v, groupPosition, childPosition, id);
  }

  public boolean isFirstTime(final String pKey) {
    final SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
    if (prefs.getBoolean(pKey, true)) {
      prefs.edit().putBoolean(pKey, false).commit();
      return true;
    }
    return false;
  }

  public int getVersionCode() {
    try {
      final PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
      return pi.versionCode;
    }
    catch (final PackageManager.NameNotFoundException e) {
      Debug.e("Package name not found", e);
      return -1;
    }
  }
}
