package com.example.todolistapp.UI;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.Activity.DetailsActivity;
import com.example.todolistapp.DataBase.dbHandler;
import com.example.todolistapp.Model.Task;
import com.example.todolistapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private Context context;
    private List<Task> taskList;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
       //inflate list row

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder( RecyclerViewAdapter.ViewHolder holder, int position) {
        //binds view with widgets

        Task task = taskList.get(position);

        holder.task_title.setText(task.getTitle());
        holder.task_details.setText(task.getDetail());
        holder.task_dateAdded.setText(task.getDateAddedOn());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView task_title;
        public TextView task_details;
        public TextView task_dateAdded;
        public Button btn_edit;
        public Button btn_delete;
        public int id;


        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            task_title = view.findViewById(R.id.tv_title);
            task_details = view.findViewById(R.id.tv_details);
            task_dateAdded = view.findViewById(R.id.tv_date);
            btn_edit = view.findViewById(R.id.btn_edit);
            btn_delete = view.findViewById(R.id.btn_delete);



            btn_edit.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen when the card is selected
                    //first get position of our item

                    int position = getAdapterPosition();

                    Task task = taskList.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("TaskTitle", task.getTitle());
                    intent.putExtra("TaskDetails", task.getDetail());
                    intent.putExtra("ID", task.getId());
                    intent.putExtra("Date", task.getDateAddedOn());

                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_edit:
                    int position = getAdapterPosition();
                    Task task = taskList.get(position);
                    editTask(task);

                    break;

                case R.id.btn_delete:
                    position = getAdapterPosition();
                    task = taskList.get(position);
                    deleteTask(task.getId());

                    break;
            }
        }

        public void deleteTask(final int id) {
            //create AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button yesButton = view.findViewById(R.id.btn_yes);
            Button noButton = view.findViewById(R.id.btn_no);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item

                    dbHandler db = new dbHandler(context);
                    db.deleteTask(id);
                    taskList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        private void editTask(final Task task) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);
            final EditText et_taskTitle = view.findViewById(R.id.et_task);
            final EditText et_taskDetail = view.findViewById(R.id.et_details);
            final TextView tv_title = view.findViewById(R.id.tv_title);

            tv_title.setText("Edit Task");
            Button btn_save = view.findViewById(R.id.btn_save);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHandler db =new dbHandler(context);

                    //update item
                    task.setTitle(et_taskTitle.getText().toString());
                    task.setDetail(et_taskDetail.getText().toString());

                    if (! et_taskTitle.getText().toString().isEmpty()
                            && ! et_taskDetail.getText().toString().isEmpty()) {
                       db.updateTask(task);
                       notifyItemChanged(getAdapterPosition(),task);
                    }else{
                        Snackbar.make(view, "Empty Fields are not allowed",
                                Snackbar.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();


                }
            });

        }

    }

}



