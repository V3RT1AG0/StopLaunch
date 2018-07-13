package com.gamerequirements.Home;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamerequirements.Blog.Information;
import com.gamerequirements.MyApplication;
import com.gamerequirements.R;
import com.gamerequirements.Singelton;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by v3rt1ag0 on 7/7/18.
 */

class BlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<Information> info;
    Context context;
    private Lifecycle lifecycle;

    BlogAdapter(List<Information> info, Lifecycle lifecycle)
    {

        this.info = info;
        context = MyApplication.getContext();
        this.lifecycle = lifecycle;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        switch (viewType)
        {
            case 2:
                return new MyViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blog_card_post, parent, false));
            case 5:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_card_video, parent, false);
                YouTubePlayerView youTubePlayerView = v.findViewById(R.id.youtube_player_view);
                youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
                lifecycle.addObserver(youTubePlayerView);
                return new MyVideoViewHolder(v,youTubePlayerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Information bloginfo = info.get(position);
        switch (holder.getItemViewType())
        {

            case 2:
                MyViewHolder holder0 = (MyViewHolder) holder;
                holder0.title.setText(bloginfo.getTitle());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder0.subtitle.setText(Html.fromHtml(bloginfo.getSubtitle(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder0.subtitle.setText(Html.fromHtml(bloginfo.getSubtitle()));

                String url = bloginfo.getImgvideurl();
                Log.d("triggered1",bloginfo.getTitle());
                Picasso.with(context)
                        .load(url)
                        .into(holder0.imageView);
                break;

            case 5:
                MyVideoViewHolder holder1 = (MyVideoViewHolder) holder;
                holder1.title.setText(bloginfo.getTitle());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder1.subtitle.setText(Html.fromHtml(bloginfo.getSubtitle(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder1.subtitle.setText(Html.fromHtml(bloginfo.getSubtitle()));

                holder1.cueVideo(bloginfo.getImgvideurl());
                break;
        }

    }

    @Override
    public int getItemViewType(int position)
    {
        Information bloginfo = info.get(position);
        return bloginfo.getCategory();
    }


    @Override
    public int getItemCount()
    {
        return info.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, subtitle;
        ImageView imageView;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.title_post);
            subtitle = itemView.findViewById(R.id.subTitle_post);
            imageView = itemView.findViewById(R.id.image_post);
            itemView.findViewById(R.id.tags).setVisibility(View.GONE);
            itemView.findViewById(R.id.cats).setVisibility(View.GONE);
        }
    }

    class MyVideoViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, subtitle;
        YouTubePlayerView youtubePlayerView;
        YouTubePlayer youtubePlayer;
        private String currentVideoId;

        public MyVideoViewHolder(View itemView,YouTubePlayerView player)
        {
            super(itemView);
            youtubePlayerView = player;
            itemView.findViewById(R.id.tags).setVisibility(View.GONE);
            itemView.findViewById(R.id.cats).setVisibility(View.GONE);
            title = itemView.findViewById(R.id.title_post);
            subtitle = itemView.findViewById(R.id.subTitle_post);
            youtubePlayerView.initialize(new YouTubePlayerInitListener()
                                         {
                                             @Override
                                             public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer)
                                             {
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
                                                         Log.d("visibility2",state.toString());
                                                         if(state== PlayerConstants.PlayerState.BUFFERING)
                                                             HomeMain.cancelSlider();
                                                         else if(state== PlayerConstants.PlayerState.PAUSED)
                                                             HomeMain.setUpSlider();

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
