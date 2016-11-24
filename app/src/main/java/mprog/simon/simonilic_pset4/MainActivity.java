package mprog.simon.simonilic_pset4;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    final String[] from = new String[] {DatabaseHelper._ID, DatabaseHelper.TASK, DatabaseHelper.CHECKED};

    final int[] to = new int[] {R.id.id, R.id.task, R.id.checkboxImage};

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

        // if nothing is typed in, notify user
        if (newTask.isEmpty()) {
            Toast.makeText(this, "Please enter a task.", Toast.LENGTH_SHORT).show();
            return;
        }

        // add new task
        dbHelper.insert(newTask);
        adapter.notifyDataSetChanged();

        // empty input field
        editText.setText("");

        // restart activity
        recreate();
    }

    public void createList(Cursor cursor) {
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.toDoList);

        // define list adapter
        adapter = new SimpleCursorAdapter(this, R.layout.to_do_list_item, cursor, from, to, 0);
        setAdapterViewBinder();
        adapter.notifyDataSetChanged();

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // set on click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (view.getId() == R.id.checkboxImage) {
                    ImageView imageView = (ImageView) view;
                    int checked = cursor.getInt(columnIndex);

                    if (checked == 1) {
                        imageView.setImageDrawable(getDrawable(android.R.drawable.checkbox_on_background));
                    } else {
                        Toast.makeText(MainActivity.this, "joe", Toast.LENGTH_SHORT).show();
                        imageView.setImageDrawable(getDrawable(android.R.drawable.checkbox_off_background));
                    }
                checkTask(id);
            }
        });

        // Register ListView for context menu, implicitly defining item longclick listener
        registerForContextMenu(listView);
    }

    public void setAdapterViewBinder() {
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.checkboxImage) {
                    ImageView imageView = (ImageView) view;
                    int checked = cursor.getInt(columnIndex);

                    if (checked == 1) {
                        imageView.setImageDrawable(getDrawable(android.R.drawable.checkbox_on_background));
                    } else {
                        Toast.makeText(MainActivity.this, "joe", Toast.LENGTH_SHORT).show();
                        imageView.setImageDrawable(getDrawable(android.R.drawable.checkbox_off_background));
                    }

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_edit:
                //editNote(info.id);
                return true;
            case R.id.action_delete:
                deleteTask(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteTask(long id) {
        dbHelper.delete(id);
        adapter.notifyDataSetChanged();

        recreate();
    }

    public void checkTask(long id) {



        //dbHelper.update_checked(id, )
    }
}
