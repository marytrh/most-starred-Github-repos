package com.example.maryem.starredrepos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by maryem on 22/12/18.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder>{

    private Context context;
    private List<Repo> list;

    public RepoAdapter(Context context, List<Repo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new RepoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoAdapter.ViewHolder holder, int position) {
        Repo repo = list.get(position);
        holder.textName.setText(repo.getRepo_name());
        holder.textDesc.setText(repo.getRepo_description());
        holder.textOwner.setText(repo.getRepo_owner_name());
        holder.textNum.setText(repo.getRepo_number_stars());
        holder.pic.setImageResource(R.drawable.pic);
        holder.star.setImageResource(R.drawable.stare);
    }

 

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textName, textDesc, textOwner, textNum;
        public ImageView star , pic ;
        public ViewHolder(View item) {
            super(item);
            textName = itemView.findViewById(R.id.name);
            textDesc = itemView.findViewById(R.id.description);
            textOwner = itemView.findViewById(R.id.owner);
            textNum = itemView.findViewById(R.id.num_stars);
            pic = itemView.findViewById(R.id.pic);
            star = itemView.findViewById(R.id.star);
        }
    }
}
