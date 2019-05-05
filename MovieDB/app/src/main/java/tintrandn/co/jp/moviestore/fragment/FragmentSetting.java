package tintrandn.co.jp.moviestore.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import tintrandn.co.jp.moviestore.R;


public class FragmentSetting extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String[] keys = {"pref_key_movie_category","pref_key_movie_rate_from","pref_key_movie_release_from","pref_key_movie_sort_by"};
    private SettingCallback mSettingCallback;
    private static int mResult;
    private static final String TAG = "FragmentSetting";

    public FragmentSetting(SettingCallback mSettingCallback) {
        this.mSettingCallback = mSettingCallback;
    }

    @Override
    public void setDivider(Drawable divider) {
        super.setDivider(null);
    }

    @Override
    public void setDividerHeight(int height) {
        super.setDividerHeight(0);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.setting_preferences, rootKey);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        String category = sharedPreferences.getString(keys[0],"Popular Movies");
        String rate = sharedPreferences.getString(keys[1], "0");
        String release_year = sharedPreferences.getString(keys[2],"2015");
        String sort = sharedPreferences.getString(keys[3],"Release Date");
        Log.d(TAG,"Category: " +category+" Rate: " + rate +" Year: " +release_year+ " sort: " +sort);
        ListPreference listMoviePreference = (ListPreference) findPreference(keys[0]);
        final Preference setRate = findPreference(keys[1]);
        EditTextPreference releaseYearPreference = (EditTextPreference) findPreference(keys[2]);
        ListPreference listSortPreference  = (ListPreference) findPreference(keys[3]);
        listMoviePreference.setSummary(category);
        setRate.setSummary(rate);
        releaseYearPreference.setSummary(release_year);
        listSortPreference.setSummary(sort);

        setRate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d("Set","Movie rate");
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View deleteDialogView = factory.inflate(R.layout.preference_seekbar, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Movie with rate from");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString(keys[1],String.valueOf(mResult));
                        editor.apply();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog deleteDialog = builder.create();
                deleteDialog.setView(deleteDialogView);
                final TextView seekbar_value = (TextView) deleteDialogView.findViewById(R.id.seekbar_value);
                SeekBar seekBar = (SeekBar)deleteDialogView.findViewById(R.id.seekbar);
                seekBar.setMax(10);
                mResult = Integer.valueOf((String)setRate.getSummary());
                seekBar.setProgress(mResult);
                seekbar_value.setText(setRate.getSummary());
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekbar_value.setText(String.valueOf(progress));
                        mResult = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                deleteDialog.show();
                return true;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(true);
//            Log.d("Settings","Set title Settings" );
//            actionBar.setTitle("Settings");
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(keys[0]) || key.equals(keys[3])){
            ListPreference  listPreference = (ListPreference) findPreference(key);
            listPreference.setSummary(sharedPreferences.getString(key, ""));
        }
        if (key.equals(keys[1])){
            Log.d("Set","Date");
            Preference  editTextPreference = findPreference(key);
            editTextPreference.setSummary(sharedPreferences.getString(key, ""));
        }
        if (key.equals(keys[2])){
            Log.d("Set","Release Date");
            EditTextPreference  editTextPreference = (EditTextPreference) findPreference(key);
            editTextPreference.setSummary(sharedPreferences.getString(key, ""));
        }
        mSettingCallback.SettingBack();
    }

    public interface SettingCallback{
        void SettingBack();
    }
}
