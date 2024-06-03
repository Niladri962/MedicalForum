package com.example.smedicalapp;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    // Variables
    private Context context;
    private List<Medical> MedicalList;

    public MyAdapter(Context context, List<Medical> medicalList) {
        this.context = context;
        this.MedicalList = medicalList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.medical_row, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Medical currentMedical = MedicalList.get(position);

        holder.title.setText(currentMedical.getTitle());
        holder.Info.setText(currentMedical.getInfo());
        holder.name.setText(currentMedical.getUserName());

        String imageUrl = currentMedical.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(
                currentMedical.getTimeAdded().getSeconds()*1000
        );
        holder.dateAdded.setText(timeAgo);


        // Glide Library to display the image
        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return MedicalList.size();
    }


    // View Holder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, Info, dateAdded, name;
        public ImageView image, shareButton;
        public String userId, username;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.journal_title_list);
            Info= itemView.findViewById(R.id.journal_thought_list);
            dateAdded = itemView.findViewById(R.id.journal_timestamp_list);

            image = itemView.findViewById(R.id.journal_image_list);
            name = itemView.findViewById(R.id.journal_row_username);

            shareButton = itemView.findViewById(R.id.journal_row_share_button);

            shareButton.setOnClickListener(v -> {

                // onClick()
                // Sharing the Post....
            });

        }
    }
}