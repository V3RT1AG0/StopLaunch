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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by v3rt1ag0 on 12/31/17.
 */

public class OuterAdapter extends RecyclerView.Adapter<OuterAdapter.MyViewHolder>
{
    private List<OuterCardInformation> outerlist;
    private Context context;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    OuterAdapter(List<OuterCardInformation> outerlist)
    {
        this.outerlist = outerlist;
        context = MyApplication.getContext();
        dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
        calendar = Calendar.getInstance();
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
        holder.data.setText(getDate(outerCardInformation.data));
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
            data = (TextView) itemView.findViewById(R.id.data);
            gamesadded= (TextView) itemView.findViewById(R.id.games_added);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.innerRecycler);
        }
    }

    String getDate(String data)
    {
        Date date;
        String month = null;
        try
        {
            date = dateFormat.parse(data);  // here we are getting date in format "Tue, 12 Jan 2016 09:40:07 GMT"
            calendar.setTime(date);
            month = new SimpleDateFormat("MMMM").format(date);

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return calendar.get(Calendar.DATE) + " " + month.substring(0,3) + " '" + Integer.toString(calendar.get(Calendar.YEAR)).substring(2,4);
    }
}
