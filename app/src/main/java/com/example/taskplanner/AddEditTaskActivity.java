package com.example.taskplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskplanner.data.Task;
import com.example.taskplanner.data.TaskDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText dueDateEditText;
    private Button saveButton;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        // Retrieve task details from intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TASK_ID")) {
            int taskId = intent.getIntExtra("TASK_ID", -1);
            String taskTitle = intent.getStringExtra("TASK_TITLE");
            String taskDescription = intent.getStringExtra("TASK_DESCRIPTION");
            String taskDueDate = intent.getStringExtra("TASK_DUE_DATE");

            // Populate the UI fields with task details for editing
            titleEditText.setText(taskTitle);
            descriptionEditText.setText(taskDescription);
            dueDateEditText.setText(taskDueDate);
        }
    }

    private void saveTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String dueDateString = dueDateEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(dueDateString)) {
            Toast.makeText(this, "Please enter a due date", Toast.LENGTH_SHORT).show();
            return;
        }

        Date dueDate;
        try {
            dueDate = dateFormat.parse(dueDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if the task ID is valid (indicating an existing task being edited)
        Intent intent = getIntent();
        int taskId = intent.getIntExtra("TASK_ID", -1);
        if (taskId != -1) {
            // Create a Task object with updated details
            Task updatedTask = new Task(taskId, title, description, dueDate.toString());

            // Update the task in the database
            TaskDBHelper dbHelper = new TaskDBHelper(this);
            int rowsAffected = dbHelper.updateTask(updatedTask);

            if (rowsAffected > 0) {
                // Task updated successfully
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
                navigateToTaskList();
            } else {
                // Error occurred while updating task
                Toast.makeText(this, "Failed to update task", Toast.LENGTH_SHORT).show();
            }
        } else {
            //     // Add new task
            Task newTask = new Task(title, description, dueDate.toString());
            // Insert the new task into the database
            TaskDBHelper dbHelper = new TaskDBHelper(this);
            long newTaskId = dbHelper.insertTask(newTask);

            newTask.setId((int) newTaskId);
// Show a "Task saved" Toast message
            Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show();
        }




        navigateToTaskList();
    }
    private void navigateToTaskList() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish current activity to prevent returning to AddEditTaskActivity
    }
}
