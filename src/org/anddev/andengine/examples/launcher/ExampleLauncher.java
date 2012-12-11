package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;

public class ExampleLauncher extends ExpandableListActivity {
  private ExpandableExampleLauncherListAdapter mExpandableExampleLauncherListAdapter;

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
