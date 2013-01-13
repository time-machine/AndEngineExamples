package org.anddev.andengine.examples.launcher;

import org.anddev.andengine.examples.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

class ExpandableExampleLauncherListAdapter extends
    BaseExpandableListAdapter {
  private static final ExampleGroup[] EXAMPLEGROUPS = {
    ExampleGroup.SIMPLE,
    ExampleGroup.MODIFIER_AND_ANIMATION,
    ExampleGroup.TOUCH,
    ExampleGroup.PARTICLESYSTEM,
    ExampleGroup.MULTIPLAYER,
    ExampleGroup.PHYSICS,
    ExampleGroup.TEXT,
    ExampleGroup.AUDIO,
    ExampleGroup.ADVANCED,
    ExampleGroup.BACKGROUND,
    ExampleGroup.OTHER,
    ExampleGroup.APP,
    ExampleGroup.GAME,
    ExampleGroup.BENCHMARK
  };

  private final Context mContext;

  public ExpandableExampleLauncherListAdapter(final Context pContext) {
    mContext = pContext;
  }

  @Override
  public Example getChild(final int groupPosition, final int childPosition) {
    return EXAMPLEGROUPS[groupPosition].EXAMPLES[childPosition];
  }

  @Override
  public long getChildId(final int groupPosition, final int childPosition) {
    return childPosition;
  }

  @Override
  public View getChildView(final int groupPosition, final int childPosition,
      final boolean isLastChild, final View convertView, final ViewGroup parent) {
    View childView;
    if (convertView != null) {
      childView = convertView;
    }
    else {
      childView = LayoutInflater.from(mContext).inflate(
          R.layout.listrow_example, null);
    }

    ((TextView)childView.findViewById(R.id.tv_listrow_example_name)).setText(
        getChild(groupPosition, childPosition).NAMERESID);

    return childView;
  }

  @Override
  public int getChildrenCount(final int groupPosition) {
    return EXAMPLEGROUPS[groupPosition].EXAMPLES.length;
  }

  @Override
  public ExampleGroup getGroup(final int groupPosition) {
    return EXAMPLEGROUPS[groupPosition];
  }

  @Override
  public int getGroupCount() {
    return EXAMPLEGROUPS.length;
  }

  @Override
  public long getGroupId(final int groupPosition) {
    return groupPosition;
  }

  @Override
  public View getGroupView(final int groupPosition, final boolean isExpanded,
      final View convertView, final ViewGroup parent) {
    View groupView;
    if (convertView != null) {
      groupView = convertView;
    }
    else {
      groupView = LayoutInflater.from(mContext).inflate(
          R.layout.listrow_examplegroup, null);
    }

    ((TextView)groupView.findViewById(R.id.tv_listrow_examplegroup_name)).setText(
        getGroup(groupPosition).NAMERESID);

    return groupView;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public boolean isChildSelectable(final int groupPosition, final int childPosition) {
    return true;
  }
}
