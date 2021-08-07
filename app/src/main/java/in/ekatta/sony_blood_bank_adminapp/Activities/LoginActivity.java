package in.ekatta.sony_blood_bank_adminapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

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
    CardView main;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;
    boolean found = false;
    String m_Text = "";
    private Button loginBtn;
    private ImageView passView;
    SpotsDialog progressDialog;
    private TextView uname, pass, resetPassword;
    private Boolean isView;
    private String username, password;
    private DatabaseReference mDatabase;
    String OTP;

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
        resetPassword = findViewById(R.id.resetPassword);
        main = findViewById(R.id.main);


        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Enter Registered Mobile Number :-");
                View abc = LayoutInflater.from(LoginActivity.this).inflate(R.layout.send_otp, main, false);
                builder.setView(abc);

                TextView input = abc.findViewById(R.id.phoneNumber);

// Set up the buttons
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (m_Text.length() == 10) {
                            progressDialog.show();
                            FirebaseDatabase.getInstance().getReference().child("BloodBank").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                                    if (snapshot.getKey().toString().contains(m_Text)) {
                                        found = true;
                                        sendVerificationCode(m_Text);
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

                                }

                                @Override
                                public void onCancelled(DatabaseError error) {

                                }
                            });
                            if (!found) {
                                Toast.makeText(LoginActivity.this, "Mobile Number is not Registered.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong Mobile Number", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

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
                    startActivity(new Intent(LoginActivity.this, ManageDonor.class));
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
                                    startActivity(new Intent(LoginActivity.this, ManageDonor.class));
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
            if ("Yes".equals(intent.getStringExtra("forceclose"))) {
                editor.clear();
                editor.apply();
            }
        } catch (Exception ignored) {

        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);

        if (UnameValue.equals("kdtech500@gmail.com")) {
            UserHolder.getInstance().setEmail(UnameValue);
            startActivity(new Intent(LoginActivity.this, ManageDonor.class));
            finish();
            return;
        }

        progressDialog.show();
        if (UnameValue.length() == 0) {
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
                        startActivity(new Intent(LoginActivity.this, ManageDonor.class));
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


    private void sendVerificationCode(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91" + phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                OTP = s;
                                super.onCodeSent(s, forceResendingToken);

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("Enter OTP :-");
                                View abc = LayoutInflater.from(LoginActivity.this).inflate(R.layout.send_otp, main, false);
                                builder.setView(abc);

                                TextView input = abc.findViewById(R.id.phoneNumber);
                                builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, input.getText().toString());
                                        FirebaseAuth.getInstance().signInWithCredential(credential)
                                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            setUser(phoneNo);
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            dialog.cancel();
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        progressDialog.dismiss();
                                    }
                                });

                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                setUser(phoneNo);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("abc", "onVerificationFailed: " + e.getMessage());
                                progressDialog.dismiss();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public void setUser(String username) {

        FirebaseDatabase.getInstance().getReference().child("BloodBank").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                if (snapshot.getValue().toString().contains(username)) {
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
                    startActivity(new Intent(LoginActivity.this, ManageDonor.class));
                    finish();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}