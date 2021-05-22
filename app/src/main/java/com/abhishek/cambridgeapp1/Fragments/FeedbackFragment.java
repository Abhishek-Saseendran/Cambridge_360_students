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
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.cambridgeapp1.Adapters.FeedbackAdapter;
import com.abhishek.cambridgeapp1.Models.Student;
import com.abhishek.cambridgeapp1.Models.StudentSubjectEnrolled;
import com.abhishek.cambridgeapp1.Models.Subjects;
import com.abhishek.cambridgeapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FeedbackFragment extends Fragment {

    ProgressBar progressBar;
    RecyclerView rvFeedbacks;
    FeedbackAdapter feedbackAdapter;

    List<String> subjectsEnrolledList;
    List<HashMap<String, String>> subjectTeacherList;
    Student student;
    StudentSubjectEnrolled studentSubjectEnrolled;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    private static final int GOOD = 1;
    private static final int BAD = -1;

    public FeedbackFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        rvFeedbacks = view.findViewById(R.id.rvFeedbacks);
        rvFeedbacks.setHasFixedSize(true);
        rvFeedbacks.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        subjectsEnrolledList = new ArrayList<>();
        subjectTeacherList = new ArrayList<>();
        getStudent();

        return view;
    }

    private void getStudent() {

        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("STUDENT_LIST").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null){
                                student = documentSnapshot.toObject(Student.class);
                                getStudentSubjectEnrolled();
                            }
                        }
                    }
                });
    }

    private void getStudentSubjectEnrolled() {

        firestore.collection("DEPARTMENT").document(student.getBranch().toLowerCase() + "_branch" )
                .collection("CLASS").document(student.getSem() + "_sem")
                .collection("SECTION").document(student.getSection())
                .collection("STUDENTS").document(student.getUsn())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot != null){

                                studentSubjectEnrolled = documentSnapshot.toObject(StudentSubjectEnrolled.class);
                                subjectsEnrolledList = studentSubjectEnrolled.getSubjectEnrolledNames();
                                getSubjectTeachers();
                            }
                        }
                    }
                });
    }

    private void getSubjectTeachers() {

        for (final String sub : subjectsEnrolledList){

            firestore.collection("DEPARTMENT").document(student.getBranch().toLowerCase() + "_branch")
                    .collection("CLASS").document(student.getSem() + "_sem")
                    .collection("SUBJECTS").document(sub)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()){

                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot != null){

                                    Subjects temp = documentSnapshot.toObject(Subjects.class);

                                    for (final String teacherId : temp.getHandledBy().get(student.getSection())){

                                        if (teacherId != null){

                                            subjectTeacherList.add(new HashMap<String, String>(){{
                                                put("teacherUID", teacherId);
                                                put("subjectId", sub);
                                            }});
                                            Log.d("\t\t Size :: ", subjectTeacherList.size() + "");
                                            Log.d("\t\tAdded :: ", sub + " " + teacherId);
                                        }

                                    }

                                    feedbackAdapter = new FeedbackAdapter(getContext(), subjectTeacherList, progressBar);
                                    rvFeedbacks.setAdapter(feedbackAdapter);
                                    feedbackAdapter.notifyDataSetChanged();

                                }

                            }

                        }
                    });
        }

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