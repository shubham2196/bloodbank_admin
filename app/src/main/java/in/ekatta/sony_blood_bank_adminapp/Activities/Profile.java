package in.ekatta.sony_blood_bank_adminapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import in.ekatta.sony_blood_bank_adminapp.R;
import in.ekatta.sony_blood_bank_adminapp.UserHolder;

public class Profile extends AppCompatActivity {

    DrawerLayout drawerLayout;
    private TextView pname, pemail, pmobile, ppassword;
    ImageView passVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pname = findViewById(R.id.pname);
        pemail = findViewById(R.id.pemail);
        pmobile = findViewById(R.id.pmobile);
        ppassword = findViewById(R.id.ppassword);
        passVisible = findViewById(R.id.passVisible);

        TextView main = findViewById(R.id.main_name);
        main.setText("Profile");

        passVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ppassword.getText().toString().contains("***")){
                    passVisible.setColorFilter(Color.parseColor("#bb0a1e"));
                    ppassword.setText("  "+ UserHolder.getInstance().getPass());
                } else {
                    passVisible.setColorFilter(Color.parseColor("#c5c5c5"));
                    ppassword.setText("  *********");
                }
            }
        });

        ImageView pencil = findViewById(R.id.Pencil);
        pencil.setVisibility(View.VISIBLE);

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

        LinearLayout manage = findViewById(R.id.PROFILE);
        manage.setBackgroundColor(Color.parseColor("#d8e0ec"));
        ImageView imageView = findViewById(R.id.PROFILE_IV);
        TextView textView = findViewById(R.id.PROFILE_TV);
        imageView.setColorFilter(Color.parseColor("#b91036"));
        textView.setTextColor(Color.parseColor("#b91036"));

        drawerLayout = findViewById(R.id.drawerLayout);

        pname.setText(pname.getText().toString() + UserHolder.getInstance().getName());
        pemail.setText(pemail.getText().toString() + UserHolder.getInstance().getEmail());
        pmobile.setText(pmobile.getText().toString() + UserHolder.getInstance().getMobile());

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                LayoutInflater inflater = Profile.this.getLayoutInflater();
                final View view = inflater.inflate(R.layout.changepass, null);

                builder.setView(view)
                        .setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                TextView cpass = view.findViewById(R.id.cpass);
                                TextView npass = view.findViewById(R.id.npass);
                                TextView rpass = view.findViewById(R.id.rpass);

                                if (!npass.getText().toString().equals(rpass.getText().toString())) {
                                    Toast.makeText(Profile.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                } else if (!cpass.getText().toString().equals(UserHolder.getInstance().getPass())) {
                                    Toast.makeText(Profile.this, "Current Password is Wrong.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                } else {
                                    FirebaseDatabase.getInstance().getReference().child("BloodBank")
                                            .child(UserHolder.getInstance().getEmail().split("\\.")[0].trim() + UserHolder.getInstance().getMobile())
                                            .child("pass").setValue(npass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            UserHolder.getInstance().setPass(npass.getText().toString());
                                            Toast.makeText(Profile.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Profile.this, "Error", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });
    }


    public void ClickMenu(View View) {
        ManageDonor.openDrawer(drawerLayout);
    }

    public void ClickLogo(View View) {
        ManageDonor.closeDrawer(drawerLayout);
    }

    public void ClickManageDonor(View View) {
        startActivity(new Intent(this, ManageDonor.class));
        finish();
    }

    public void ClickManageBloodBank(View view) {
        startActivity(new Intent(this, LoadBloodBank.class));
        finish();
    }

    public void ClickProfile(View view) {
        ManageDonor.closeDrawer(drawerLayout);
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
                        Intent intent = new Intent(Profile.this, LoginActivity.class);
                        intent.putExtra("forceclose","Yes");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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