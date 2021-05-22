package com.abhishek.cambridgeapp1.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.cambridgeapp1.Adapters.NoticeBoardAdapter;
import com.abhishek.cambridgeapp1.Models.NoticeBoardItem;
import com.abhishek.cambridgeapp1.Models.Student;
import com.abhishek.cambridgeapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class NoticeBoardFragment extends Fragment {

    ProgressBar progressBar;
    EditText etSearch;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    RecyclerView rvNoticeBoard;
    NoticeBoardAdapter noticeBoardAdapter;
    List<NoticeBoardItem> noticeBoardItemList;

    Student student;

    private static final int GOOD = 1;
    private static final int BAD = -1;

    public NoticeBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice_board, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        etSearch = view.findViewById(R.id.etSearchForNotices);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        rvNoticeBoard = view.findViewById(R.id.rvNoticeBoard);
        rvNoticeBoard.setHasFixedSize(true);
        rvNoticeBoard.setLayoutManager(new LinearLayoutManager(getContext()));

        getStudent();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

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
                            if (documentSnapshot != null)
                            {
                                student = documentSnapshot.toObject(Student.class);

                                noticeBoardItemList = new ArrayList<>();
                                getNotices();
                            }
                        }
                        else {
                            showToast("Error " + task.getException(), BAD, Toast.LENGTH_SHORT);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                });
    }

    private void getNotices() {

        noticeBoardItemList.clear();
        firestore.collection("DEPARTMENT").document(student.getBranch().toLowerCase() + "_branch")
                .collection("CLASS").document(student.getSem() + "_sem")
                .collection("NOTICE_BOARD")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            QuerySnapshot qds = task.getResult();

                            if (qds != null && qds.size() != 0 ){

                                noticeBoardItemList = qds.toObjects(NoticeBoardItem.class);
                                noticeBoardAdapter = new NoticeBoardAdapter(getContext(), noticeBoardItemList);
                                rvNoticeBoard.setAdapter(noticeBoardAdapter);
                                noticeBoardAdapter.notifyDataSetChanged();

                            }
                            else {
                                showToast("No Notices yet!!", BAD, Toast.LENGTH_SHORT);
                            }
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                        else {
                            showToast("Error " + task.getException(), BAD, Toast.LENGTH_SHORT);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private void filter(String text) {

        ArrayList<NoticeBoardItem> filterList = new ArrayList<>();

        for (NoticeBoardItem item : noticeBoardItemList){
            if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getBranch().toLowerCase().contains(text.toLowerCase())
                    || item.getSem().toLowerCase().contains(text.toLowerCase()) || item.getAuthor().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        noticeBoardAdapter.filterList(filterList);

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