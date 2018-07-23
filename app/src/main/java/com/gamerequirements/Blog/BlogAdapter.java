package com.gamerequirements.Blog;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.gamerequirements.Utils.DateTimeUtil;
import com.gamerequirements.Utils.YoutubeFullScreenActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

/**
 * Created by v3rt1ag0 on 7/7/18.
 */

class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyCommonViewHolder>
{
    List<Information> info;
    Context context;
    private Lifecycle lifecycle;
    static SparseArray cats, tags;

    BlogAdapter(List<Information> info, Lifecycle lifecycle, SparseArray cats, SparseArray tags)
    {

        this.info = info;
        context = MyApplication.getContext();
        this.lifecycle = lifecycle;
        BlogAdapter.cats = cats;
        BlogAdapter.tags = tags;
    }

    BlogAdapter(List<Information> info, Lifecycle lifecycle)
    {

        this.info = info;
        context = MyApplication.getContext();
        this.lifecycle = lifecycle;
    }

    @Override
    public MyCommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        switch (viewType)
        {
            case 2:
                return new MyViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blog_card_post, parent, false));
            case 5:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_card_video, parent, false);
                final YouTubePlayerView youTubePlayerView = v.findViewById(R.id.youtube_player_view);
                youTubePlayerView.getPlayerUIController().showFullscreenButton(true);
                lifecycle.addObserver(youTubePlayerView);
                return new MyVideoViewHolder(v, youTubePlayerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyCommonViewHolder holder, int position)
    {
        final Information bloginfo = info.get(position);
        holder.title.setText(bloginfo.title);
        holder.date.setText(DateTimeUtil.formatToYesterdayOrToday(bloginfo.date));
        switch (holder.getItemViewType())
        {
            case 2:

                MyViewHolder holder0 = (MyViewHolder) holder;
             /* holder0.title.setText(bloginfo.title);
                holder0.date.setText(DateTimeUtil.formatToYesterdayOrToday(bloginfo.date));*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder0.subtitle.setText(Html.fromHtml(bloginfo.subtitle, Html.FROM_HTML_MODE_COMPACT));
                else
                    holder0.subtitle.setText(Html.fromHtml(bloginfo.subtitle));

                String url = bloginfo.imgvideurl;
                Log.d("triggered1", url);
                Picasso.with(context)
                        .load(url)
                        .into(holder0.imageView);
                createCategoryandTagButtons(bloginfo,holder0);
                break;

            case 5:
                MyVideoViewHolder holder1 = (MyVideoViewHolder) holder;
               /* holder1.title.setText(bloginfo.title);
                holder1.date.setText(DateTimeUtil.formatToYesterdayOrToday(bloginfo.date));*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder1.subtitle.setText(Html.fromHtml(bloginfo.subtitle, Html.FROM_HTML_MODE_COMPACT));
                else
                    holder1.subtitle.setText(Html.fromHtml(bloginfo.subtitle));
                Log.d("triggered2", bloginfo.imgvideurl);
                //Singelton.setYouTubePlayer(holder1.youtubePlayer);
                holder1.cueVideo(bloginfo.imgvideurl);
                createCategoryandTagButtons(bloginfo,holder1);
                break;
        }

    }

    private void createCategoryandTagButtons(final Information bloginfo, MyCommonViewHolder holder)
    {
        TextView catsButton = new TextView(context);
        catsButton.setText((String) cats.get(bloginfo.category));
        catsButton.setTextColor(context.getResources().getColor(R.color.colorAccent));
        catsButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.cats.addView(catsButton);
        catsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.getContext().startActivity(new Intent(context, BlogListActivity.class).putExtra("cats", bloginfo.category));
            }
        });

        for (int i = 0; i < bloginfo.tags.length(); i++)
        {
            try
            {
                Log.d("taggggs", tags.toString() + cats.toString());
                final int tagId = bloginfo.tags.getInt(i);
                TextView tagsButton = new TextView(context);
                Log.d("taggggs", "" + tags.get(tagId));
                tagsButton.setText(" #" + (String) tags.get(tagId));
                tagsButton.setTextColor(context.getResources().getColor(R.color.colorAccent));
                tagsButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.tags.addView(tagsButton);
                tagsButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        view.getContext().startActivity(new Intent(context, BlogListActivity.class).putExtra("tags", tagId));
                    }
                });
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        Information bloginfo = info.get(position);
        return bloginfo.category;
    }


    @Override
    public int getItemCount()
    {
        return info.size();
    }

    class MyCommonViewHolder extends RecyclerView.ViewHolder{

        // Super class for both video and post view holder. Components common to both the cards will go here
        TextView title, subtitle,date;
        LinearLayout cats, tags;

        public MyCommonViewHolder(View itemView)
        {
            super(itemView);
            tags = itemView.findViewById(R.id.tags);
            cats = itemView.findViewById(R.id.cats);
            title = itemView.findViewById(R.id.title_post);
            subtitle = itemView.findViewById(R.id.subTitle_post);
            date = itemView.findViewById(R.id.post_date);
            tags.setVisibility(View.VISIBLE);
            cats.findViewById(R.id.cats).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.post_date).setVisibility(View.VISIBLE);
        }
    }

    private void addFullScreenOption(final YouTubePlayerView youtubePlayerView, final String url)
    {
        youtubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener()
        {
            @Override
            public void onYouTubePlayerEnterFullScreen()
            {
                youtubePlayerView.getContext().startActivity(new Intent(context, YoutubeFullScreenActivity.class).putExtra("id",url));
                youtubePlayerView.exitFullScreen();
            }

            @Override
            public void onYouTubePlayerExitFullScreen()
            {

            }
        });
    }

    class MyViewHolder extends MyCommonViewHolder
    {

        ImageView imageView;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    view.getContext().startActivity(new Intent(context, BlogContent.class).putExtra("id", info.get(getAdapterPosition()).getId()));
                }
            });
            imageView = itemView.findViewById(R.id.image_post);
        }
    }

    class MyVideoViewHolder extends MyCommonViewHolder
    {

        YouTubePlayerView youtubePlayerView;
        YouTubePlayer youtubePlayer;
        private String currentVideoId;

        public MyVideoViewHolder(View itemView, YouTubePlayerView player)
        {
            super(itemView);
            youtubePlayerView = player;
            youtubePlayerView.initialize(new YouTubePlayerInitListener()
                                         {
                                             @Override
                                             public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer)
                                             {
                                                 addFullScreenOption(youtubePlayerView,info.get(getAdapterPosition()).getImgvideurl());
                                                 initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener()
                                                 {
                                                     @Override
                                                     public void onReady()
                                                     {
                                                         youtubePlayer = initializedYouTubePlayer;
                                                         youtubePlayer.cueVideo(currentVideoId, 0);

                                                     }

                                                     @Override
                                                     public void onStateChange(@NonNull PlayerConstants.PlayerState state)
                                                     {
                                                         super.onStateChange(state);
                                                         Log.d("visibility",state.toString());
                                                         if(state== PlayerConstants.PlayerState.PLAYING)
                                                             Singelton.setYouTubePlayer(youtubePlayer);


                                                     }
                                                 });
                                             }
                                         }
                    , true
            );



        }



        void cueVideo(String videoId)
        {
            currentVideoId = videoId;

            if (youtubePlayer == null)
                return;

            youtubePlayer.cueVideo(videoId, 0);
        }
    }
}
