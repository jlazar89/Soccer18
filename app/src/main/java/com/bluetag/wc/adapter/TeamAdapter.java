package com.bluetag.wc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.model.Team;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jeffy on 5/28/2017.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.HolderView> {
    private Context mContext;
    private List<Team> mTeamList;

    public TeamAdapter(Context context, List<Team> teamList) {
        this.mContext = context;
        this.mTeamList = teamList;
    }

    @Override
    public TeamAdapter.HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_items, parent, false);

        return new TeamAdapter.HolderView(itemView);
    }

    @Override
    public void onBindViewHolder(final HolderView holder, int position) {
        final Team team = mTeamList.get(position);
        holder.teamName.setText(team.getTeamName());
        holder.mp.setText(team.getMplayed());
        holder.won.setText(team.getWon());
        holder.draw.setText(team.getDraw());
        holder.loose.setText(team.getLost());
        holder.gf.setText(team.getGoalsfor());
        holder.ga.setText(team.getGoalsagainst());
        holder.pts.setText(team.getPoints());

        Picasso.with(mContext).load(team.getTeamFlag()).placeholder(R.drawable.ic_placeholder_square).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mTeamList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {
        TextView teamName;
        TextView mp;
        TextView won;
        TextView draw;
        TextView loose;
        TextView gf;
        TextView ga;
        TextView pts;
        ImageView image;


        public HolderView(View view) {
            super(view);
            teamName = (TextView) view.findViewById(R.id.album_name);
            pts = (TextView) view.findViewById(R.id.points);

            mp = (TextView) view.findViewById(R.id.tvMP);
            won = (TextView) view.findViewById(R.id.tvW);
            draw = (TextView) view.findViewById(R.id.tvD);
            loose = (TextView) view.findViewById(R.id.tvL);
            gf = (TextView) view.findViewById(R.id.tvGF);
            ga = (TextView) view.findViewById(R.id.tvGA);
            image = (ImageView) view.findViewById(R.id.imageflag);
        }
    }
}
