package com.gamerequirements.Requirements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.squareup.picasso.Picasso;

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
        holder.date_TV.setText("Release Date: "+in.date);
        holder.genre_TV.setText("Genre: "+in.genre.replace("\n",", "));
        holder.summart_TV.setText(in.summary);
        String url= Singelton.getImageurl()+in.id;
        Log.d("custom",url);
        Picasso.with(context)
                .load(url)
                 // optional
                //.error(R.mipmap.ic_launcher)      // optional
                //.resize(400, 400)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount()
    {
        return info.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        LinearLayout linearLayout;
        TextView title_TV,genre_TV,date_TV,summart_TV;
        ImageView imageView;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.Game_list_LL);
            title_TV= (TextView) itemView.findViewById(R.id.name_of_game);
            genre_TV= (TextView) itemView.findViewById(R.id.genre);
            date_TV= (TextView) itemView.findViewById(R.id.date);
            imageView= (ImageView) itemView.findViewById(R.id.image);
            summart_TV=(TextView) itemView.findViewById(R.id.summary);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.Game_list_LL:
                    Bundle bundle=new Bundle();
                    bundle.putInt("id",info.get(getAdapterPosition()).id);
                    bundle.putString("title",info.get(getAdapterPosition()).title);
                    bundle.putString("genre",info.get(getAdapterPosition()).genre);
                    bundle.putString("date",info.get(getAdapterPosition()).date);
                    bundle.putString("summary",info.get(getAdapterPosition()).summary);
                    linearLayout.getContext().startActivity(new Intent(linearLayout.getContext(),Requirement_content.class).putExtras(bundle));
                    break;
            }
        }
    }
}
