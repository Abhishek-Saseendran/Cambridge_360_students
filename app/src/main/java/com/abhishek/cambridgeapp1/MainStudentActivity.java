package com.abhishek.cambridgeapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abhishek.cambridgeapp1.Fragments.FeedbackFragment;
import com.abhishek.cambridgeapp1.Fragments.NoticeBoardFragment;
import com.abhishek.cambridgeapp1.Fragments.SettingsFragment;
import com.abhishek.cambridgeapp1.Fragments.StudentAttendanceFragment;
import com.abhishek.cambridgeapp1.Fragments.StudentInternalFragment;
import com.abhishek.cambridgeapp1.Fragments.StudentProfileFragment;
import com.abhishek.cambridgeapp1.Models.Controls;
import com.abhishek.cambridgeapp1.Models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <code>MainStudentActivity</code> class is the one that control's different pages of the Student Activities
 * like Profile, Internals and Attendance, etc.
 * The <b>selectorFragment</b> will replace the needed fragment according to the option clicked on the Navigation drawer.
 */
public class MainStudentActivity extends AppCompatActivity {

    LinearLayout llInitial;
    NavigationView navViewStudentActivity;
    Fragment selectorFragment;
    DrawerLayout dlStudentActivity;

    CircleImageView civNavProfileImage;
    TextView tvNavUsername;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    Student student;
    Controls control;

    boolean isInProfileFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        Toolbar toolbar = findViewById(R.id.toolbar_Student);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.toobar_signout : //SignOut with Firebase
                        mAuth.signOut();
                        startActivity(new Intent(MainStudentActivity.this,
                                com.abhishek.cambridgeapp1.StartActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        break;
                }
                return true;

            }
        });

        llInitial = findViewById(R.id.llInitial_Student);
        navViewStudentActivity = findViewById(R.id.navViewStudentActivity);
        dlStudentActivity = findViewById(R.id.dlActivityStudent);

        civNavProfileImage = navViewStudentActivity.getHeaderView(0).findViewById(R.id.civNavProfilePic);
        tvNavUsername = navViewStudentActivity.getHeaderView(0).findViewById(R.id.tvNavUsername);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        dlStudentActivity.animate().alpha(0f).setDuration(1);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        animation.setDuration(1500);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MainStudentActivity.MyAnimationListener());
        llInitial.setAnimation(animation);

        getControls();

        navViewStudentActivity.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.nav_profile :
                        selectorFragment = new StudentProfileFragment();
                        isInProfileFragment = true;
                        break;

                    case R.id.nav_internals :
                        selectorFragment = new StudentInternalFragment();
                        isInProfileFragment = false;
                        break;

                    case R.id.nav_attendance :
                        selectorFragment = new StudentAttendanceFragment();
                        isInProfileFragment = false;
                        break;

                    case R.id.nav_noticeboard:
                        selectorFragment = new NoticeBoardFragment();
                        isInProfileFragment = false;
                        break;

                    case R.id.nav_lost_and_found:
                        break;

                    case R.id.nav_feedback:
                        selectorFragment = new FeedbackFragment();
                        isInProfileFragment = false;
                        break;

                    case R.id.nav_map:
                        isInProfileFragment = false;
                        break;

                    case R.id.nav_settings:
                        selectorFragment = new SettingsFragment();
                        isInProfileFragment = false;
                        break;
                }

                if (selectorFragment != null)
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_Student, selectorFragment)
                            .commit();
                }

                dlStudentActivity.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainStudentActivity.this, dlStudentActivity, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlStudentActivity.addDrawerListener(toggle);
        toggle.syncState();

        Bundle intent = getIntent().getExtras();
        if (intent != null)
        {
            //
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_Student, new StudentProfileFragment()).commit();
            isInProfileFragment = true;
        }

        firestore.collection("STUDENT_LIST")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            student = documentSnapshot.toObject(Student.class);

                            if (student.getImageUrl().isEmpty())
                            {
                                if (student.getGender().equals("male"))
                                {
                                    Picasso.get().load(R.drawable.ic_boy).into(civNavProfileImage);
                                }
                                else {
                                    Picasso.get().load(R.drawable.ic_girl).into(civNavProfileImage);
                                }
                            }
                            else {
                                Picasso.get().load(student.getImageUrl()).into(civNavProfileImage);
                            }

                            tvNavUsername.setText(student.getName());
                        }
                    }
                });

        firestore.collection("STUDENT_LIST").document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String url = value.get("imageUrl").toString();
                        if (!url.isEmpty())
                            Picasso.get().load(url).into(civNavProfileImage);
                    }
                });

    }

    private void getControls() {

        firestore.collection("ADMIN_OPTIONS").document("controls")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;

                        if (value != null){

                            control = value.toObject(Controls.class);
                            MenuItem feedback = navViewStudentActivity.getMenu().findItem(R.id.nav_feedback);
                            feedback.setEnabled(control.isEnableFeedback());
                            feedback.setVisible(control.isEnableFeedback());

                        }
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
            dlStudentActivity.animate().alpha(1f).setDuration(1000);
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

        if (dlStudentActivity.isDrawerOpen(GravityCompat.START))
        {
            dlStudentActivity.closeDrawer(GravityCompat.START);
        }
        else if (!isInProfileFragment){

            selectorFragment = new StudentProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_Student, selectorFragment).commit();
            isInProfileFragment = true;
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_signout, menu);
        return true;

    }



}