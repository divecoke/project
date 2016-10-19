package com.example.ernestas.myapplication65651;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.utilities.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class ProfileSettings extends AppCompatActivity implements View.OnClickListener{

    EditText etFirstName, etLastName, etAddress;
    Button bSaveInformation;

    Firebase firebase;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Firebase.setAndroidContext(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        firebase = new Firebase("https://howdoilooktoday-401f4.firebaseio.com");

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etAddress = (EditText) findViewById(R.id.etAddress);


        bSaveInformation = (Button) findViewById(R.id.bSaveInformation);

        bSaveInformation.setOnClickListener(this);

        Firebase dataRef = firebase.child(user.getUid());

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                if (dataSnapshot.hasChild("firstName")) {
                    String FirstName = map.get("firstName");
                    etFirstName.setText(FirstName);
                }
                if(dataSnapshot.hasChild("lastName")) {
                    String LastName = map.get("lastName");
                    etLastName.setText(LastName);
                }
                if(dataSnapshot.hasChild("address")) {
                    String Address = map.get("address");
                    etAddress.setText(Address);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    private void saveUserInformation() {
        String first = etFirstName.getText().toString().trim();
        String last = etLastName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        UserInformation userInformation = new UserInformation(first, last, address);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(TextUtils.isEmpty(first)) {
            Toast.makeText(this, "You must fill First Name edit text bar", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(last)) {
            Toast.makeText(this, "You must fill Last Name edit text bar", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(address)) {
            Toast.makeText(this, "You must fill Address edit text bar", Toast.LENGTH_LONG).show();
        } else {
            databaseReference.child(user.getUid()).setValue(userInformation); //Saugojimas
            Toast.makeText(this, "Information Saved", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        if(v == bSaveInformation) {
            saveUserInformation();
        }
    }
}
