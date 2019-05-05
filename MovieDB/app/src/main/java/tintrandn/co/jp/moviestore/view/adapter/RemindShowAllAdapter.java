package tintrandn.co.jp.moviestore.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.storage.entity.Remind;

public class RemindShowAllAdapter extends RecyclerView.Adapter<RemindShowAllAdapter.ViewHolder> {

    private static final String TAG = "RemindShowAllAdapter";
    private ArrayList<Remind> mRemindArrayList;
    private DeleteRemindListener mDeleteRemindlistener;
    public RemindShowAllAdapter(ArrayList<Remind> mRemindArrayList) {
        this.mRemindArrayList = mRemindArrayList;
    }

    public void SetRemindShowAllAdapter(DeleteRemindListener mDeleteRemindlistener) {
        this.mDeleteRemindlistener = mDeleteRemindlistener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView remind_tilte, remind_time;

        ViewHolder(View view) {
            super(view);
            remind_tilte = (TextView) view.findViewById(R.id.remind_show_all_name);
            remind_time = (TextView) view.findViewById(R.id.remind_show_all_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.remind_item_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Remind mRemind = mRemindArrayList.get(position);
//        // Instantiate the RequestQueue.
//        CustomRequestQueue requestQueue = new CustomRequestQueue(mContext);
//        ImageLoader.ImageCache imageCache = new BitmapLruCache();
//        ImageLoader imageLoader = new ImageLoader(requestQueue.getRequestQueue(), imageCache);
        //Image URL - This can point to any image file supported by Android
//        final String url = "https://image.tmdb.org/t/p/w640/";
//        final String url = "https://image.tmdb.org/t/p/w533_and_h300_bestv2/";
//        holder.remind_image.setImageUrl(url+remind.getImage(),imageLoader);
        String title = mRemind.getRemind_movie_name();
        String release = mRemind.getRemind_movie_release_day();
        String [] year = release.split("-");
        String rate = mRemind.getRemind_movie_rate();
        holder.remind_tilte.setText(title+" - "+year[0]+" - "+rate+"/10");
        holder.remind_time.setText(mRemind.getRemind_time());
        final String remind_id = mRemind.get_id();
        Log.d(TAG,"Remind id: " + remind_id + " position: " +String.valueOf(position) );
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,"Remind item click");
                mDeleteRemindlistener.callback(remind_id, holder.getAdapterPosition());
                return false;
            }
        });
    }

    public void removeDeleteRemind(int position) {
        Log.d("Remove remind","Call from Fragment");
        mRemindArrayList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRemindArrayList.size();
    }

    public interface DeleteRemindListener {
        // you can define any parameter as per your requirement
        void callback(String remind_id,int position);
        //do nothing
    }
}
