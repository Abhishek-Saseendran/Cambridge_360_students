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
import java.util.Map;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{

    private Context mContext;
    private List<SubjectsEnrolled> subjectsEnrolledList;
    private List<Subjects> subjectsList;
    String section = "";
    FirebaseUser firebaseUser;

    public AttendanceAdapter(Context mContext, List<SubjectsEnrolled> subjectsEnrolledList, List<Subjects> subjectsList, String section) {
        this.mContext = mContext;
        this.subjectsEnrolledList = subjectsEnrolledList;
        this.subjectsList = subjectsList;
        this.section = section;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.attendance_item, parent, false);
        return new AttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubjectsEnrolled subjectEnroll = subjectsEnrolledList.get(position);
        Subjects subject = subjectsList.get(position);

        holder.tvSubject.setText(subjectEnroll.getSubjectId());
        holder.tvObtained.setText(String.valueOf((int)subjectEnroll.getAttendance()));

        Map<String, Integer> attendance = subject.getTotalAttendance();
        holder.tvTotal.setText(attendance.get(section) + "");

        double percent;
        if (attendance.get(section) != 0)
        {
            percent = ( subjectEnroll.getAttendance() / attendance.get(section)) * 100.0 ;
        }
        else {
            percent = 100.00;
        }
        double percentage = Math.round(percent * 100) / 100.0;
        String percentageString = String.valueOf(percentage)+ mContext.getResources().getString(R.string.percentage);
        holder.tvPercentage.setText(percentageString);

        if (percentage < 85.0)
        {
            holder.tvPercentage.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
        }
        else if (percentage > 95.0)
        {
            holder.tvPercentage.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        }

    }

    @Override
    public int getItemCount() {
        return subjectsEnrolledList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvSubject, tvObtained, tvTotal, tvPercentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubject = itemView.findViewById(R.id.tvSubject_attendanceItem);
            tvObtained = itemView.findViewById(R.id.tvObtained_attendanceItem);
            tvTotal = itemView.findViewById(R.id.tvTotalAttendance_attendanceItem);
            tvPercentage = itemView.findViewById(R.id.tvPercentageAttendance_attendanceItem);

        }
    }

}
