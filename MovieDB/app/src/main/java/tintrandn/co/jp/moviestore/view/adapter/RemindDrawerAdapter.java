package tintrandn.co.jp.moviestore.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.storage.dao.MovieDao;
import tintrandn.co.jp.moviestore.model.storage.entity.Remind;

public class RemindDrawerAdapter extends RecyclerView.Adapter <RemindDrawerAdapter.MyViewHolder> {

    private List<Remind> remind = Collections.emptyList();

    public RemindDrawerAdapter(List<Remind> remind) {
        this.remind = remind;
    }

    public void updateData(List<Remind> remind){
        this.remind = remind;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView remind_title;
        TextView remind_time;


        MyViewHolder(View itemView) {
            super(itemView);
            remind_title = (TextView) itemView.findViewById(R.id.remind_title);
            remind_time = (TextView)itemView.findViewById(R.id.remind_time);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_drawer_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Remind current = remind.get(position);
        String title = current.getRemind_movie_name();
        String release_day = current.getRemind_movie_release_day();
        String [] year = release_day.split("-");
        String rate = current.getRemind_movie_rate();

        holder.remind_title.setText(title+" - "+year[0]+" - "+rate+"/10");
        holder.remind_time.setText(current.getRemind_time());
    }

    @Override
    public int getItemCount() {
        return remind.size();
    }
}
