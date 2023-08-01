package com.msbte.modelanswerpaper;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>
{

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final model model) {

        holder.name.setText(model.getFilename());

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.download.getContext(),download.class);

                intent.putExtra("filename",model.getFilename());
                intent.putExtra("fileurl",model.getFileurl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.download.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.candle_iteam,parent,false);
        return  new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {

        TextView name;
        Button download;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);

            name=itemView.findViewById(R.id.mpname);
            download=itemView.findViewById(R.id.downloadfile2);


        }
    }
}
