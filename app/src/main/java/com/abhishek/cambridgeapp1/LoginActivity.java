package com.abhishek.cambridgeapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.cambridgeapp1.Models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * <code>LoginActivity</code> provides functionality for a user to login to his profile.
 * This page accepts an email and password that was registered and checks to see if the credentials matches.
 * If the user has forgotten their password, then the page also provides an option to reset password when clicked on the <i>Forgot password</i> dialog.
 * This authentication and password reset is taken care by <i>Firebase</i>.
 */
public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvForgotPassword;

    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore_temporary;

    private static final int GOOD = 1;
    private static final int BAD = -1;

    /**
     * Sign's in a user using Firebase Authentication.
     * Check's if the user is signing in for the first time.
     * If yes, then redirects the user to <code>EditStudentActivity</code> page.
     * If no, then redirects the user to <code>MainActivityStudent</code> page.
     *
     * This redirection is decided by checking if the <b>branch, sem, section</b> values in the user's
     * <i>document in STUDENT_LIST in Firestore</i> are empty or not.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail_Login);
        etPassword = findViewById(R.id.etPassword_Login);
        btnLogin = findViewById(R.id.btnLogin_Login);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        firestore_temporary = FirebaseFirestore.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty())
                {
                    showToast("Enter all fields!!", BAD, Toast.LENGTH_SHORT);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    if (mAuth.getCurrentUser().isEmailVerified())
                                    {

                                        if (mAuth.getCurrentUser().getEmail().contains("@cambridge.edu.in") &&
                                            mAuth.getCurrentUser().getEmail().matches(".*\\d.*")) //Student email always contains a number
                                        {
                                            firestore_temporary.collection("STUDENT_LIST")
                                                    .document(mAuth.getCurrentUser().getUid())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                            Student student = documentSnapshot.toObject(Student.class);

                                                            if (student != null){

                                                                if (student.getBranch().isEmpty() || student.getSem().isEmpty() || student.getSection().isEmpty())
                                                                {
                                                                    showToast("New User!!", GOOD, Toast.LENGTH_SHORT);
                                                                    startActivity(new Intent(LoginActivity.this,
                                                                            EditStudentActivity.class)
                                                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    finish();
                                                                }
                                                                else
                                                                {
                                                                    startActivity(new Intent(LoginActivity.this,
                                                                            MainStudentActivity.class)
                                                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    finish();
                                                                }


                                                            }
                                                            else {

                                                                mAuth.signOut();
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                showToast("Error", BAD, Toast.LENGTH_SHORT);

                                                            }

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                           showToast("Please register again!! Talk to the admin!!", BAD, Toast.LENGTH_SHORT);
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                        }
                                        else {
                                            showToast("Invalid email!!", BAD, Toast.LENGTH_SHORT);
                                        }

                                    }
                                    else{
                                        showToast("Please verify Email", BAD, Toast.LENGTH_SHORT);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast(e.getMessage(), BAD, Toast.LENGTH_SHORT);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                }
            }
        });


    }

    /**
     *Sends a link to the registered email to change the password.
     */
    public void forgotPassword(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        tvForgotPassword.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorGrey));

        etPassword.setVisibility(View.GONE);

        if (etEmail.getText().toString().isEmpty())
        {
            showToast("Please enter your email in the email field", GOOD, Toast.LENGTH_SHORT);
            progressBar.setVisibility(View.INVISIBLE);
        }
        else {

            //Send Link to reset Password
            mAuth.sendPasswordResetEmail(etEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast("Please go to the email link we have sent you to reset your password!!", GOOD, Toast.LENGTH_LONG);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            else {
                                showToast("Failed!! Unregistered user!!", BAD, Toast.LENGTH_SHORT);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

        }

    }

    /**
     *Redirects to <code>RegisterActivity</code> when clicked on <i>Not yet Registered</i>.
     */
    public void goToRegisterActivity(View view)
    {
        startActivity(new Intent(LoginActivity.this,
                com.abhishek.cambridgeapp1.RegisterActivity.class));
        finish();
    }

    public void showToast(String text, int emoji, int duration){

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_root));

        TextView toastText = layout.findViewById(R.id.toast_message);
        ImageView toastImage = layout.findViewById(R.id.toast_emoji);

        toastText.setText(text);
        if (emoji == GOOD){
            toastImage.setImageResource(R.drawable.ic_emoji_ok);
        }else {
            toastImage.setImageResource(R.drawable.ic_emoji_bad);
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(duration);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}