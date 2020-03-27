package com.example.todolistapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.todolistapp.DataBase.dbHandler;
import com.example.todolistapp.Model.Task;
import com.example.todolistapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText et_taskTitle;
    private EditText et_taskDetail;
    private dbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new dbHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       byPassActivity();

       //check if item was saved
//        List<Task> todoTask = db.getAllTasks();
//        for (Task task : todoTask) {
//            Log.d("Main", "onCreate: " + task.getTitle());
//        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                popUp();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void popUp() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        et_taskTitle = view.findViewById(R.id.et_task);
        et_taskDetail = view.findViewById(R.id.et_details);
        Button btn_save = view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save to DataBase & go to next screen


                if (et_taskTitle.getText().toString().isEmpty()
                        && et_taskDetail.getText().toString().isEmpty()) {
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT).show();
                }else{
                    saveTaskToDB(v);
                }

            }
        });

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

    }

    private void saveTaskToDB(View v) {

        Task task = new Task();

        String newTitle = et_taskTitle.getText().toString();
        String newDetail = et_taskDetail.getText().toString();

        task.setTitle(newTitle);
        task.setDetail(newDetail);

        //Save to DB
        db.addTask(task);

        Snackbar.make(v,"Item Saved", Snackbar.LENGTH_SHORT).show();
        Log.d("Item Added ID:", String.valueOf(db.getTaskCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        },1000); //1sec
    }

    public void byPassActivity(){
        //checks if database is empty; if not goes to list activity & shows all added item

        if (db.getTaskCount()>0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
            }
        }
    }