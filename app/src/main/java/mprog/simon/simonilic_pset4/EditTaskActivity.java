package mprog.simon.simonilic_pset4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EditTaskActivity extends Activity {
    private DatabaseHelper dbHelper;
    private long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        setTitle("Edit Task");

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        _id = intent.getLongExtra("id", -1);
        String task = intent.getStringExtra("task");

        TextView taskTextView = (TextView) findViewById(R.id.task_edit);
        taskTextView.setText(task);
    }

    public void cancelEdit(View view) {
        Toast.makeText(this, "Cancelled Edit", Toast.LENGTH_SHORT).show();
        returnHome();
    }

    public void updateTask(View view) {
        TextView taskTextView = (TextView) findViewById(R.id.task_edit);
        String new_task = taskTextView.getText().toString();

        dbHelper.update_task(_id, new_task);
        Toast.makeText(this, "Edited Task", Toast.LENGTH_SHORT).show();

        returnHome();
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
        finish();
    }
}
