package com.gamerequirements.Notification;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gamerequirements.MyApplication;
import com.gamerequirements.R;

import java.util.List;


/**
 * Created by v3rt1ag0 on 12/31/17.
 */

public class OuterAdapter extends RecyclerView.Adapter<OuterAdapter.MyViewHolder>
{
    private List<OuterCardInformation> outerlist;
    private Context context;

    OuterAdapter(List<OuterCardInformation> outerlist)
    {
        this.outerlist = outerlist;
        context = MyApplication.getContext();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outer_card_view, parent, false);
        MyViewHolder pvh = new MyViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        OuterCardInformation outerCardInformation = outerlist.get(position);
        holder.data.setText(outerCardInformation.data); //DATE
        holder.gamesadded.setText(outerCardInformation.count);
        InnerAdapter innerAdapter = new InnerAdapter(outerCardInformation.innerCardList);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(innerAdapter);
    }


    @Override
    public int getItemCount()
    {
        return outerlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView data,gamesadded;
        RecyclerView recyclerView;

         MyViewHolder(View itemView)
        {
            super(itemView);
            data = itemView.findViewById(R.id.data);
            gamesadded= itemView.findViewById(R.id.games_added);
            recyclerView = itemView.findViewById(R.id.innerRecycler);
        }
    }
}
