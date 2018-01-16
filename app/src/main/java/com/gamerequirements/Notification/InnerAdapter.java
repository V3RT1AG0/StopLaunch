package com.gamerequirements.Notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamerequirements.R;
import com.gamerequirements.Requirements.Requirement_content;
import com.gamerequirements.MyApplication;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.MyViewHolder>
{
    List<InnerCardInformation> info;
    Context context;

    InnerAdapter(List<InnerCardInformation> info)
    {
        this.info = info;
        context = MyApplication.getContext();
    }

    @Override
    public com.gamerequirements.Notification.InnerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inner_card_view, parent, false);
        com.gamerequirements.Notification.InnerAdapter.MyViewHolder pvh = new com.gamerequirements.Notification.InnerAdapter.MyViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(com.gamerequirements.Notification.InnerAdapter.MyViewHolder holder, int position)
    {
        InnerCardInformation innerInfo = info.get(position);
        holder.title.setText(innerInfo.name);
        String url = MyApplication.getImageurl() + innerInfo.gid;
        Picasso.with(context)
                .load(url)
                .into(holder.imageView);

    }


    @Override
    public int getItemCount()
    {
        return info.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title;
        ImageView imageView;

        MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.inner_title);
            imageView = itemView.findViewById(R.id.inner_image);
        }

        @Override
        public void onClick(View v)
        {
            Bundle bundle = new Bundle();
            bundle.putInt("id", Integer.parseInt(info.get(getAdapterPosition()).gid));
            v.getContext().startActivity(new Intent(v.getContext(), Requirement_content.class).putExtras(bundle));
        }
    }
}