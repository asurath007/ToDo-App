package com.example.todolistapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todolistapp.Model.Task;
import com.example.todolistapp.Util.constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class dbHandler extends SQLiteOpenHelper {

    private Context ctx;

    public dbHandler( Context context) {
        super(context, constants.DB_NAME, null, constants.DB_VERSION);
        this.ctx= context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + constants.TABLE_NAME +"("
                + constants.KEY_ID +" INTEGER PRIMARY KEY," + constants.KEY_TITLE
                + " TEXT, " + constants.KEY_DETAILS + " TEXT, " + constants.KEY_DATE
                + " LONG);";

        db.execSQL(CREATE_TASK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + constants.TABLE_NAME);

        onCreate(db);
    }

    /**CRUD OPERATION: Create, Read, Update, Delete**/
    // Add Task

    public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(constants.KEY_TITLE, task.getTitle());
        cv.put(constants.KEY_DETAILS, task.getDetail());
        cv.put(constants.KEY_DATE, java.lang.System.currentTimeMillis());

        //insert the row
        db.insert(constants.TABLE_NAME,null,cv);

        Log.d("SAVED!!", "Saved to DB");

    }

    //Get a task

    public Task getTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(constants.TABLE_NAME, new String[]{
                constants.KEY_ID, constants.KEY_TITLE, constants.KEY_DETAILS,
                constants.KEY_DATE}, constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,
                null,null);

        if (cursor != null)
            cursor.moveToFirst();

            Task task = new Task();
            task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(constants.KEY_ID))));
            task.setTitle(cursor.getString(cursor.getColumnIndex(constants.KEY_TITLE)));
            task.setDetail(cursor.getString(cursor.getColumnIndex(constants.KEY_DETAILS)));

            //convert timestamp to readable format
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(constants.KEY_DATE)))
                    .getTime());

            task.setDateAddedOn(formattedDate);

        return task;
    }

    //Get all tasks
    public List<Task> getAllTasks(){
        SQLiteDatabase db= this.getReadableDatabase();

        List<Task> toDoTask = new ArrayList<>();

        Cursor cursor = db.query(constants.TABLE_NAME, new String[]{
                constants.KEY_ID, constants.KEY_TITLE, constants.KEY_DETAILS,
                constants.KEY_DATE},null,null,null,null, constants.KEY_DATE );

        if (cursor.moveToFirst()){
            do{
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(constants.KEY_ID))));
                task.setTitle(cursor.getString(cursor.getColumnIndex(constants.KEY_TITLE)));
                task.setDetail(cursor.getString(cursor.getColumnIndex(constants.KEY_DETAILS)));

                //convert timestamp to readable format
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(constants.KEY_DATE)))
                        .getTime());

                task.setDateAddedOn(formattedDate);

                //Add to todoTask
                toDoTask.add(task);

            }while (cursor.moveToNext());

        }

        return toDoTask;
    }

    //Update Task
    public int updateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(constants.KEY_TITLE, task.getTitle());
        values.put(constants.KEY_DETAILS, task.getDetail());
        values.put(constants.KEY_DATE, java.lang.System.currentTimeMillis()); //get system time

        //update row
        return db.update(constants.TABLE_NAME,values, constants.KEY_ID + "+?",
                new String[]{String.valueOf(task.getId())});

    }

    //Delete Task
    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(constants.TABLE_NAME, constants.KEY_ID + "+?",
                new String[]{String.valueOf(id)});

        db.close();

    }

    //Get count
    public int getTaskCount(){
        String countQuery = "SELECT * FROM " + constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
