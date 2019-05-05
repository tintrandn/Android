package tintrandn.co.jp.moviestore.fragment.container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.fragment.FragmentRemindShowAll;
import tintrandn.co.jp.moviestore.fragment.FragmentSetting;

public class FragmentSettingContainer extends Fragment implements FragmentSetting.SettingCallback{
    private String TAG = "Setting";
    private SettingContanerListener mSettingContanerListener;
    private FragmentManager mFragmentManager;

    public void setSettingListener(SettingContanerListener mSettingContanerListener ){
        this.mSettingContanerListener = mSettingContanerListener;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_container,container,false);
        mFragmentManager = getChildFragmentManager();
        if (mFragmentManager.getFragments()==null) {
            addFragmentSetting();
        }
        return v;
    }

    private void addFragmentSetting() {
        FragmentSetting firstFragment = new FragmentSetting(this);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,firstFragment, "Settings");
        fragmentTransaction.addToBackStack("addFragmentSetting");
        fragmentTransaction.commit();

    }

    public void replaceFragmentShowAllRemind() {
        Log.d(TAG,"Replace FragmentShowAllRemind "+mFragmentManager.getFragments().size());
        FragmentRemindShowAll secondFragment = new FragmentRemindShowAll();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, secondFragment, "Reminds");
        fragmentTransaction.addToBackStack("addFragmentShowAllRemind");
        // Commit the transaction
        fragmentTransaction.commit();
    }


    public void changeFragment(){
//        for(int entry = 0; entry < mFragmentManager.getBackStackEntryCount(); entry++){
//            String ide = mFragmentManager.getBackStackEntryAt(entry).getName();
//            Log.d("TAG", "Found fragment: " + ide);
//        }
            if (mFragmentManager.getBackStackEntryCount()==2){
                mFragmentManager.popBackStack();
            }
        mFragmentManager.popBackStack("MovieDetail",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceFragmentShowAllRemind();
    }

    @Override
    public void SettingBack() {
        mSettingContanerListener.updateDisplay();
    }

    public interface SettingContanerListener{
        void updateDisplay();
    }

}
