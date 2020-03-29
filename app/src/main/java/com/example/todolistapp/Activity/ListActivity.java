package com.example.todolistapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.todolistapp.DataBase.dbHandler;
import com.example.todolistapp.Model.Task;
import com.example.todolistapp.UI.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.todolistapp.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    //To Show the items that has been added.

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Task> taskList;
    private List<Task> listItems;
    private dbHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText et_taskTitle;
    private EditText et_taskDetail;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                popUp();
            }

        });


        db = new dbHandler(this);
        recyclerView = findViewById(R.id.rv_main);
        recyclerView.setHasFixedSize(true); //all items fixed correctly
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //setting layout of rv

        taskList = new ArrayList<>();
        listItems = new ArrayList<>();

        // get items from db

        taskList =db.getAllTasks();

        for (Task c: taskList){
            Log.d("ad", "onCreate: " + c.getTitle());
            Task task = new Task();
            task.setTitle(c.getTitle());
            task.setDetail("Detail: " + c.getDetail());
            task.setId(c.getId());
            task.setDateAddedOn("Added On: "+ c.getDateAddedOn());

            listItems.add(task);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged(); //notifies changes in the adapter


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
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        },1000); //1sec
    }

}
