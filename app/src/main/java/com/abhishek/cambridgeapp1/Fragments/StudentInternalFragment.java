package com.abhishek.cambridgeapp1.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.cambridgeapp1.Adapters.InternalAdapter;
import com.abhishek.cambridgeapp1.Models.Student;
import com.abhishek.cambridgeapp1.Models.Subjects;
import com.abhishek.cambridgeapp1.Models.SubjectsEnrolled;
import com.abhishek.cambridgeapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentInternalFragment extends Fragment {

    private RecyclerView rvInternals;
    private InternalAdapter internalAdapter;
    private List<SubjectsEnrolled> subjectsEnrolledList;
    List<Subjects> subjectsList;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    Student student;

    private ProgressBar progressBar;
    private RadioGroup rgInternals;
    private String internalNumber = "internal1";

    private static final int GOOD = 1;
    private static final int BAD = -1;

    public StudentInternalFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_internal, container, false);

        rvInternals = view.findViewById(R.id.rvInternalsStudent);
        progressBar = view.findViewById(R.id.progressBar);
        rgInternals = view.findViewById(R.id.rgInternals);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.INVISIBLE);

        rvInternals.setHasFixedSize(true);
        rvInternals.setLayoutManager(new LinearLayoutManager(getContext()));

        rgInternals.check(R.id.rbInternal1);

        subjectsEnrolledList = new ArrayList<>();
        subjectsList = new ArrayList<>();
        internalAdapter = new InternalAdapter(getContext(), subjectsEnrolledList, internalNumber, subjectsList);
        rvInternals.setAdapter(internalAdapter);

        //getStudent();
        getSubjectsEnrolled();

        rgInternals.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                if (radioButton != null)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    internalNumber = radioButton.getTag().toString().trim();
                    internalAdapter = new InternalAdapter(getContext(), subjectsEnrolledList, internalNumber, subjectsList);
                    rvInternals.setAdapter(internalAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });

        return view;
    }


    private void getSubjectsEnrolled() {

        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("STUDENT_LIST").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot ds = task.getResult();
                            student = ds.toObject(Student.class);

                          firestore.collection("DEPARTMENT").document(student.getBranch().toLowerCase() + "_branch")
                              .collection("CLASS").document(student.getSem() + "_sem")
                              .collection("SECTION").document(student.getSection())
                              .collection("STUDENTS").document(student.getUsn())
                              .collection("SUBJECTS_ENROLLED")
//                            firestore.collection("DEPARTMENT").document("cse_branch")
//                                    .collection("CLASS").document("6_sem")
//                                    .collection("STUDENTS").document(mAuth.getCurrentUser().getUid())
//                                    .collection("SUBJECTS_ENROLLED")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            subjectsEnrolledList.clear();
                                            subjectsList.clear();
                                            for (DocumentSnapshot ds : queryDocumentSnapshots)
                                            {
                                                //subjectsEnrolledList.add(ds.toObject(SubjectsEnrolled.class));

                                                final SubjectsEnrolled subEnr = ds.toObject(SubjectsEnrolled.class);

                                                if (subEnr != null){

                                                    firestore.collection("DEPARTMENT")
                                                            .document(student.getBranch().toLowerCase() + "_branch")
                                                            .collection("CLASS")
                                                            .document(student.getSem() + "_sem")
                                                            .collection("SUBJECTS")
                                                            .document(subEnr.getSubjectId())
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful())
                                                                    {
                                                                        DocumentSnapshot doc = task.getResult();
                                                                        Subjects sub = doc.toObject(Subjects.class);

                                                                        if (sub != null){

                                                                            subjectsEnrolledList.add(subEnr);
                                                                            subjectsList.add(sub);

                                                                            Log.d("SubjectsEnrolled Added", subjectsEnrolledList.size() + "::" +
                                                                                    subEnr.getSubjectId() + subEnr.getSubjectName());
                                                                            Log.d("Subjectist added ",  subjectsList.size() + ":;" + sub.getSubjectId());
                                                                            internalAdapter.notifyDataSetChanged();
                                                                            progressBar.setVisibility(View.INVISIBLE);

                                                                        }
                                                                        else {
                                                                            showToast(subEnr.getSubjectName() + " not found", BAD, Toast.LENGTH_SHORT);
                                                                        }

                                                                    }
                                                                }
                                                            });

                                                }
                                                else {
                                                    showToast("No subjects Enrolled ", BAD, Toast.LENGTH_SHORT);
                                                }


                                            }
                                            //internalAdapter.notifyDataSetChanged();
                                            //progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast("Something went wrong " + e.toString(), BAD, Toast.LENGTH_SHORT);
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                        }
                    }
                });
    }

    public void showToast(String text, int emoji, int duration){

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) getActivity().findViewById(R.id.toast_root));

        TextView toastText = layout.findViewById(R.id.toast_message);
        ImageView toastImage = layout.findViewById(R.id.toast_emoji);

        toastText.setText(text);
        if (emoji == GOOD){
            toastImage.setImageResource(R.drawable.ic_emoji_ok);
        }else {
            toastImage.setImageResource(R.drawable.ic_emoji_bad);
        }

        Toast toast = new Toast(getContext());
        toast.setDuration(duration);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


}