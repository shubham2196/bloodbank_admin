package in.ekatta.sony_blood_bank_adminapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import in.ekatta.sony_blood_bank_adminapp.R;

public class AddBloodBank extends AppCompatActivity {

    private DatabaseReference mReff;
    private ImageView abackBtn;
    private CheckBox Opositive, Apositive, Bpositive, ABpositive, Onegative, Anegative, Bnegative, ABnegative;
    private TextView bbname, bbarealine, bblandmark, bbcity, bbdistrict, bbpin, bbmobile, bbpass, bbreppass, bbemail;
    private Button bbsaveBtn, bbcancelBtn;
    private String name, arealine, landmark, city, district, pin, mobile, pass, reppass, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blood_bank);

        bbname = findViewById(R.id.bbname);
        bbarealine = findViewById(R.id.bbarealine);
        bblandmark = findViewById(R.id.bblandmark);
        bbcity = findViewById(R.id.bbcity);
        bbdistrict = findViewById(R.id.bbdistrict);
        bbpin = findViewById(R.id.bbpin);
        bbmobile = findViewById(R.id.bbmobile);
        bbpass = findViewById(R.id.bbpass);
        bbreppass = findViewById(R.id.bbreppass);
        bbemail = findViewById(R.id.bbemail);

        Opositive = findViewById(R.id.Opositive);
        Apositive = findViewById(R.id.Apositive);
        Bpositive = findViewById(R.id.Bpositive);
        ABpositive = findViewById(R.id.ABpositive);
        Onegative = findViewById(R.id.Onegative);
        Anegative = findViewById(R.id.Anegative);
        Bnegative = findViewById(R.id.Bnegative);
        ABnegative = findViewById(R.id.ABnegative);

        bbsaveBtn = findViewById(R.id.bbsaveBtn);
        bbcancelBtn = findViewById(R.id.bbcancelBtn);
        abackBtn = findViewById(R.id.abackBtn);

        mReff = FirebaseDatabase.getInstance().getReference().child("BloodBank");

        bbcancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        abackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bbsaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = bbname.getText().toString().trim();
                arealine = bbarealine.getText().toString().trim();
                landmark = bblandmark.getText().toString().trim();
                city = bbcity.getText().toString().trim();
                district = bbdistrict.getText().toString().trim();
                pin = bbpin.getText().toString().trim();
                mobile = bbmobile.getText().toString().trim();
                pass = bbpass.getText().toString().trim();
                reppass = bbreppass.getText().toString().trim();
                email = bbemail.getText().toString().trim();

                if (name.isEmpty()) {
                    bbname.setError("Name is Empty");
                    bbname.requestFocus();
                } else if (arealine.isEmpty()) {
                    bbarealine.setError("Area Line is Empty");
                    bbarealine.requestFocus();
                } else if (landmark.isEmpty()) {
                    bblandmark.setError("Landmark is Empty");
                    bblandmark.requestFocus();
                } else if (city.isEmpty()) {
                    bbcity.setError("City is Empty");
                    bbcity.requestFocus();
                } else if (district.isEmpty()) {
                    bbdistrict.setError("District is Empty");
                    bbdistrict.requestFocus();
                } else if (pin.isEmpty()) {
                    bbpin.setError("Pin is Empty");
                    bbpin.requestFocus();
                } else if (mobile.isEmpty()) {
                    bbmobile.setError("Mobile Number is Empty");
                    bbmobile.requestFocus();
                } else if (pass.isEmpty()) {
                    bbpass.setError("Password is Empty");
                    bbpass.requestFocus();
                } else if (reppass.isEmpty()) {
                    bbreppass.setError("Repeat Password is Empty");
                    bbreppass.requestFocus();
                } else if (!pass.equals(reppass)) {
                    Toast.makeText(AddBloodBank.this, "Passwords Not Matched", Toast.LENGTH_SHORT).show();
                } else if (!Opositive.isChecked() && !Apositive.isChecked() && !Bpositive.isChecked() && !ABpositive.isChecked() && !Onegative.isChecked() &&
                        !Anegative.isChecked() && !Bnegative.isChecked() && !ABnegative.isChecked()) {
                    Toast.makeText(AddBloodBank.this, "Please Select At least One of Blood Group", Toast.LENGTH_SHORT).show();
                } else if (pin.length() != 6) {
                    bbpin.setError("Pin is Not Valid");
                    bbpin.requestFocus();
                } else if (mobile.length() != 10) {
                    bbmobile.setError("Mobile Number is Not Valid");
                    bbmobile.requestFocus();
                } else if (pass.length() < 6) {
                    bbpass.setError("Password should be of Length >= 6");
                    bbpass.requestFocus();
                } else if (!email.contains("@") || !email.contains(".")) {
                    bbemail.setError("Enter Correct Email");
                    bbemail.requestFocus();
                } else {
                    HashMap data = new HashMap();
                    data.put("name",name);
                    data.put("arealine",arealine);
                    data.put("landmark",landmark);
                    data.put("city",city);
                    data.put("district",district);
                    data.put("pin",pin);
                    data.put("mobile",mobile);
                    data.put("email",email);
                    data.put("pass",pass);
                    data.put("O+",Opositive.isChecked());
                    data.put("A+",Apositive.isChecked());
                    data.put("B+",Bpositive.isChecked());
                    data.put("AB+",ABpositive.isChecked());
                    data.put("O-",Onegative.isChecked());
                    data.put("A-",Anegative.isChecked());
                    data.put("B-",Bnegative.isChecked());
                    data.put("AB-",ABnegative.isChecked());

                    mReff.child(email.split("\\.")[0].trim() + mobile).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddBloodBank.this, "Done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddBloodBank.this, LoadBloodBank.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddBloodBank.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}