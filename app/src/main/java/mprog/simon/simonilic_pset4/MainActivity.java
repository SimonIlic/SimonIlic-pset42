package mprog.simon.simonilic_pset4;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper = new DatabaseHelper(this);
        List tasks = getTasks();

        createList(tasks);



    }

    public void addTask(View view) {

    }

    public List<String> getTasks() {
        Cursor cursor = dbHelper.fetch();


    }

    public void createList(List<String> tasks) {
        // Get ListView object from xml
        ListView listView = (ListView) findViewById(R.id.toDoList);

        // define list adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, tasks);

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                displayMovie(sharedPref, movieIDs, position);
            }
        });
    }
}
