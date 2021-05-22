package com.abhishek.cambridgeapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.cambridgeapp1.Models.Student;
import com.abhishek.cambridgeapp1.Models.StudentSubjectEnrolled;
import com.abhishek.cambridgeapp1.Models.Subjects;
import com.abhishek.cambridgeapp1.Models.SubjectsEnrolled;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>EditStudentActivity</code> is the first page the user come's to when he Log's in for the first time.
 * The user can also come to this page when the <b>Edit Profile button</b> on the bottom right corner of <code>StudentProfileFragment</code> is clicked.
 * This page is used to select the Branch, Sem and Section along with the chosen electives every 6 months i.e., every semester.
 *
 * Once the submit button is clicked:
 * -The previous <i>document</i> in <i>STUDENT collection</i> in the <i>previous-Branch, previous-Sem path</i> and <i>SUBJECTS_ENROLLED collection in Firestore</i> of that particular Student are deleted,
 * -The new values of <i>branch, sem, section</i> of the student are updated in the user's <i>document</i> in <i>STUDENT_LIST collection</i>
 * -A new <i>document</i> in <i>STUDENT collection</i> in the <i>new-Branch, new-Sem path</i> is created with the <i>SUBJECTS_ENROLLED subcollection</i> having all the subjects the student has enrolled for.
 */
public class EditStudentActivity extends AppCompatActivity {

    Button btnSubmit;
    ImageButton ibSelectSubjects;

    TextView tvStudentProfileName, tvStudentUSN, tvStudentEmail;
    TextView tvPhone, tvDOB;

    Spinner spinSem, spinSection, spinElective1, spinElective2, spinBranch;
    RelativeLayout rlElectives;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    ProgressBar progressBar;

    private ArrayAdapter<String> branchArray;
    private ArrayAdapter<String> semesterArray;
    private ArrayAdapter<String> sectionArray;
    private Student student;
    private StudentSubjectEnrolled studentSubjectEnrolled;
    private List<String> subjectEnrolledNames;

    private List<String> subjects1;
    private List<String> subjects2;
    private List<Subjects> electiveSubjects1;
    private List<Subjects> electiveSubjects2;
    private static List<SubjectsEnrolled> subjectsEnrolledList;
    public static List<SubjectsEnrolled> dummy;
    private String branch = "";
    private String sem = "";
    private String section = "";

    private String previousBranch = "";
    private String previousSem = "";
    private String previousSection = "";

    private static final int GOOD = 1;
    private static final int BAD = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        btnSubmit = findViewById(R.id.btnSubmitStudent_Edit);
        tvStudentProfileName = findViewById(R.id.tvStudentProfileName_Edit);
        tvStudentEmail = findViewById(R.id.tvStudentProfileEmail_Edit);
        tvStudentUSN = findViewById(R.id.tvStudentProfileUSN_Edit);
        tvPhone = findViewById(R.id.tvStudentProfilePhone_Edit);
        tvDOB = findViewById(R.id.tvStudentProfileDOB_Edit);
        ibSelectSubjects = findViewById(R.id.ibSelectSubjects);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        spinSem = findViewById(R.id.spinSem);
        spinSection = findViewById(R.id.spinSection);
        spinBranch = findViewById(R.id.spinBranch);
        spinElective1 = findViewById(R.id.spinElective1);
        spinElective2 = findViewById(R.id.spinElective2);
        rlElectives = findViewById(R.id.rlElectives);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        spinSem.setEnabled(false);
        spinSection.setEnabled(false);
        spinBranch.setEnabled(false);

        spinElective1.setEnabled(false);
        spinElective2.setEnabled(false);
        rlElectives.setVisibility(View.INVISIBLE);

        loadProfile();

        semesterArray = new ArrayAdapter<>(this, R.layout.spinner_item,
                getResources().getStringArray(R.array.sem_array));
        spinSem.setAdapter(semesterArray);

        sectionArray = new ArrayAdapter<>(this, R.layout.spinner_item,
                getResources().getStringArray(R.array.section_array));
        spinSection.setAdapter(sectionArray);

        branchArray = new ArrayAdapter<>(this, R.layout.spinner_item,
                getResources().getStringArray(R.array.branch_array));
        spinBranch.setAdapter(branchArray);


        spinSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sem = spinSem.getSelectedItem().toString().trim();
                rlElectives.setVisibility(View.INVISIBLE);
                subjectsEnrolledList.clear();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sem = student.getSem();

            }
        });

        spinBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                branch = spinBranch.getSelectedItem().toString().trim();
                rlElectives.setVisibility(View.INVISIBLE);
                subjectsEnrolledList.clear();

                if (branch.equals("BSC"))
                {
                    semesterArray = new ArrayAdapter<>(EditStudentActivity.this, R.layout.spinner_item,
                            getResources().getStringArray(R.array.sem_basic_science));
                    spinSem.setAdapter(semesterArray);

                    sectionArray = new ArrayAdapter<>(EditStudentActivity.this, R.layout.spinner_item,
                            getResources().getStringArray(R.array.section_basic_science));
                    spinSection.setAdapter(sectionArray);
                }
                else {

                    semesterArray = new ArrayAdapter<>(EditStudentActivity.this, R.layout.spinner_item,
                            getResources().getStringArray(R.array.sem_array));
                    spinSem.setAdapter(semesterArray);

                    sectionArray = new ArrayAdapter<>(EditStudentActivity.this, R.layout.spinner_item,
                            getResources().getStringArray(R.array.section_array));
                    spinSection.setAdapter(sectionArray);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                branch = student.getBranch();

            }
        });

        spinSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                section = spinSection.getSelectedItem().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                section = student.getSection();
            }
        });


        ibSelectSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Loading elective subjects for " + branch + " " + sem + " Sem", GOOD, Toast.LENGTH_SHORT);
                loadSubjects();

            }
        });

        subjectEnrolledNames = new ArrayList<>();
        subjectsEnrolledList = new ArrayList<>(8);
        dummy = new ArrayList<>();

        spinElective1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                for (Subjects sub : electiveSubjects1)
                {
                    if (spinElective1.getSelectedItem().toString().equals(sub.getSubjectName()))
                    {
                        SubjectsEnrolled subEnroll = new SubjectsEnrolled(sub.getSubjectId(),
                                sub.getSubjectName(), 0, 0, 0, 0);
                        subjectsEnrolledList.set(0, subEnroll);
                        subjectEnrolledNames.add(subEnroll.getSubjectId());
                        Log.d("\t\tSubjects added :: ", subEnroll.getSubjectName() + " :: " + subjectsEnrolledList.get(0));
                        Log.d("\t\tArray size ::: ", String.valueOf(subjectsEnrolledList.size()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinElective2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                for (Subjects sub : electiveSubjects2)
                {
                    if (spinElective2.getSelectedItem().toString().equals(sub.getSubjectName()))
                    {
                        SubjectsEnrolled subEnroll = new SubjectsEnrolled(sub.getSubjectId(),
                                sub.getSubjectName(), 0, 0, 0, 0);
                        subjectsEnrolledList.set(1, subEnroll);
                        subjectEnrolledNames.add(subEnroll.getSubjectId());
                        Log.d("\t\tSubjects added :: ", subEnroll.getSubjectName() + " :: " + subjectsEnrolledList.get(1));
                        Log.d("\t\tArray size ::: ", String.valueOf(subjectsEnrolledList.size()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (branch.isEmpty() || sem.isEmpty() || section.isEmpty() || subjectsEnrolledList.size() < 2)
                {
                    showToast("Enter all fields", BAD, Toast.LENGTH_SHORT);
                }
                else {

                    if (subjectsEnrolledList.get(0).getSubjectId().equals(subjectsEnrolledList.get(1).getSubjectId()))
                    {
                        showToast("Select different Electives", BAD, Toast.LENGTH_SHORT);
                    }
                    else {
                        student.setBranch(branch);
                        student.setSem(sem);
                        student.setSection(section);
                        subjectsEnrolledList.addAll(dummy);

                        progressBar.setVisibility(View.VISIBLE);
                        showToast("Updating", GOOD, Toast.LENGTH_SHORT);
                        //deletePreviousStudent();
                        updateProfile();
                        setStudent();
                    }
                }
            }
        });

    }

    /**
     * This method is called to load the value's of the user from <i>STUDENT_LIST in Firestore</i> into a <code>Student</code> class Object.
     * This object can then be used to set the fields on the page.
     */
    private void loadProfile() {

        firestore.collection("STUDENT_LIST").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful())
                        {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            student = documentSnapshot.toObject(Student.class);

                            tvStudentProfileName.setText(student.getName());
                            tvStudentUSN.setText(student.getUsn());
                            tvStudentEmail.setText(student.getEmail());
                            tvPhone.setText(student.getPhone());
                            tvDOB.setText(student.getDob());


                            if (!student.getBranch().isEmpty())
                            {
                                previousBranch = student.getBranch();
                            }

                            if (!student.getSem().isEmpty())
                            {
                                previousSem = student.getSem();
                            }

                            if (!student.getSection().isEmpty())
                            {
                                previousSection = student.getSection();
                            }

                            spinSem.setEnabled(true);
                            spinSection.setEnabled(true);
                            spinBranch.setEnabled(true);

                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    /**
     * On selecting the Branch and semester, the elective subjects as per that branch and that semester  are loaded on to the Spinners.
     * The default/common subject's i.e., Non-elective subjects are loaded onto a different Array named <i>dummy</i>.
     */
    public void loadSubjects()
    {
        dummy.clear();
        subjectsEnrolledList.clear();
        subjectsEnrolledList.add(0,null);
        subjectsEnrolledList.add(1,null);
        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("DEPARTMENT").document(branch.toLowerCase() + "_branch")
                .collection("CLASS").document(sem + "_sem")
                .collection("SUBJECTS")
//        firestore.collection("/DEPARTMENT/cse_branch/CLASS/6_sem/SUBJECTS")
                //.whereEqualTo("isElective", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        subjects1 = new ArrayList<>();
                        subjects2 = new ArrayList<>();
                        electiveSubjects1 = new ArrayList<>();
                        electiveSubjects2 = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            for (QueryDocumentSnapshot ds : queryDocumentSnapshots)
                            {
                                Subjects subject = ds.toObject(Subjects.class);

                                if (subject.isIsElective())
                                {
                                    if (subject.getElectiveSet() == 1)
                                    {
                                        electiveSubjects1.add(subject);
                                        Log.d("\t\tElectives 1 :: ", subject.getSubjectName());
                                        subjects1.add(subject.getSubjectName());
                                    }
                                    else
                                    {
                                        electiveSubjects2.add(subject);
                                        Log.d("\t\tElectives 2 :: ", subject.getSubjectName());
                                        subjects2.add(subject.getSubjectName());
                                    }
                                }
                                else {
                                    SubjectsEnrolled subEnroll = new SubjectsEnrolled(subject.getSubjectId(),
                                            subject.getSubjectName(), 0, 0, 0, 0);
                                    dummy.add(subEnroll);
                                    subjectEnrolledNames.add(subEnroll.getSubjectId());
                                }
                            }

                            ArrayAdapter<String> electiveArray1 = new ArrayAdapter<String>(EditStudentActivity.this,
                                    R.layout.spinner_item, subjects1.toArray(new String[subjects1.size()]));
                            ArrayAdapter<String> electiveArray2 = new ArrayAdapter<String>(EditStudentActivity.this,
                                    R.layout.spinner_item, subjects2.toArray(new String[subjects2.size()]));
                            spinElective1.setAdapter(electiveArray1);
                            spinElective2.setAdapter(electiveArray2);

                            spinElective1.setEnabled(true);
                            spinElective2.setEnabled(true);
                            rlElectives.setVisibility(View.VISIBLE);

                        }
                        else {
                            showToast("No subjects", BAD, Toast.LENGTH_SHORT);
                            subjectsEnrolledList.clear();

                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error" + e.toString(), BAD, Toast.LENGTH_SHORT);
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("Error :::: ", e.toString());
                    }
                });

    }

    /**
     * This method recursively detele's the documents and subcollections in the Lower Level <i>STUDENT collection in Firestore</i>
     * i.e., Deletes the document of the student and the subject's he/she had enrolled before.
     * Firebase's cloud function's are used to achieve this.
     */
    public void deletePreviousStudent() //Need the firebase cloud functions
    {
        if (previousBranch.isEmpty() || previousSem.isEmpty() || previousSection.isEmpty())
        {
            Log.d("Deleting not needed! ::", "Branch, sem, section is empty");
        }
        else {

            String path = "/DEPARTMENT/"+previousBranch+"_branch/CLASS/"+previousSem+"_sem/SECTION/"+previousSection+"STUDENTS/{"+mAuth.getCurrentUser().getUid()+"}";
            Map<String, Object> data = new HashMap<>();
            data.put("path", path);

//            HttpsCallableReference deleteFn =
//                    FirebaseFunctions.getInstance().getHttpsCallable("recursiveDelete");
//            deleteFn.call(data)
//                    .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
//                        @Override
//                        public void onSuccess(HttpsCallableResult httpsCallableResult) {
//                            Log.d("Successfully deleted ::", previousBranch + " " + previousSem + " " + mAuth.getCurrentUser().getUid());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("UNSUCCESFUL :: ", previousBranch + " " + previousSem + " " + mAuth.getCurrentUser().getUid() + e.toString());
//                        }
//                    });

            //Comment this once you get Cloud functions
            firestore.document(data.get("path").toString())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Log.d("Successfully deleted ::", previousBranch + " " + previousSem + " " + mAuth.getCurrentUser().getUid());
                            }
                            else {
                                Log.d("UNSUCCESFUL :: ", previousBranch + " " + previousSem + " " + mAuth.getCurrentUser().getUid() + task.getException());
                            }
                        }
                    });

        }
    }

    /**
     * Update the values of <i>branch, sem, section</i> in the user's <i>document</i> in <i>STUDENT_LIST collection</i>
     */
    public void updateProfile()
    {
        firestore.collection("STUDENT_LIST").document(mAuth.getCurrentUser().getUid())
                .set(student, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       showToast("Profile Updated", GOOD, Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error Updating!!" + e.toString(), BAD, Toast.LENGTH_SHORT);
                        Log.d("Error :::: ", e.toString());
                    }
                });
    }

    /**
     * Create a <i>document</i> in the new <i>branch, sem, STUDENTS path</i> in <i>firestore</i> and add the <i>SUBJECT_ENROLLED subcollection</i>
     * to that <i>document</i>.
     * <i>SUBJECTS_ENROLLED collection</i> will now have the all the subject's including the chosen electives and the common subject's that are assigned
     * to that particular sem of that particular branch.
     */
    public void setStudent()
    {
        studentSubjectEnrolled = new StudentSubjectEnrolled(student.getUsn(), student.getEmail(), student.getName(),
                student.getGender(), student.getImageUrl(), subjectEnrolledNames);

        firestore.collection("DEPARTMENT").document(branch.toLowerCase() + "_branch")
                .collection("CLASS").document(sem + "_sem")
                .collection("SECTION").document(section)
                .collection("STUDENTS")
                .document(student.getUsn())
                .set(studentSubjectEnrolled)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        for (final SubjectsEnrolled subsEnroll : subjectsEnrolledList)
                        {
                            firestore.collection("DEPARTMENT").document(branch.toLowerCase() + "_branch")
                                    .collection("CLASS").document(sem + "_sem")
                                    .collection("SECTION").document(section)
                                    .collection("STUDENTS")
                                    .document(student.getUsn())
                                    .collection("SUBJECTS_ENROLLED")
                                    .document(subsEnroll.getSubjectId())
                                    .set(subsEnroll)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                showToast(subsEnroll.getSubjectName() + "added!!", GOOD, Toast.LENGTH_SHORT);
                                            }
                                        }
                                    });
                        }
                        startActivity(new Intent(EditStudentActivity.this,
                                com.abhishek.cambridgeapp1.MainStudentActivity.class )
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error setting profile" + e.toString(), BAD, Toast.LENGTH_SHORT);
                        Log.d("Error :::: ", e.toString());
                    }
                });

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