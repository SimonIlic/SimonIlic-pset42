package mprog.simon.simonilic_pset4;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.TASK};

    final int[] to = new int[] { R.id.id, R.id.task};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiate database helper
        dbHelper = new DatabaseHelper(this);

        // get tasks in cursor
        Cursor cursor = dbHelper.fetch();

        // create list
        createList(cursor);
    }

    public void addTask(View view) {
        TextView editText = (TextView) findViewById(R.id.editText);
        String newTask = editText.getText().toString();

        // add new task
        dbHelper.insert(newTask);
        adapter.notifyDataSetChanged();

        editText.setText("");

        // restart activity
        recreate();
    }

    public void createList(Cursor cursor) {
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.toDoList);

        // define list adapter
        adapter = new SimpleCursorAdapter(this, R.layout.to_do_list_item, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // Register ListView for context menu, implicitly defining item click listener
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
}
