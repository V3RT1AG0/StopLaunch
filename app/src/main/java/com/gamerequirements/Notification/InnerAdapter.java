package com.gamerequirements.Notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.MyViewHolder>
{
    List<InnerCardInformation> info;
    Context context;

    InnerAdapter(List<InnerCardInformation> info)
    {
        this.info=info;
        context= MyApplication.getContext();
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
        InnerCardInformation innerInfo =  info.get(position);
        holder.title.setText(innerInfo.name);
        String url = Singelton.getImageurl()+innerInfo.gid;
        Picasso.with(context)
                .load(url)
                .into(holder.imageView);

    }


    @Override
    public int getItemCount()
    {
        return info.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        ImageView imageView;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            title  = (TextView) itemView.findViewById(R.id.inner_title);
            imageView = (ImageView) itemView.findViewById(R.id.inner_image);
        }

    }
}