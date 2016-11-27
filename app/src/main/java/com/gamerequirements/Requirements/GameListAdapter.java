package com.gamerequirements.Requirements;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamerequirements.MyApplication;
import com.gamerequirements.R;

import java.util.List;

/**
 * Created by v3rt1ag0 on 10/14/16.
 */

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.MyViewHolder>
{
    List<Information> info;
    Context context;

    GameListAdapter(List<Information> info)
    {
        this.info=info;
        context= MyApplication.getContext();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_card, parent, false);
        MyViewHolder pvh = new MyViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Information in=info.get(position);
        holder.title_TV.setText(in.title);
    }

    @Override
    public int getItemCount()
    {
        return info.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        LinearLayout linearLayout;
        TextView title_TV;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.Game_list_LL);
            title_TV= (TextView) itemView.findViewById(R.id.name_of_game);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.Game_list_LL:
                    linearLayout.getContext().startActivity(new Intent(linearLayout.getContext(),Requirement_content.class).putExtra("id",info.get(getAdapterPosition()).id).putExtra("title",info.get(getAdapterPosition()).title));
                    break;
            }
        }
    }
}
