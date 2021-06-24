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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import in.ekatta.sony_blood_bank_adminapp.Adapter.DonorAdapter;
import in.ekatta.sony_blood_bank_adminapp.DataClasses.DonorData;
import in.ekatta.sony_blood_bank_adminapp.R;
import in.ekatta.sony_blood_bank_adminapp.UserHolder;

public class ManageUser extends AppCompatActivity {

    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    DonorAdapter dataAdapter;
    ArrayList<DonorData> list;
    SpotsDialog progressDialog;
    EditText search;
    TextView OPP, APP, BPP, ABPP, ONN, ANN, BNN, ABNN;
    String BG = " O+ A+ B+ AB+ O- A- B- AB-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        OPP = findViewById(R.id.OPP);
        APP = findViewById(R.id.APP);
        BPP = findViewById(R.id.BPP);
        ABPP = findViewById(R.id.ABPP);
        progressDialog = new SpotsDialog(ManageUser.this);

        TextView main = findViewById(R.id.main_name);
        main.setText("Manage Donor");

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

        LinearLayout manage = findViewById(R.id.MANAGEDONOR);
        manage.setBackgroundColor(Color.parseColor("#d8e0ec"));
        ImageView imageView = findViewById(R.id.MANAGEDONOR_IV);
        TextView textView = findViewById(R.id.MANAGEDONOR_TV);
        imageView.setColorFilter(Color.parseColor("#b91036"));
        textView.setTextColor(Color.parseColor("#b91036"));

        ONN = findViewById(R.id.ONN);
        ANN = findViewById(R.id.ANN);
        BNN = findViewById(R.id.BNN);
        ABNN = findViewById(R.id.ABNN);

        OPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" O+")) {
                    BG = BG.replace(" O+", "");
                    OPP.setBackgroundResource(R.drawable.unselected);
                    OPP.setTextColor(Color.parseColor("#2e6090"));
                    showSelectedBloodGroupDonor(BG);
                } else {
                    BG += " O+";
                    OPP.setBackgroundResource(R.drawable.selected);
                    OPP.setTextColor(Color.parseColor("#ffffff"));
                    showSelectedBloodGroupDonor(BG);
                }
            }
        });

        APP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" A+")) {
                    BG = BG.replace(" A+", "");
                    APP.setBackgroundResource(R.drawable.unselected);
                    APP.setTextColor(Color.parseColor("#2e6090"));
                    showSelectedBloodGroupDonor(BG);
                } else {
                    BG += " A+";
                    APP.setBackgroundResource(R.drawable.selected);
                    APP.setTextColor(Color.parseColor("#ffffff"));
                    showSelectedBloodGroupDonor(BG);
                }
            }
        });

        BPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" B+")) {
                    BG = BG.replace(" B+", "");
                    BPP.setBackgroundResource(R.drawable.unselected);
                    BPP.setTextColor(Color.parseColor("#2e6090"));
                    showSelectedBloodGroupDonor(BG);
                } else {
                    BG += " B+";
                    BPP.setBackgroundResource(R.drawable.selected);
                    BPP.setTextColor(Color.parseColor("#ffffff"));
                    showSelectedBloodGroupDonor(BG);
                }
            }
        });

        ABPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" AB+")) {
                    BG = BG.replace(" AB+", "");
                    ABPP.setBackgroundResource(R.drawable.unselected);
                    ABPP.setTextColor(Color.parseColor("#2e6090"));
                    showSelectedBloodGroupDonor(BG);
                } else {
                    BG += " AB+";
                    ABPP.setBackgroundResource(R.drawable.selected);
                    ABPP.setTextColor(Color.parseColor("#ffffff"));
                    showSelectedBloodGroupDonor(BG);
                }
            }
        });

        ONN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" O-")) {
                    BG = BG.replace(" O-", "");
                    ONN.setBackgroundResource(R.drawable.unselected);
                    ONN.setTextColor(Color.parseColor("#2e6090"));
                    showSelectedBloodGroupDonor(BG);
                } else {
                    BG += " O-";
                    ONN.setBackgroundResource(R.drawable.selected);
                    ONN.setTextColor(Color.parseColor("#ffffff"));
                    showSelectedBloodGroupDonor(BG);
                }
            }
        });

        ANN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" A-")) {
                    BG = BG.replace(" A-", "");
                    ANN.setBackgroundResource(R.drawable.unselected);
                    ANN.setTextColor(Color.parseColor("#2e6090"));
                    showSelectedBloodGroupDonor(BG);
                } else {
                    BG += " A-";
                    ANN.setBackgroundResource(R.drawable.selected);
                    ANN.setTextColor(Color.parseColor("#ffffff"));
                    showSelectedBloodGroupDonor(BG);
                }
            }
        });

        BNN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" B-")) {
                    BG = BG.replace(" B-", "");
                    BNN.setBackgroundResource(R.drawable.unselected);
                    BNN.setTextColor(Color.parseColor("#2e6090"));
                    showSelectedBloodGroupDonor(BG);
                } else {
                    BG += " B-";
                    BNN.setBackgroundResource(R.drawable.selected);
                    BNN.setTextColor(Color.parseColor("#ffffff"));
                    showSelectedBloodGroupDonor(BG);
                }
            }
        });

        ABNN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BG.contains(" AB-")) {
                    BG = BG.replace(" AB-", "");
                    ABNN.setBackgroundResource(R.drawable.unselected);
                    ABNN.setTextColor(Color.parseColor("#2e6090"));
                    showSelectedBloodGroupDonor(BG);
                } else {
                    BG += " AB-";
                    ABNN.setBackgroundResource(R.drawable.selected);
                    ABNN.setTextColor(Color.parseColor("#ffffff"));
                    showSelectedBloodGroupDonor(BG);
                }
            }
        });


        recyclerView = findViewById(R.id.donorList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ManageUser.this));
        list = new ArrayList<>();
        dataAdapter = new DonorAdapter(ManageUser.this, list);
        recyclerView.setAdapter(dataAdapter);

        search = findViewById(R.id.search_text);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (list.size() != 0) {
                    dataAdapter.searchKeyword(s.toString());
                }
            }
        });

        showAll();

    }

    private void showAll(){
        progressDialog.show();
        list.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().toString().equals("UserDetails")) {
                    Iterable<DataSnapshot> ds = snapshot.getChildren();
                    for (DataSnapshot dS : ds) {
                            DonorData data = dS.getValue(DonorData.class);
                            list.add(data);
                    }
                    dataAdapter.notifyDataSetChanged();
                }
                dataAdapter.notifyDataSetChanged();
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

    private void showSelectedBloodGroupDonor(String selectedBloodGroup) {
        list.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().toString().equals("UserDetails")) {
                    Iterable<DataSnapshot> ds = snapshot.getChildren();
                    for (DataSnapshot dS : ds) {
                        if (selectedBloodGroup.contains(" "+dS.child("bloodGroup").getValue().toString())) {
                            DonorData data = dS.getValue(DonorData.class);
                            list.add(data);
                        }
                    }
                    dataAdapter.notifyDataSetChanged();
                }
                dataAdapter.notifyDataSetChanged();
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
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View View) {
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickManageDonor(View View) {
        closeDrawer(drawerLayout);
    }

    public void ClickManageBloodBank(View view) {
        startActivity(new Intent(this, ManageBloodBank_A.class));
        finish();
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
                        Intent intent = new Intent(ManageUser.this, LoginActivity.class);
                        intent.putExtra("forceclose","Yes");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

}