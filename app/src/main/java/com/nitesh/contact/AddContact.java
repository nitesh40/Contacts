package com.nitesh.contact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddContact extends AppCompatActivity {
    TextView maddcontact;
    EditText mname, mnum, memail, maddress;
    Button mbutton, msave;
    ImageView imageView;
    String imageUrl;

    static final int PICK_IMAGE_REQUEST = 1;

    Uri uri;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://contact-4292b.firebaseio.com/Contacts");
        progressDialog=new ProgressDialog(this);


        maddcontact = findViewById(R.id.add);
        mname = findViewById(R.id.name);
        mnum = findViewById(R.id.num);
        memail = findViewById(R.id.email);
        maddress = findViewById(R.id.address);
        msave = findViewById(R.id.save);
        imageView = findViewById(R.id.image);
        mbutton = findViewById(R.id.choose);

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
                uri = data.getData();
                imageView.setImageURI(uri);
        }

    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void save(){

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageUrl = taskSnapshot.toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddContact.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        String aname= mname.getText().toString().trim();
        String anum= mnum.getText().toString().trim();
        String aemail= memail.getText().toString().trim();
        String amaddress= maddress.getText().toString().trim();

        if (anum.isEmpty()){
        mnum.setError("Number Required");
        mnum.requestFocus();
                return;
        }

            final String image = imageUrl;
            String id = databaseReference.push().getKey();    //unique id
            Model model = new Model(amaddress, aemail, aname, anum, id);
//            model.setName(mname.getText().toString());
//            model.setPhone(mnum.getText().toString());
//            model.setEmail(memail.getText().toString());
//            model.setAddress(maddress.getText().toString());
//            model.setId(id);
//
//            String username = model.getName();

            databaseReference.child(id).setValue(model);
            finish();
            progressDialog.setMessage("Adding Contact...");
            progressDialog.show();
    }

}
