package in.ekatta.sony_blood_bank_adminapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import in.ekatta.sony_blood_bank_adminapp.R;
import in.ekatta.sony_blood_bank_adminapp.UserHolder;

public class ManageBloodBank_B extends AppCompatActivity {

    private DatabaseReference mReff;
    private ImageView backBtn;
    private CheckBox uOpositive, uApositive, uBpositive, uABpositive, uOnegative, uAnegative, uBnegative, uABnegative;
    private TextView ubbname, ubbarealine, ubblandmark, ubbcity, ubbdistrict, ubbpin, ubbmobile, ubbemail;
    private Button ubbupdateBtn, ubbcancelBtn;
    private String name, arealine, landmark, city, district, pin, mobile, pass, email,field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_blood_bank_b);

        uOpositive = findViewById(R.id.uOpositive);
        uOnegative = findViewById(R.id.uOnegative);

        uABnegative = findViewById(R.id.uABnegative);
        uABpositive = findViewById(R.id.uABpositive);

        uApositive = findViewById(R.id.uApositive);
        uAnegative = findViewById(R.id.uAnegative);

        uBnegative = findViewById(R.id.uBnegative);
        uBpositive = findViewById(R.id.uBpositive);

        ubbname = findViewById(R.id.ubbname);
        ubbarealine = findViewById(R.id.ubbarealine);
        ubblandmark = findViewById(R.id.ubblandmark);
        ubbcity = findViewById(R.id.ubbcity);
        ubbdistrict = findViewById(R.id.ubbdistrict);
        ubbpin = findViewById(R.id.ubbpin);
        ubbmobile = findViewById(R.id.ubbmobile);
        ubbemail = findViewById(R.id.ubbemail);

        ubbupdateBtn = findViewById(R.id.ubbupdateBtn);
        ubbcancelBtn = findViewById(R.id.ubbcancelBtn);
        backBtn = findViewById(R.id.backBtn);

        mReff = FirebaseDatabase.getInstance().getReference().child("BloodBank");

        Intent intent = getIntent();
        field = intent.getStringExtra("name");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBloodBank_B.this,ManageBloodBank_A.class));
                finish();
            }
        });

        mReff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().toString().equals(field)) {
                    ubbname.setText(snapshot.child("name").getValue().toString());
                    ubbcity.setText(snapshot.child("city").getValue().toString());
                    ubbdistrict.setText(snapshot.child("district").getValue().toString());
                    ubbemail.setText(snapshot.child("email").getValue().toString());
                    ubbmobile.setText(snapshot.child("mobile").getValue().toString());
                    ubbname.setText(snapshot.child("name").getValue().toString());
                    ubblandmark.setText(snapshot.child("landmark").getValue().toString());
                    ubbarealine.setText(snapshot.child("arealine").getValue().toString());
                    ubbpin.setText(snapshot.child("pin").getValue().toString());

                    uOpositive.setChecked((Boolean) snapshot.child("O+").getValue());
                    uApositive.setChecked((Boolean) snapshot.child("A+").getValue());
                    uBpositive.setChecked((Boolean) snapshot.child("B+").getValue());
                    uABpositive.setChecked((Boolean) snapshot.child("AB+").getValue());

                    uOnegative.setChecked((Boolean) snapshot.child("O-").getValue());
                    uAnegative.setChecked((Boolean) snapshot.child("A-").getValue());
                    uBnegative.setChecked((Boolean) snapshot.child("B-").getValue());
                    uABnegative.setChecked((Boolean) snapshot.child("AB-").getValue());

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ubbupdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = ubbname.getText().toString().trim();
                arealine = ubbarealine.getText().toString().trim();
                landmark = ubblandmark.getText().toString().trim();
                city = ubbcity.getText().toString().trim();
                district = ubbdistrict.getText().toString().trim();
                pin = ubbpin.getText().toString().trim();
                mobile = ubbmobile.getText().toString().trim();
                email = ubbemail.getText().toString().trim();
                pass = UserHolder.getInstance().getPass();

                if (name.isEmpty()) {
                    ubbname.setError("Name is Empty");
                    ubbname.requestFocus();
                } else if (arealine.isEmpty()) {
                    ubbarealine.setError("Area Line is Empty");
                    ubbarealine.requestFocus();
                } else if (landmark.isEmpty()) {
                    ubblandmark.setError("Landmark is Empty");
                    ubblandmark.requestFocus();
                } else if (city.isEmpty()) {
                    ubbcity.setError("City is Empty");
                    ubbcity.requestFocus();
                } else if (district.isEmpty()) {
                    ubbdistrict.setError("District is Empty");
                    ubbdistrict.requestFocus();
                } else if (pin.isEmpty()) {
                    ubbpin.setError("Pin is Empty");
                    ubbpin.requestFocus();
                } else if (mobile.isEmpty()) {
                    ubbmobile.setError("Mobile Number is Empty");
                    ubbmobile.requestFocus();
                } else if (!uOpositive.isChecked() && !uApositive.isChecked() && !uBpositive.isChecked() && !uABpositive.isChecked() && !uOnegative.isChecked() &&
                        !uAnegative.isChecked() && !uBnegative.isChecked() && !uABnegative.isChecked()) {
                    Toast.makeText(ManageBloodBank_B.this, "Please Select At least One of Blood Group", Toast.LENGTH_SHORT).show();
                } else if (pin.length() != 6) {
                    ubbpin.setError("Pin is Not Valid");
                    ubbpin.requestFocus();
                } else if (mobile.length() != 10) {
                    ubbmobile.setError("Mobile Number is Not Valid");
                    ubbmobile.requestFocus();
                } else if (!email.contains("@") || !email.contains(".")) {
                    ubbemail.setError("Enter Correct Email");
                    ubbemail.requestFocus();
                } else {
                    HashMap data = new HashMap();
                    data.put("name", name);
                    data.put("arealine", arealine);
                    data.put("landmark", landmark);
                    data.put("city", city);
                    data.put("district", district);
                    data.put("pin", pin);
                    data.put("mobile", mobile);
                    data.put("email", email);
                    data.put("pass", pass);
                    data.put("O+", uOpositive.isChecked());
                    data.put("A+", uApositive.isChecked());
                    data.put("B+", uBpositive.isChecked());
                    data.put("AB+", uABpositive.isChecked());
                    data.put("O-", uOnegative.isChecked());
                    data.put("A-", uAnegative.isChecked());
                    data.put("B-", uBnegative.isChecked());
                    data.put("AB-", uABnegative.isChecked());

                    mReff.child(field).updateChildren(data, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(ManageBloodBank_B.this, "Done", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ManageBloodBank_B.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(ManageBloodBank_B.this, ManageBloodBank_A.class));
                            finish();
                        }
                    });
                }
            }
        });

        ubbcancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBloodBank_B.this, ManageBloodBank_A.class));
                finish();
            }
        });

    }
}