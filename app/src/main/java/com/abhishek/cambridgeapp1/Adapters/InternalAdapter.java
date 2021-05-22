package com.abhishek.cambridgeapp1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.cambridgeapp1.Models.Subjects;
import com.abhishek.cambridgeapp1.Models.SubjectsEnrolled;
import com.abhishek.cambridgeapp1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class InternalAdapter extends RecyclerView.Adapter<InternalAdapter.ViewHolder>{

    private Context mContext;
    private List<SubjectsEnrolled> subjectsEnrolledList;
    private List<Subjects> subjectsList;
    private String internalNumber;

    FirebaseUser firebaseUser;

    public InternalAdapter(Context mContext, List<SubjectsEnrolled> subjectsEnrolledList, String internalNumber, List<Subjects> subjectsList) {
        this.mContext = mContext;
        this.subjectsEnrolledList = subjectsEnrolledList;
        this.internalNumber = internalNumber;
        this.subjectsList = subjectsList;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.internal_item, parent, false);
        return new InternalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubjectsEnrolled subjectEnrolled = subjectsEnrolledList.get(position);
        Subjects subject = subjectsList.get(position);
        holder.tvSubjectCode.setText(subjectEnrolled.getSubjectId());
        holder.tvSubjectName.setText(subjectEnrolled.getSubjectName());

        if (internalNumber.equals("internal1"))
        {
            holder.tvSubjectScore.setText(String.valueOf(subjectEnrolled.getInternal1()));
        }
        else if (internalNumber.equals("internal2"))
        {
            holder.tvSubjectScore.setText(String.valueOf(subjectEnrolled.getInternal2()));
        }
        else if (internalNumber.equals("internal3"))
        {
            holder.tvSubjectScore.setText(String.valueOf(subjectEnrolled.getInternal3()));
        }
        else { //Average
            float avg = ( subjectEnrolled.getInternal1() + subjectEnrolled.getInternal2() + subjectEnrolled.getInternal3() ) / 3;
            double average = Math.round(avg * 100) /100.0;
            holder.tvSubjectScore.setText(average+"");
        }

        double percent = Float.parseFloat(holder.tvSubjectScore.getText().toString()) / subject.getTotalMarks() * 100;

        if (percent < 35)//Float.parseFloat(holder.tvSubjectScore.getText().toString()) < 20.0)
        {
            holder.tvSubjectScore.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
        }
        if (percent > 80)//Float.parseFloat(holder.tvSubjectScore.getText().toString()) > 40.0)
        {
            holder.tvSubjectScore.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        }

    }

    @Override
    public int getItemCount() {
        return subjectsEnrolledList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvSubjectCode, tvSubjectName, tvSubjectScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubjectCode = itemView.findViewById(R.id.tvCode_internalItem);
            tvSubjectName = itemView.findViewById(R.id.tvSubject_internalItem);
            tvSubjectScore = itemView.findViewById(R.id.tvScore_internalItem);

        }
    }
}
