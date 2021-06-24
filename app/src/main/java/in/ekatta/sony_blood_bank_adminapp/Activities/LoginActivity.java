package in.ekatta.sony_blood_bank_adminapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;
import in.ekatta.sony_blood_bank_adminapp.R;
import in.ekatta.sony_blood_bank_adminapp.UserHolder;

public class LoginActivity extends AppCompatActivity {
    public static SharedPreferences.Editor editor;

    public static final String PREFS_NAME = "preferences";
    public static final String PREF_UNAME = "Username";
    public static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;


    private Button loginBtn;
    private ImageView passView;
    SpotsDialog progressDialog;
    private TextView uname, pass;
    private Boolean isView;
    private String username, password;
    private DatabaseReference mDatabase;

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setting up variables
        uname = findViewById(R.id.uname);
        pass = findViewById(R.id.pass);
        loginBtn = findViewById(R.id.loginBtn);
        passView = findViewById(R.id.passView);
        isView = false;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("BloodBank");

        progressDialog = new SpotsDialog(LoginActivity.this);
        passView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isView) {
                    passView.setColorFilter(Color.parseColor("#c5c5c5"));
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isView = false;
                } else {
                    passView.setColorFilter(Color.parseColor("#bb0a1e"));
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isView = true;
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validation
                username = uname.getText().toString().trim();
                password = pass.getText().toString().trim();

                if (username.isEmpty()) {
                    uname.setText("");
                    uname.setError("Please Enter Valid Username");
                    uname.requestFocus();
                } else if (password.length() < 6) {
                    pass.setText("");
                    pass.setError("Incorrect Password");
                    pass.requestFocus();
                } else if (username.equals("kdtech500@gmail.com") && password.equals("123456")) {
                    UserHolder.getInstance().setEmail(username);
                    UnameValue = username;
                    PasswordValue = password;
                    savePreferences();
                    startActivity(new Intent(LoginActivity.this, ManageUser.class));
                    finish();
                } else {
                    progressDialog.show();
                    if (username.contains("@") && username.contains(".")) {
                        username = username.split("\\.")[0];
                    }
                    mDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (snapshot.getValue().toString().contains(username)) {
                                if (snapshot.child("pass").getValue().toString().equals(password)) {
                                    UserHolder.getInstance().setOPositive((Boolean) snapshot.child("O+").getValue());
                                    UserHolder.getInstance().setAPositive((Boolean) snapshot.child("A+").getValue());
                                    UserHolder.getInstance().setBPositive((Boolean) snapshot.child("B+").getValue());
                                    UserHolder.getInstance().setABPositive((Boolean) snapshot.child("AB+").getValue());

                                    UserHolder.getInstance().setONegative((Boolean) snapshot.child("O-").getValue());
                                    UserHolder.getInstance().setANegative((Boolean) snapshot.child("A-").getValue());
                                    UserHolder.getInstance().setBNegative((Boolean) snapshot.child("B-").getValue());
                                    UserHolder.getInstance().setABNegative((Boolean) snapshot.child("AB-").getValue());

                                    UserHolder.getInstance().setArealine(snapshot.child("arealine").getValue().toString().trim());
                                    UserHolder.getInstance().setCity(snapshot.child("city").getValue().toString().trim());
                                    UserHolder.getInstance().setDistrict(snapshot.child("district").getValue().toString().trim());
                                    UserHolder.getInstance().setEmail(snapshot.child("email").getValue().toString().trim());
                                    UserHolder.getInstance().setLandmark(snapshot.child("landmark").getValue().toString().trim());
                                    UserHolder.getInstance().setMobile(snapshot.child("mobile").getValue().toString().trim());
                                    UserHolder.getInstance().setName(snapshot.child("name").getValue().toString().trim());
                                    UserHolder.getInstance().setPass(snapshot.child("pass").getValue().toString().trim());
                                    UserHolder.getInstance().setPin(snapshot.child("pin").getValue().toString().trim());
                                    UnameValue = username;
                                    PasswordValue = password;
                                    savePreferences();
                                    progressDialog.dismiss();
                                    startActivity(new Intent(LoginActivity.this, ManageUser.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
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
                            Toast.makeText(LoginActivity.this, "User Not Exist", Toast.LENGTH_SHORT).show();
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        });
    }


    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        // Edit and commit
        UnameValue = username;
        PasswordValue = password;
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.apply();
    }

    private void loadPreferences() {


        try {
            Intent intent = getIntent();
            if ("Yes".equals(intent.getStringExtra("forceclose"))){
                editor.clear();
                editor.apply();
            }
        } catch (Exception ignored){

        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);

        if (UnameValue.equals("kdtech500@gmail.com")) {
            UserHolder.getInstance().setEmail(UnameValue);
            startActivity(new Intent(LoginActivity.this, ManageUser.class));
            finish();
            return;
        }

        progressDialog.show();
        if (UnameValue.length() == 0){
            progressDialog.dismiss();
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("BloodBank").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue().toString().contains(UnameValue)) {
                    if (snapshot.child("pass").getValue().toString().equals(PasswordValue)) {
                        UserHolder.getInstance().setOPositive((Boolean) snapshot.child("O+").getValue());
                        UserHolder.getInstance().setAPositive((Boolean) snapshot.child("A+").getValue());
                        UserHolder.getInstance().setBPositive((Boolean) snapshot.child("B+").getValue());
                        UserHolder.getInstance().setABPositive((Boolean) snapshot.child("AB+").getValue());

                        UserHolder.getInstance().setONegative((Boolean) snapshot.child("O-").getValue());
                        UserHolder.getInstance().setANegative((Boolean) snapshot.child("A-").getValue());
                        UserHolder.getInstance().setBNegative((Boolean) snapshot.child("B-").getValue());
                        UserHolder.getInstance().setABNegative((Boolean) snapshot.child("AB-").getValue());

                        UserHolder.getInstance().setArealine(snapshot.child("arealine").getValue().toString().trim());
                        UserHolder.getInstance().setCity(snapshot.child("city").getValue().toString().trim());
                        UserHolder.getInstance().setDistrict(snapshot.child("district").getValue().toString().trim());
                        UserHolder.getInstance().setEmail(snapshot.child("email").getValue().toString().trim());
                        UserHolder.getInstance().setLandmark(snapshot.child("landmark").getValue().toString().trim());
                        UserHolder.getInstance().setMobile(snapshot.child("mobile").getValue().toString().trim());
                        UserHolder.getInstance().setName(snapshot.child("name").getValue().toString().trim());
                        UserHolder.getInstance().setPass(snapshot.child("pass").getValue().toString().trim());
                        UserHolder.getInstance().setPin(snapshot.child("pin").getValue().toString().trim());

                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, ManageUser.class));
                        finish();
                    } else {
                    }
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
                Toast.makeText(LoginActivity.this, "User Not Exist", Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.dismiss();
    }
}