package com.example.todolistapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.todolistapp.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView tskTitle;
    private TextView tskDetail;
    private TextView tskDateAddedOn;
    private int tskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tskTitle = findViewById(R.id.tv_title_det);
        tskDetail = findViewById(R.id.tv_details_det);
        tskDateAddedOn = findViewById(R.id.tv_dateAdded_det);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null ){   //key has to be same as in put extras
            tskTitle.setText(bundle.getString("TaskTitle"));
            tskDetail.setText(bundle.getString("TaskDetails"));
            tskDateAddedOn.setText(bundle.getString("Date"));
            tskID = bundle.getInt("ID");

        }


    }
}
