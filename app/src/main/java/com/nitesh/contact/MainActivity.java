package com.nitesh.contact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button button;
    ListView listView;
//    ArrayAdapter<String> arrayAdapter;
//    ArrayList<String> myArrayList = new ArrayList<>();
    public static String Name = "name";
    public static String Id ="id";
    List<Model> contactList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        listView = findViewById(R.id.listView);

        //databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://contact-4292b.firebaseio.com/Contacts");
        databaseReference = FirebaseDatabase.getInstance().getReference("Contacts");
        contactList = new ArrayList<>();

//        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myArrayList);
//
//        listView.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddContact.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Model model = contactList.get(position);

                Intent intent = new Intent(getApplicationContext(), ShowDetals.class);

                intent.putExtra(Id, model.getUserid());
                intent.putExtra(Name, model.getName());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Model model = contactList.get(position);

                showUpdateDialog(model.getUserid(),model.getName(),model.getPhone(),model.getEmail(),model.getAddress());
                return false;
            }
        });


    }


    private void showUpdateDialog(final String userid, String name, String phone, String email, String address){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        editTextName.setText(name);
        final EditText editTextPhone = dialogView.findViewById(R.id.editTextPhone);
        editTextPhone.setText(phone);
        final EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        editTextEmail.setText(email);
        final EditText editTextAddress = dialogView.findViewById(R.id.editTextAddress);
        editTextAddress.setText(address);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);


        dialogBuilder.setTitle("Update Contact :  " + name);

        final AlertDialog alertDialog =dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();

                updateContact(userid, name, phone, email, address);
                alertDialog.dismiss();

            }
        });
    }
    private boolean updateContact(String id, String name, String phone, String email, String address){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contacts").child(id);
        Model model = new Model(address, email, name, phone, id);

        databaseReference.setValue(model);

        Toast.makeText(this,"Contact Updated",Toast.LENGTH_LONG).show();

        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contactList.clear();

                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()){
                    Model model = contactSnapshot.getValue(Model.class);

                    contactList.add(model);

                }
                ContactList adapter = new ContactList(MainActivity.this, contactList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
