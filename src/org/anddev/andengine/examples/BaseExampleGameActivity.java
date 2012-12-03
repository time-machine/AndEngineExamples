package org.anddev.andengine.examples;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.Menu;
import android.view.MenuItem;


public abstract class BaseExampleGameActivity extends BaseGameActivity {
  private static final int MENU_TRACE = Menu.FIRST;

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    menu.add(Menu.NONE, MENU_TRACE, Menu.NONE, "Start Method Tracing");
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    menu.findItem(MENU_TRACE).setTitle(getEngine().isMethodTracing() ?
        "Stop Method Tracing" : "Start Method Tracing");
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
    switch (item.getItemId()) {
    case MENU_TRACE:
      if (getEngine().isMethodTracing()) {
        getEngine().stopMethodTracing();
      }
      else {
        getEngine().startMethodTracing("AndEngin_" +
            System.currentTimeMillis() + ".trace");
      }
      return true;
    default:
      return super.onMenuItemSelected(featureId, item);
    }
  }
}
