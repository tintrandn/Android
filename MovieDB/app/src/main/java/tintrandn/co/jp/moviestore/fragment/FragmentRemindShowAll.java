package tintrandn.co.jp.moviestore.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.storage.dao.RemindDao;
import tintrandn.co.jp.moviestore.model.storage.entity.Remind;
import tintrandn.co.jp.moviestore.view.adapter.RemindShowAllAdapter;

public class FragmentRemindShowAll extends Fragment {
    private Context mContext;

    public FragmentRemindShowAll() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show_all_remind,container,false);
        mContext = v.getContext();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar!= null) {
            Log.d("ShowAllRemind","Set title All Remind" );
            actionBar.setTitle("All Remind");
        }
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.remind_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        RemindDao remindDao = new RemindDao(v.getContext());
        ArrayList<Remind> remindArrayList = remindDao.getAllRemind();
        final RemindShowAllAdapter mRemindShowAllAdapter = new RemindShowAllAdapter(remindArrayList);
        mRecyclerView.setAdapter(mRemindShowAllAdapter);

        mRemindShowAllAdapter.SetRemindShowAllAdapter(new RemindShowAllAdapter.DeleteRemindListener() {
            @Override
            public void callback(final String remind_id, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // add a list
                String[] button = {"Delete", "Cancel"};
                builder.setItems(button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // delete
                                LayoutInflater factory = LayoutInflater.from(mContext);
                                final View deleteDialogView = factory.inflate(R.layout.delete_remind_dialog, null);
                                final AlertDialog deleteDialog = new AlertDialog.Builder(mContext).create();
                                deleteDialog.setView(deleteDialogView);
                                deleteDialogView.findViewById(R.id.agree_delete_remind).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //your business logic
                                        RemindDao remindDao = new RemindDao(mContext);
                                        //delete 1 row success
                                        Log.d("Deleted remind id: ", remind_id);
                                        if (remindDao.delete(remind_id) == 1) {
                                            mRemindShowAllAdapter.removeDeleteRemind(position);
                                            Log.d("Delete remind", " Success");
                                            deleteDialog.dismiss();
                                        }
                                    }
                                });
                                deleteDialogView.findViewById(R.id.cancel_delete_remind).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteDialog.dismiss();
                                    }
                                });

                                deleteDialog.show();

                            case 1: // cancel
                                dialog.dismiss();
                        }
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return v;
    }

}
