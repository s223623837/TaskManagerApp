package com.example.taskplanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskplanner.adapters.TaskAdapter;
import com.example.taskplanner.data.Task;
import com.example.taskplanner.data.TaskDBHelper;
import com.example.taskplanner.utils.DateUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;


    private Button addTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch tasks from database using TaskDBHelper
        TaskDBHelper dbHelper = new TaskDBHelper(this);
        taskList = dbHelper.getAllTasks();

        // Initialize TaskAdapter with the fetched taskList and set the adapter to RecyclerView
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);
        addTaskButton = findViewById(R.id.addTaskButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddEditTaskActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onTaskClick(Task task) {
        // Handle task item click (e.g., open Task Details Activity)
        Toast.makeText(this, "Task clicked: " + task.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(Task task) {
        // Navigate to AddEditTaskActivity with the task details
        Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
        intent.putExtra("TASK_ID", task.getId());
        intent.putExtra("TASK_TITLE", task.getTitle());
        intent.putExtra("TASK_DESCRIPTION", task.getDescription());
        intent.putExtra("TASK_DUE_DATE", DateUtils.convertDateFormat( task.getDueDate()) );
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteClick(Task task) {
        // Delete the task from the database
        TaskDBHelper dbHelper = new TaskDBHelper(this);
        dbHelper.deleteTask(task.getId());

        // Remove the task from the RecyclerView list
        taskList.remove(task);
        taskAdapter.notifyDataSetChanged(); // Notify adapter of data change

        Toast.makeText(this, "Task deleted: " + task.getTitle(), Toast.LENGTH_SHORT).show();
    }

}