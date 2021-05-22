package com.abhishek.cambridgeapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.cambridgeapp1.Models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * <code>RegisterActivity</code> provides the functionality to Register a user.
 * The registration is done using <i>Firebase</i>.
 * The page ask's for fields like <i>USN, Name, Phone, Email, Date of birth and Gender</i> which are permanent fields of the user
 * and that can never be changed.
 * The Gender field is only a means to set the user's initial profile image.
 * The page ask's for a password which can be reset later on in the <code>LoginActivity</code> page if the user forget's their password.
 */
public class RegisterActivity extends AppCompatActivity {

    EditText etId, etFullname, etEmail, etPassword, etPhone, etDOB;
    Button btnRegister;

    RadioGroup rgGender;
    ProgressBar pb;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore_temporary;

    private static final int GOOD = 1;
    private static final int BAD = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final int[] buttonClicks = {0};

        etId = findViewById(R.id.etId_Register);
        etFullname = findViewById(R.id.etFullname_Register);
        etEmail = findViewById(R.id.etEmail_Register);
        etPhone = findViewById(R.id.etPhone_Register);
        etPassword = findViewById(R.id.etPassword_Register);
        etDOB = findViewById(R.id.etDOB_Register);

        btnRegister = findViewById(R.id.btnRegister_Register);

        rgGender = findViewById(R.id.rgGender);
        rgGender.clearCheck();

        final String[] gender = {""};

        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        firestore_temporary = FirebaseFirestore.getInstance();

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);

                if (null != rb) {
                    gender[0] = rb.getTag().toString().trim().toLowerCase();
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonClicks[0]++;

                String id = etId.getText().toString().trim();
                String fullname = etFullname.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String dob = etDOB.getText().toString().trim();

                if (id.isEmpty() || fullname.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()
                        || gender[0].isEmpty() || dob.isEmpty())
                {
                    showToast("Enter all fields!!", BAD, Toast.LENGTH_SHORT);
                    buttonClicks[0] = 0;
                }
                else {

                    if (email.endsWith("@cambridge.edu.in") && email.matches(".*\\d.*")) //Student email always has numbers
                    {
                        if (password.length() >= 6)
                        {
                            if (buttonClicks[0] == 1){
                                showToast(getResources().getString(R.string.make_sure),GOOD, Toast.LENGTH_LONG);
                            }
                            else {

                                registerUser(email, password, id, fullname, phone, gender[0], dob);
                            }
                        }
                        else {

                            Drawable d= ContextCompat.getDrawable(RegisterActivity.this,R.drawable.ic_error);
                            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                            etPassword.setError("Password length should be greater than or equal to 6", d);
                            buttonClicks[0] = 0;
                        }
                    }
                    else {
                        Drawable d= ContextCompat.getDrawable(RegisterActivity.this,R.drawable.ic_error);
                        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                        etEmail.setError("Email should be registered with \"cambridge.edu.in\"", d);
                        buttonClicks[0] = 0;
                    }
                }
            }
        });

    }

    /**
     * This method is used to register a user in the app using Firebase authentication.
     * Once the user is registered, a confirmation email is sent to the email so that only valid user's are able to sign in to the app.
     * The method also creates a <i>document</i> in <i>Firestore's STUDENT_LIST Collection</i> but with empty fields for
     * <i>branch, sem, section and imageUrl</i>.
     *
     * @param email -Email entered by the user.
     * @param password -Password entered by the user
     * @param id -USN entered by the user.
     * @param name -Name of the user.
     * @param phone -Phone of the user.
     * @param gender -Gender of the user
     * @param dob -Date of birth of the user.
     */
    private void registerUser(final String email, String password, final String id, final String name, final String phone,
                              final String gender, final String dob) {

        pb.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(final AuthResult authResult) {

                        mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful())
                                        {

                                            Student student = new Student(id, name, email, phone, gender, dob, "");
                                            firestore_temporary.collection("STUDENT_LIST")
                                                    .document(mAuth.getCurrentUser().getUid())
                                                    .set(student)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            showToast("Please check your email for verification link!!", GOOD, Toast.LENGTH_LONG);
                                                            pb.setVisibility(View.INVISIBLE);
                                                            startActivity(new Intent(RegisterActivity.this,
                                                                    com.abhishek.cambridgeapp1.StartActivity.class)
                                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            showToast(e.getMessage(), BAD, Toast.LENGTH_SHORT);
                                                            pb.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                        }
                                        else
                                        {
                                            showToast("Failed : " +task.getException().getMessage(), BAD, Toast.LENGTH_SHORT);
                                            pb.setVisibility(View.INVISIBLE);
                                        }

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pb.setVisibility(View.INVISIBLE);
                        showToast("Registering failed!! " + e.getMessage(), BAD, Toast.LENGTH_SHORT);

                    }
                });
    }

    /**
     *Redirects to <code>LoginActivity</code> when clicked on <i>Already Registered</i>.
     */
    public void goToLoginActivity(View view)
    {
        startActivity(new Intent(RegisterActivity.this,
                com.abhishek.cambridgeapp1.LoginActivity.class));
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