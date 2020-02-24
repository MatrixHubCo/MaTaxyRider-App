package tz.co.matrixhub.mataxyrider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import tz.co.matrixhub.mataxyrider.Model.Rider;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    //Button
    Button btnSignIn, btnRegister;

    //Layout
    RelativeLayout rootLayout;

    //firebase
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference riders;

    private final static int PERMISSION = 1000;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //before setContentView, insert and set the font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        //firebase initialization
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        riders = db.getReference("Riders");

        //event listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login,null);

        final MaterialEditText editEmail = login_layout.findViewById(R.id.editEmail);
        final MaterialEditText editPassword = login_layout.findViewById(R.id.editPassword);

        dialog.setView(login_layout);

        //set Button
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                btnSignIn.setEnabled(false);

                //check validation
                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_LONG).show();
                    btnSignIn.setEnabled(true);
                    return;
                }

                if (editPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootLayout, "Password too short", Snackbar.LENGTH_LONG).show();
                    btnSignIn.setEnabled(true);
                    return;
                }

                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter your password", Snackbar.LENGTH_LONG).show();
                    btnSignIn.setEnabled(true);
                    return;
                }

                final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(MainActivity.this).build();
                waitingDialog.show();

                //login codes
                auth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLayout, "Error "+e.getMessage(), Snackbar.LENGTH_LONG).show();
                        btnSignIn.setEnabled(true);
                    }
                });
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void showRegisterDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);

        final MaterialEditText editEmail = register_layout.findViewById(R.id.editEmail);
        final MaterialEditText editName = register_layout.findViewById(R.id.editName);
        final MaterialEditText editPassword = register_layout.findViewById(R.id.editPassword);
        final MaterialEditText editPhone = register_layout.findViewById(R.id.editPhone);

        dialog.setView(register_layout);

        //set Button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                btnRegister.setEnabled(false);

                //check validation
                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    btnRegister.setEnabled(true);
                    return;
                }

                if (TextUtils.isEmpty(editName.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter your Full name", Snackbar.LENGTH_SHORT).show();
                    btnRegister.setEnabled(true);
                    return;
                }

                if (editPassword.getText().toString().length()<6) {
                    Snackbar.make(rootLayout, "Password too short", Snackbar.LENGTH_SHORT).show();
                    btnRegister.setEnabled(true);
                    return;
                }

                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter your password", Snackbar.LENGTH_SHORT).show();
                    btnRegister.setEnabled(true);
                    return;
                }

                if (TextUtils.isEmpty(editPhone.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter your Phone Number", Snackbar.LENGTH_SHORT).show();
                    btnRegister.setEnabled(true);
                    return;
                }

                final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(MainActivity.this).build();
                waitingDialog.show();

                //registering a new user into an app
                auth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                //save the user to db
                                Rider rider = new Rider();
                                rider.setEmail(editEmail.getText().toString());
                                rider.setName(editName.getText().toString());
                                rider.setPhone(editPhone.getText().toString());
                                rider.setPassword(editPassword.getText().toString());

                                //using email to key
                                riders.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(rider)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                waitingDialog.dismiss();
                                                Snackbar.make(rootLayout, "Registered successfully", Snackbar.LENGTH_SHORT).show();
                                                return;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                waitingDialog.dismiss();
                                                Snackbar.make(rootLayout, "Failed to register "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                                btnRegister.setEnabled(true);
                                                return;
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout, "Error "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
