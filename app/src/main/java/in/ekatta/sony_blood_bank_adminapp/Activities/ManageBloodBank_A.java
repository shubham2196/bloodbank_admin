package in.ekatta.sony_blood_bank_adminapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import in.ekatta.sony_blood_bank_adminapp.Adapter.BloodBankAdapter;
import in.ekatta.sony_blood_bank_adminapp.DataClasses.BloodBankData;
import in.ekatta.sony_blood_bank_adminapp.R;
import in.ekatta.sony_blood_bank_adminapp.UserHolder;

public class ManageBloodBank_A extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    SpotsDialog progressDialog;
    BloodBankAdapter bloodBankAdapter;
    ArrayList<BloodBankData> list;
    EditText search;

    TextView OP, AP, BP, ABP, ON, AN, BN, ABN;
    String BG = " O+ A+ B+ AB+ O- A- B- AB-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_blood_bank_a);

        OP = findViewById(R.id.OPP);
        AP = findViewById(R.id.APP);
        BP = findViewById(R.id.BPP);
        ABP = findViewById(R.id.ABPP);

        ON = findViewById(R.id.ONN);
        AN = findViewById(R.id.ANN);
        BN = findViewById(R.id.BNN);
        ABN = findViewById(R.id.ABNN);
        fab = findViewById(R.id.fab);

        progressDialog = new SpotsDialog(ManageBloodBank_A.this);

        TextView main = findViewById(R.id.main_name);
        main.setText("Manage Blood Bank");

        drawerLayout = findViewById(R.id.drawerLayout);
        LinearLayout linearLayout = findViewById(R.id.PROFILE);
        LinearLayout linearLayout2 = findViewById(R.id.MANAGEBLOODBANK);
        if (!UserHolder.getInstance().getEmail().equals("kdtech500@gmail.com")) {
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
            TextView user = findViewById(R.id.user);
            TextView post = findViewById(R.id.post);
            user.setText(UserHolder.getInstance().getName());
            post.setText("Blood Bank");
        }

        linearLayout2.setBackgroundColor(Color.parseColor("#d8e0ec"));
        ImageView imageView = findViewById(R.id.MANAGEBLOODBANK_IV);
        TextView textView = findViewById(R.id.MANAGEBLOODBANK_TV);
        imageView.setColorFilter(Color.parseColor("#b91036"));
        textView.setTextColor(Color.parseColor("#b91036"));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBloodBank_A.this, AddBloodBank.class));
            }
        });

        OP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BG.contains(" O+")) {
                    BG = BG.replace(" O+", "");
                    OP.setBackgroundResource(R.drawable.unselected);
                    OP.setTextColor(Color.parseColor("#2e6090"));
                    common();
                } else {
                    BG += " O+";
                    OP.setBackgroundResource(R.drawable.selected);
                    OP.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        AP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" A+")) {
                    BG = BG.replace(" A+", "");
                    AP.setBackgroundResource(R.drawable.unselected);
                    AP.setTextColor(Color.parseColor("#2e6090"));
                    common();
                } else {
                    BG += " A+";
                    AP.setBackgroundResource(R.drawable.selected);
                    AP.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        BP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" B+")) {
                    BG = BG.replace(" B+", "");
                    BP.setBackgroundResource(R.drawable.unselected);
                    BP.setTextColor(Color.parseColor("#2e6090"));
                    common();
                } else {
                    BG += " B+";
                    BP.setBackgroundResource(R.drawable.selected);
                    BP.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        ABP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" AB+")) {
                    BG = BG.replace(" AB+", "");
                    ABP.setBackgroundResource(R.drawable.unselected);
                    ABP.setTextColor(Color.parseColor("#2e6090"));
                    common();
                } else {
                    BG += " AB+";
                    ABP.setBackgroundResource(R.drawable.selected);
                    ABP.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        ON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" O-")) {
                    BG = BG.replace(" O-", "");
                    ON.setBackgroundResource(R.drawable.unselected);
                    ON.setTextColor(Color.parseColor("#2e6090"));
                    common();
                } else {
                    BG += " O-";
                    ON.setBackgroundResource(R.drawable.selected);
                    ON.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        AN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" A-")) {
                    BG = BG.replace(" A-", "");
                    AN.setBackgroundResource(R.drawable.unselected);
                    AN.setTextColor(Color.parseColor("#2e6090"));
                    common();
                } else {
                    BG += " A-";
                    AN.setBackgroundResource(R.drawable.selected);
                    AN.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        BN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" B-")) {
                    BG = BG.replace(" B-", "");
                    BN.setBackgroundResource(R.drawable.unselected);
                    BN.setTextColor(Color.parseColor("#2e6090"));
                    common();
                } else {
                    BG += " B-";
                    BN.setBackgroundResource(R.drawable.selected);
                    BN.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        ABN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" AB-")) {
                    BG = BG.replace(" AB-", "");
                    ABN.setBackgroundResource(R.drawable.unselected);
                    ABN.setTextColor(Color.parseColor("#2e6090"));
                    common();
                } else {
                    BG += " AB-";
                    ABN.setBackgroundResource(R.drawable.selected);
                    ABN.setTextColor(Color.parseColor("#ffffff"));
                    common();
                }
            }
        });

        search = findViewById(R.id.search_text);


        recyclerView = findViewById(R.id.bloodBankList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ManageBloodBank_A.this));
        list = new ArrayList<>();
        bloodBankAdapter = new BloodBankAdapter(ManageBloodBank_A.this, list);
        recyclerView.setAdapter(bloodBankAdapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BloodBank");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    BloodBankData data = ds.getValue(BloodBankData.class);
                    list.add(data);
                }
                bloodBankAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bloodBankAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (list.size() != 0) {
                    bloodBankAdapter.searchKeyword(s.toString());
                } else {
                    Toast.makeText(ManageBloodBank_A.this, "No Result Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAll() {

        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("BloodBank").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BloodBankData donorData = new BloodBankData();
                donorData.setName(snapshot.child("name").getValue().toString());
                donorData.setArealine(snapshot.child("arealine").getValue().toString());
                donorData.setCity(snapshot.child("city").getValue().toString());
                donorData.setDistrict(snapshot.child("district").getValue().toString());
                donorData.setLandmark(snapshot.child("landmark").getValue().toString());
                donorData.setMobile(snapshot.child("mobile").getValue().toString());
                donorData.setPin(snapshot.child("pin").getValue().toString());
                donorData.setEmail(snapshot.child("email").getValue().toString());

                list.add(donorData);
                bloodBankAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
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

    }

    private void common() {
        list.clear();
        if (BG.equals("")) {
            list.clear();
            bloodBankAdapter.notifyDataSetChanged();
            return;
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("BloodBank").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BloodBankData donorData = new BloodBankData();
                donorData.setName(snapshot.child("name").getValue().toString());
                donorData.setArealine(snapshot.child("arealine").getValue().toString());
                donorData.setCity(snapshot.child("city").getValue().toString());
                donorData.setDistrict(snapshot.child("district").getValue().toString());
                donorData.setLandmark(snapshot.child("landmark").getValue().toString());
                donorData.setMobile(snapshot.child("mobile").getValue().toString());
                donorData.setPin(snapshot.child("pin").getValue().toString());
                donorData.setEmail(snapshot.child("email").getValue().toString());

                if ((Boolean) snapshot.child("O+").getValue() && BG.contains(" O+"))
                    list.add(donorData);
                else if ((Boolean) snapshot.child("O-").getValue() && BG.contains(" O-"))
                    list.add(donorData);
                else if ((Boolean) snapshot.child("A-").getValue() && BG.contains(" A-"))
                    list.add(donorData);
                else if ((Boolean) snapshot.child("B-").getValue() && BG.contains(" B-"))
                    list.add(donorData);
                else if ((Boolean) snapshot.child("AB-").getValue() && BG.contains(" AB-"))
                    list.add(donorData);
                else if ((Boolean) snapshot.child("A+").getValue() && BG.contains(" A+"))
                    list.add(donorData);
                else if ((Boolean) snapshot.child("B+").getValue() && BG.contains(" B+"))
                    list.add(donorData);
                else if ((Boolean) snapshot.child("AB+").getValue() && BG.contains(" AB+"))
                    list.add(donorData);
                bloodBankAdapter.notifyDataSetChanged();
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
    }


    public void ClickMenu(View View) {
        ManageUser.openDrawer(drawerLayout);
    }


    public void ClickLogo(View View) {
        ManageUser.closeDrawer(drawerLayout);
    }


    public void ClickManageDonor(View View) {
        startActivity(new Intent(this, ManageUser.class));
        finish();
    }

    public void ClickManageBloodBank(View view) {
        ManageUser.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view) {
        startActivity(new Intent(this, Profile.class));
        finish();
    }

    public void ClickLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Do you want to Logout?")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ManageBloodBank_A.this, LoginActivity.class);
                        intent.putExtra("forceclose", "Yes");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        builder.create().show();
    }


}