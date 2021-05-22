package com.abhishek.cambridgeapp1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.cambridgeapp1.Models.NoticeBoardItem;
import com.abhishek.cambridgeapp1.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.ViewHolder>{

    Context mContext;
    List<NoticeBoardItem> noticeBoardItemList;

    private static final int GOOD = 1;
    private static final int BAD = -1;
    RelativeLayout root;

    public NoticeBoardAdapter(Context mContext, List<NoticeBoardItem> noticeBoardItemList) {
        this.mContext = mContext;
        this.noticeBoardItemList = noticeBoardItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_board_list, parent, false);
        root = view.findViewById(R.id.toast_root);
        return new NoticeBoardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final NoticeBoardItem notice = noticeBoardItemList.get(position);

        holder.tvFileName.setText(notice.getName());
        holder.tvFileDescription.setText(notice.getDescription());
        holder.tvFileAuthor.setText(notice.getAuthor());

        String semClass = notice.getBranch() + " " + notice.getSem();
        holder.tvClass.setText(semClass);

        if (!notice.isIsImage())
            holder.rlImageCropper.setVisibility(View.GONE);
        else {
            if (notice.getFileUrl() != null && !notice.getFileUrl().isEmpty())
            {
                Picasso.get().load(notice.getFileUrl()).placeholder(R.drawable.logo_loadings).into(holder.ivIfImage);
                holder.rlImageCropper.setVisibility(View.VISIBLE);
            }
        }

        switch (notice.getExtension()){

            case "pdf" : holder.ivFileType.setImageResource(R.drawable.logo_pdf);
                break;

            case "docx" : holder.ivFileType.setImageResource(R.drawable.logo_doc);
                break;

            case "png" : holder.ivFileType.setImageResource(R.drawable.logo_png);
                break;

        }

        holder.rlGoToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go View File
                if (!notice.getFileUrl().isEmpty() && notice.getFileUrl() != null)
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notice.getFileUrl())));
                else
                    showToast("Unable to open File", BAD, Toast.LENGTH_SHORT);
            }
        });

        holder.ivIfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!notice.getFileUrl().isEmpty() && notice.getFileUrl() != null)
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notice.getFileUrl())));
                else
                    showToast("Unable to open File", BAD, Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noticeBoardItemList.size();
    }

    public void filterList(List<NoticeBoardItem> filterList){
        noticeBoardItemList = filterList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rlGoToFile;
        public RelativeLayout rlImageCropper;
        public ImageView ivFileType, ivIfImage;
        public TextView tvFileName, tvFileDescription, tvFileAuthor, tvClass;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivIfImage = itemView.findViewById(R.id.ivIfImage);
            rlImageCropper = itemView.findViewById(R.id.rlImageCropper);
            rlGoToFile = itemView.findViewById(R.id.rlGoToFile);
            ivFileType = itemView.findViewById(R.id.ivFileFormat);
            tvFileName = itemView.findViewById(R.id.tvNoticeName);
            tvFileDescription = itemView.findViewById(R.id.tvNoticeData);
            tvFileAuthor = itemView.findViewById(R.id.tvNoticeAuthor);
            tvClass = itemView.findViewById(R.id.tvClassSem_Notice);

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
