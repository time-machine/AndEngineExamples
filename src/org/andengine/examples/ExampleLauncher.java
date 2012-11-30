package org.andengine.examples;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExampleLauncher extends ListActivity {
  final Example[] EXAMPLES = Example.values();

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // create an ArrayAdapter, that will actually make the Strings above appear
    // in the ListView
    setListAdapter(new ArrayAdapter<Example>(this,
        android.R.layout.simple_list_item_1, EXAMPLES));
  }

  @Override
  protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
    super.onListItemClick(l, v, position, id);
    startActivity(new Intent(this, EXAMPLES[position].CLASS));
  }

  private static enum Example {
    PAUSE(PauseExample.class, "Pause Example"),
    MENU(MenuExample.class, "Menu Example"),
    SUBMENU(SubMenuExample.class, "SubMenu Example");
    //FONT(FontExample.class, "Font Example");

    public final Class<? extends Activity> CLASS;
    public final String NAME;

    private Example(final Class<? extends Activity> pExampleClass,
        final String pExampleName) {
      CLASS = pExampleClass;
      NAME = pExampleName;
    }

    @Override
    public String toString() {
      return NAME;
    }
  }
}
