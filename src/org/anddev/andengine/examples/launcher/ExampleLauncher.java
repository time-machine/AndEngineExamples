package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;

public class ExampleLauncher extends ExpandableListActivity {
  private ExpandableExampleLauncherListAdapter mExpandableExampleLauncherListAdapter;
  private static final int DIALOG_FIRST_APP_LAUNCH = 0;

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

    if (isFirstTime("first.app.launch")) {
      showDialog(DIALOG_FIRST_APP_LAUNCH);
    }
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
}
