package tintrandn.co.jp.moviestore.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.activity.ProfileEdit;
import tintrandn.co.jp.moviestore.model.UserProfile;
import tintrandn.co.jp.moviestore.model.storage.dao.RemindDao;
import tintrandn.co.jp.moviestore.model.storage.dao.UserProfileDao;
import tintrandn.co.jp.moviestore.model.storage.entity.Remind;
import tintrandn.co.jp.moviestore.view.adapter.RemindDrawerAdapter;


public class FragmentDrawer extends Fragment{
    private static String TAG = "FragmentDrawer";
    private FragmentDrawerListener mDrawerListener;
    private Context mContext;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RemindDrawerAdapter adapter;
    private TextView user_name;
    private TextView user_mail;
    private TextView user_birthday;
    private TextView user_gender;

    public FragmentDrawer() {
    }

    public void setDrawerListener (FragmentDrawerListener mDrawerListener) {
        this.mDrawerListener = mDrawerListener;
    }

    private ImageView profile_picture;
    //    private SharedPreferences sharedPreferences;
//    private static final String SHOW_ALL_REMIND = "tintrandn.co.jp.moviestore.SHOW_ALL_REMIND";



    private List<Remind> getData() {
        List<Remind> remindList = new ArrayList<>();
        RemindDao remindDao = new RemindDao(mContext);
        if (remindDao.checkDataBase()){
            remindList = remindDao.getRemind(2);
        }
        return remindList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_navigation_drawer,container,false);
        mContext = getContext();
        RecyclerView mRemindList = (RecyclerView) view.findViewById(R.id.remind_drawer_list);
        adapter = new RemindDrawerAdapter(getData());
        mRemindList.setAdapter(adapter);
        mRemindList.setLayoutManager(new LinearLayoutManager(getActivity()));
        profile_picture = (ImageView) view.findViewById(R.id.profile_picture);
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_mail = (TextView) view.findViewById(R.id.user_mail);
        user_birthday = (TextView) view.findViewById(R.id.user_birthday);
        user_gender = (TextView) view.findViewById(R.id.user_gender);
        Button btn_profile_edit = (Button) view.findViewById(R.id.btn_profile_edit);
        Button btn_profile_showall = (Button) view.findViewById(R.id.btn_profile_showall);
        //send a broadcast to display the show all screen
        btn_profile_showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close app drawer
                mDrawerLayout.closeDrawers();
                //call back main activity and switch to setting tab
                mDrawerListener.callback();
                //send broadcast to Setting contain, transaction layout to show all remind
//                Intent show_all_remoind = new Intent();
//                show_all_remoind.setAction(SHOW_ALL_REMIND);
//                getActivity().sendBroadcast(show_all_remoind);
//                Log.d(TAG,"Send broadcast " +SHOW_ALL_REMIND);
            }


        });
        //set Edit listener
        btn_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_edit_intent = new Intent(getActivity(),ProfileEdit.class);
                startActivity(profile_edit_intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG,"onResume");
        UserProfileDao userProfileDao = new UserProfileDao(getContext());
        if (userProfileDao.checkDataBase()){
            UserProfile user = userProfileDao.getUser();
            if (user !=null) {
                if (user.getUser_image()!=null){
                    File imgFile = new File(user.getUser_image());
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    profile_picture.setImageBitmap(myBitmap);
                }
                user_name.setText(user.getUser_name());
                user_mail.setText(user.getUser_mail());
                user_birthday.setText(user.getUser_birthday());
                user_gender.setText(user.getUser_gender());
            }
        }

        super.onResume();
    }

    public void setUp(DrawerLayout drawerLayout, final Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("Drawer","Open");
                adapter.updateData(getData());
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("Drawer","Close");
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                adapter.updateData(getData());
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

//        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public interface FragmentDrawerListener {
        // you can define any parameter as per your requirement
        void callback();
            //do nothing
    }
}
