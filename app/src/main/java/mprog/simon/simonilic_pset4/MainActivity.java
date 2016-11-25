package mprog.simon.simonilic_pset4;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private Cursor db_cursor;

    final String[] from = new String[] {DatabaseHelper._ID, DatabaseHelper.TASK, DatabaseHelper.CHECKED};

    final int[] to = new int[] {R.id.id, R.id.task, R.id.checkboxImage};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiate database helper
        dbHelper = new DatabaseHelper(this);

        // get tasks in cursor
        db_cursor = dbHelper.fetch();

        // create list
        createList();
    }

    /** Add a task **/
    public void addTask(View view) {
        TextView editText = (TextView) findViewById(R.id.editText);
        String newTask = editText.getText().toString();

        // if nothing is typed in, notify user
        if (newTask.isEmpty()) {
            Toast.makeText(this, "Please enter a task.", Toast.LENGTH_SHORT).show();
            return;
        }

        // add new task in database
        dbHelper.insert(newTask);

        refreshListView();

        // empty input field
        editText.setText("");
    }

    /** Create the listview, fetching data from database and setting the list adapter **/
    public void createList() {
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.toDoList);

        // define list adapter
        adapter = new SimpleCursorAdapter(this, R.layout.to_do_list_item, db_cursor, from, to, 0);

        // set special viewbinder in order to display checkboxes
        setAdapterViewBinder();
        adapter.notifyDataSetChanged();

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // set on click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

             public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                 checkTask(id);
             }
         });

        // Register ListView for context menu, implicitly defining item longclick listener
        registerForContextMenu(listView);
    }

    /** Set a special viewbinder for the cursor adapter in order to
     * display checkboxes (imageviews) correctly.**/
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
            case R.id.action_check:
                checkTask(info.id);
                return true;
            case R.id.action_edit:

                editTask(info.id, info.targetView);
                return true;
            case R.id.action_delete:
                deleteTask(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editTask(long id, View view) {
        TextView taskTextView = (TextView) view.findViewById(R.id.task);
        String task = taskTextView.getText().toString();

        Intent modify_intent = new Intent(getApplicationContext(), EditTaskActivity.class);
        modify_intent.putExtra("id", id);
        modify_intent.putExtra("task", task);

        startActivity(modify_intent);
    }

    /** Deletes a task **/
    private void deleteTask(long id) {
        // update database
        dbHelper.delete(id);

        refreshListView();
    }

    /** Flips the 'checked' state of a task **/
    public void checkTask(long id) {
        // flip the checked state of task in database
        dbHelper.update_checked(id);

        refreshListView();
    }

    /** Refresh the listView after database was updated **/
    public void refreshListView() {
        // update db_cursor
        db_cursor = dbHelper.fetch();
        // update the cursor used by adapter
        adapter.changeCursor(db_cursor);
        // notify adapter of updated cursor
        adapter.notifyDataSetChanged();
    }
}
