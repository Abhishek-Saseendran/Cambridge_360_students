package com.abhishek.cambridgeapp1.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.cambridgeapp1.Models.Feedbacks;
import com.abhishek.cambridgeapp1.Models.Teacher;
import com.abhishek.cambridgeapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder>{

    ProgressBar progressBar;
    Context mContext;
    List<HashMap<String, String>> subjectTeachersList;
    List<Feedbacks> ratingList;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    private static final int GOOD = 1;
    private static final int BAD = -1;
    RelativeLayout root;

    public FeedbackAdapter(Context mContext, List<HashMap<String, String>> subjectTeachersList, ProgressBar progressBar) {
        this.mContext = mContext;
        this.subjectTeachersList = subjectTeachersList;
        this.progressBar = progressBar;
        ratingList = new ArrayList<>(Collections.nCopies(subjectTeachersList.size(), new Feedbacks()));

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feedback_list, parent, false);
        root = view.findViewById(R.id.toast_root);
        return new FeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final HashMap<String, String> feedBack = subjectTeachersList.get(position);

        final float[] rate1 = new float[1];
        final float[] rate2 = new float[1];
        final float[] rate3 = new float[1];
        final float[] rate4 = new float[1];
        final float[] rate5 = new float[1];
        final float[] rate6 = new float[1];
        final float[] rate7 = new float[1];
        final float[] rate8 = new float[1];
        final float[] rate9 = new float[1];
        final float[] rate10 = new float[1];

        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("TEACHER_LIST").document(feedBack.get("teacherUID"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null){

                                Teacher teacher = documentSnapshot.toObject(Teacher.class);

                                holder.tvTeacherName.setText(teacher.getName());
                                holder.tvTeacherEmail.setText(teacher.getEmail());
                                holder.tvSubjectName.setText(feedBack.get("subjectId"));
                                if (teacher.getImageUrl().isEmpty()){

                                    if (teacher.getGender().equals("male")){
                                        Picasso.get().load(R.drawable.ic_boy).into(holder.civTeacherImage);
                                    }
                                    else {
                                        Picasso.get().load(R.drawable.ic_girl).into(holder.civTeacherImage);
                                    }

                                }
                                else {
                                    Picasso.get().load(teacher.getImageUrl()).into(holder.civTeacherImage);
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });

        holder.rbQuestion1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate1[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate2[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate3[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate4[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate5[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion6.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate6[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion7.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate7[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion8.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate8[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion9.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate9[0] = ratingBar.getRating();
            }
        });

        holder.rbQuestion10.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate10[0] = ratingBar.getRating();
            }
        });

        holder.ibSaveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rate1[0] != 0 && rate2[0] != 0 && rate3[0] != 0 && rate4[0] != 0 && rate5[0] != 0
                        && rate6[0] != 0 && rate7[0] != 0 && rate8[0] != 0 && rate9[0] != 0 && rate10[0] != 0){

                    saveFeedback(rate1[0], rate2[0], rate3[0], rate4[0], rate5[0], rate6[0], rate7[0], rate8[0], rate9[0], rate10[0],
                            feedBack.get("teacherUID"), feedBack.get("subjectId"));

                    holder.ibSaveFeedback.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorGrey)));
                    holder.ibSaveFeedback.setEnabled(false);
                }
                else {
                    showToast("Enter all fields", BAD, Toast.LENGTH_SHORT);
                }
            }
        });

    }

    private void saveFeedback(final float r1, final float r2, final float r3, final float r4, final float r5,
                              final float r6, final float r7, final float r8, final float r9, final float r10,
                              final String teacherUID, final String subjectId) {

        progressBar.setVisibility(View.VISIBLE);

        final Feedbacks[] rating = {new Feedbacks()};

        final DocumentReference feedbackDocument = firestore.collection("TEACHER_LIST").document(teacherUID)
                                                        .collection("FEEDBACKS").document(subjectId);

        firestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                progressBar.setVisibility(View.VISIBLE);
                DocumentSnapshot documentSnapshot = transaction.get(feedbackDocument);

                rating[0] = documentSnapshot.toObject(Feedbacks.class);

                if (rating[0] != null){

                    rating[0].setNo_of_feedbacks(rating[0].getNo_of_feedbacks() + 1);
                    rating[0].setFeedbackScore1(rating[0].getFeedbackScore1() + r1);
                    rating[0].setFeedbackScore2(rating[0].getFeedbackScore2() + r2);
                    rating[0].setFeedbackScore3(rating[0].getFeedbackScore3() + r3);
                    rating[0].setFeedbackScore4(rating[0].getFeedbackScore4() + r4);
                    rating[0].setFeedbackScore5(rating[0].getFeedbackScore5() + r5);
                    rating[0].setFeedbackScore6(rating[0].getFeedbackScore6() + r6);
                    rating[0].setFeedbackScore7(rating[0].getFeedbackScore7() + r7);
                    rating[0].setFeedbackScore8(rating[0].getFeedbackScore8() + r8);
                    rating[0].setFeedbackScore9(rating[0].getFeedbackScore9() + r9);
                    rating[0].setFeedbackScore10(rating[0].getFeedbackScore10() + r10);

                    transaction.set(feedbackDocument, rating[0]);

                }
                return null;
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast("Done!!", GOOD, Toast.LENGTH_SHORT);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error " + e.toString(), BAD, Toast.LENGTH_SHORT);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });


    }

    @Override
    public int getItemCount() {
        return subjectTeachersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        //super.setHasStableIds(hasStableIds);
        super.setHasStableIds(true);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView civTeacherImage;
        public TextView tvTeacherName, tvTeacherEmail, tvSubjectName;
        public RatingBar rbQuestion1, rbQuestion2, rbQuestion3, rbQuestion4, rbQuestion5,
                            rbQuestion6, rbQuestion7, rbQuestion8, rbQuestion9, rbQuestion10;
        public ImageButton ibSaveFeedback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civTeacherImage = itemView.findViewById(R.id.civTeacherImage_feedbackList);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName_feedbackList);
            tvTeacherEmail = itemView.findViewById(R.id.tvTeacherEmail_feedbackList);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName_feedbackList);
            rbQuestion1 = itemView.findViewById(R.id.ratBarQuestion1);
            rbQuestion2 = itemView.findViewById(R.id.ratBarQuestion2);
            rbQuestion3 = itemView.findViewById(R.id.ratBarQuestion3);
            rbQuestion4 = itemView.findViewById(R.id.ratBarQuestion4);
            rbQuestion5 = itemView.findViewById(R.id.ratBarQuestion5);
            rbQuestion6 = itemView.findViewById(R.id.ratBarQuestion6);
            rbQuestion7 = itemView.findViewById(R.id.ratBarQuestion7);
            rbQuestion8 = itemView.findViewById(R.id.ratBarQuestion8);
            rbQuestion9 = itemView.findViewById(R.id.ratBarQuestion9);
            rbQuestion10 = itemView.findViewById(R.id.ratBarQuestion10);
            ibSaveFeedback = itemView.findViewById(R.id.ibSaveFeedBack_feedbackList);

        }
    }

    public void showToast(String text, int emoji, int duration){

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) root);

        TextView toastText = layout.findViewById(R.id.toast_message);
        ImageView toastImage = layout.findViewById(R.id.toast_emoji);

        toastText.setText(text);
        if (emoji == GOOD){
            toastImage.setImageResource(R.drawable.ic_emoji_ok);
        }else {
            toastImage.setImageResource(R.drawable.ic_emoji_bad);
        }

        Toast toast = new Toast(mContext);
        toast.setDuration(duration);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
