package com.nitesh.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowDetals extends AppCompatActivity {

    ListView listView;
    TextView uname;
    List<Model> detals;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detals);

        listView = findViewById(R.id.listdetails);
        uname = findViewById(R.id.textNAME);

        Intent intent = getIntent();

        String id = intent.getStringExtra(MainActivity.Id);
        String name = intent.getStringExtra(MainActivity.Name);

        uname.setText(name);

        databaseReference = FirebaseDatabase.getInstance().getReference("Contacts");
        detals = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Intent intent = getIntent();

                String id = intent.getStringExtra(MainActivity.Id);

                detals.clear();

                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()){
                    Model model = contactSnapshot.getValue(Model.class);

                    Log.d("String1",model.userid);

                    if (model.userid.equals(id)){
                        detals.add(model);
                    }
                }
                DetailList adapter = new DetailList(ShowDetals.this, detals);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
