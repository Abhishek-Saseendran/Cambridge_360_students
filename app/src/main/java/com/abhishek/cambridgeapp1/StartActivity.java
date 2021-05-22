package com.abhishek.cambridgeapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.cambridgeapp1.Models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * @author Abhishek Saseendran 1CD17CS008
 *
 * <code>StartActivity</code> is the first page of the the app.
 * If the user has yet to sign up or login, this page provides the functionalities to proceed to the login / register activity.
 * If the user has already logged in and hasn't signed out, the contents in the page (other than the progress bar) are not visible and will
 * be redirected to the user's Profile page :- <code>StudentProfileFragment</code>
 */

public class StartActivity extends AppCompatActivity {

    LinearLayout llInitial;
    Button btnLogin, btnRegister;

    DrawerLayout dlStartActivity;
    NavigationView navViewStartActivity;
    LinearLayout llLogin;
    Toolbar toolbar;
    ProgressBar progressBar;

    /**
     * @param sayHelloCount is used to display different messages on clicking the <i>Say Hi</i> Button on the Navigation drawer menu.
     */
    private int sayHelloCount = 0;

    private static final int GOOD = 1;
    private static final int BAD = -1;

    /**
     * The on start method is used to redirect to the user's profile page <b>if</b> the user has already logged in <b>and</b> validated their email.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
        {
            FirebaseFirestore.getInstance().collection("STUDENT_LIST")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Student student = documentSnapshot.toObject(Student.class);

                            if (student != null){

                                if (student.getBranch().isEmpty() || student.getSem().isEmpty() || student.getSection().isEmpty())
                                {

                                    //Send to edit profile
                                    Toast.makeText(StartActivity.this, "New User!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(StartActivity.this,
                                            EditStudentActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }
                                else {

                                    //Old user, Send to Profile page
                                    startActivity(new Intent(StartActivity.this,
                                            MainStudentActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    finish();
                                }


                            }
                            else {

                                init();

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StartActivity.this, "Please register again!! Talk to the admin!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else {
            init();
        }

    }

    private void init() {

        progressBar.setVisibility(View.INVISIBLE);
        llInitial.setVisibility(View.VISIBLE);
        dlStartActivity.animate().alpha(0f).setDuration(1);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        animation.setDuration(1500);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        llInitial.setAnimation(animation);

        toolbar.setVisibility(View.VISIBLE);
        llLogin.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        llInitial = findViewById(R.id.llInitial);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        dlStartActivity = findViewById(R.id.dlActivityStart);
        navViewStartActivity = findViewById(R.id.navViewStartActivity);
        llLogin = findViewById(R.id.llLogin);

        progressBar = findViewById(R.id.progressBar);

        llInitial.setVisibility(View.INVISIBLE);
        llLogin.setVisibility(View.INVISIBLE);

        navViewStartActivity.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_hi :

                        if (sayHelloCount == 0)
                        {
                            sayHelloCount++;
                            showToast("Hello", GOOD, Toast.LENGTH_SHORT);
                        }
                        else if (sayHelloCount == 1)
                        {
                            sayHelloCount++;
                            showToast("Hey Whassup!?", GOOD, Toast.LENGTH_SHORT);
                        }
                        else {
                            sayHelloCount = 0;
                            showToast("Okay stop clicking me and go make some friends!! Tata.. XD", GOOD, Toast.LENGTH_LONG);
                        }

                        break;

                    case R.id.nav_website:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cambridge.edu.in")));
                        break;

                    case R.id.nav_contact:
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+918618225882")));
                        break;

                    case R.id.nav_map:
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("geo:0,0?q=Cambridge Institute of Technology, Bangalore")));
                        break;
                }

                dlStartActivity.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(StartActivity.this, dlStartActivity, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlStartActivity.addDrawerListener(toggle);
        toggle.syncState();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StartActivity.this,
                        com.abhishek.cambridgeapp1.LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StartActivity.this,
                        com.abhishek.cambridgeapp1.RegisterActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

    }

    /**
     * <code>MyAnimationListener</code> class is used to do operations on or after an animation is called.
     */
    private class MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            llInitial.clearAnimation();
            llInitial.setVisibility(View.INVISIBLE);
            dlStartActivity.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * This method is used to close the navigation drawer on back button press.
     * If the drawer is already closed, then the app closes when the back button is pressed.
     */
    @Override
    public void onBackPressed() {

        if (dlStartActivity.isDrawerOpen(GravityCompat.START))
        {
            dlStartActivity.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
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