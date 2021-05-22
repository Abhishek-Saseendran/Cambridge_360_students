package com.abhishek.cambridgeapp1.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.abhishek.cambridgeapp1.MainStudentActivity;
import com.abhishek.cambridgeapp1.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;


public class SettingsFragment extends Fragment {

    Button btnReview;
    ReviewManager manager;
    ReviewInfo reviewInfo;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnReview = view.findViewById(R.id.btnReviewGoogle);
        manager =  ReviewManagerFactory.create(getContext());

        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    reviewInfo = task.getResult();
                    Log.d("\t\tUGHH somethig", "OK");

                } else {
                    // There was some problem, continue regardless of the result.
                    Log.d("\t\tUGHH somethig", "NOPE");
                }
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
                flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }
                });

            }
        });

        return view;
    }
}