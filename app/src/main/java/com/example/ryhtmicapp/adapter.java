package com.example.ryhtmicapp;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.Myholder> {
    private List<list>listItems;
    private Context context;

    public adapter(List<list> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_musci_view,parent,false);
                return new Myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.Myholder holder, int position) {

        list listItem=listItems.get(position);
                holder.title.setText(listItem.getSongname());
        holder.album.setText(listItem.getSongname());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public class Myholder extends RecyclerView.ViewHolder{


        public TextView title;
        public TextView album;
        public TextView favtxt;
        public ImageView fav;
        public ImageView image;
        public TextView votescount;


        public Myholder(@NonNull View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.songname);
            album=(TextView) itemView.findViewById(R.id.createdAt);
            favtxt=(TextView) itemView.findViewById(R.id.favtxt);
            fav= (ImageView) itemView.findViewById(R.id.fav);
            image= (ImageView) itemView.findViewById(R.id.songimage);


        }
    }
}
