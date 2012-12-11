package org.anddev.andengine.examples.launcher;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

public class ExampleLauncher extends ExpandableListActivity {
  private ExpandableExampleLauncherListAdapter mExpandableExampleLauncherListAdapter;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mExpandableExampleLauncherListAdapter =
        new ExpandableExampleLauncherListAdapter(this);
    setListAdapter(mExpandableExampleLauncherListAdapter);
  }

  @Override
  public boolean onChildClick(final ExpandableListView parent, final View v,
      final int groupPosition, final int childPosition, final long id) {
    final Example example = mExpandableExampleLauncherListAdapter.getChild(
        groupPosition, childPosition);
    startActivity(new Intent(this, example.CLASS));
    return super.onChildClick(parent, v, groupPosition, childPosition, id);
  }
}
