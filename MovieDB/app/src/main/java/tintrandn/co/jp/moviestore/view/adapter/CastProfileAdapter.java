package tintrandn.co.jp.moviestore.view.adapter;

import android.content.Context;
//import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.Cast;
import tintrandn.co.jp.moviestore.util.CustomVolleyRequestQueue;


public class CastProfileAdapter extends RecyclerView.Adapter<CastProfileAdapter.ViewHolder> {

    private static final String TAG = "CastProfileAdapter";
    private Context mContext;
    private ArrayList<Cast> mCastArrayList;

    public CastProfileAdapter(Context mContext , ArrayList<Cast> casts) {
        this.mContext = mContext;
        this.mCastArrayList = casts;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private NetworkImageView cast_profile;
        private TextView name;

        ViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            cast_profile = (NetworkImageView) view.findViewById(R.id.cast_profile);
            name = (TextView) view.findViewById(R.id.cast_name);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"Create Cast Profile View");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_profile_item, parent, false);

        return new CastProfileAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Cast cast = mCastArrayList.get(position);
        Log.d(TAG,"onBind "+cast.getName());
        // Instantiate the RequestQueue.
        //Image URL - This can point to any image file supported by Android
        final String url = "http://image.tmdb.org/t/p/w138_and_h175_bestv2";
        ImageLoader mImageLoader = CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();
        mImageLoader.get(url+cast.getImage(), ImageLoader.getImageListener(holder.cast_profile, R.drawable.loading, R.drawable.image_not_found));
        holder.cast_profile.setImageUrl(url+cast.getImage(),mImageLoader);
        holder.name.setText(cast.getName());
    }

    @Override
    public int getItemCount() {
        return mCastArrayList.size();
    }

//    class MyTask extends AsyncTask<Object, Object, String> {
//        @Override
//        protected String doInBackground(Object... params) {
//
//            return "Task completed";
//        }
//        @Override
//        protected void onPostExecute(String result) {
//
//
//        }
//    }
}
