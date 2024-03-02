package edu.ucsd.cse110.successorator.ui.goallist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogSwitchViewBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;


public class SwitchViewDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentDialogSwitchViewBinding view;

    public SwitchViewDialogFragment() {
        // Required empty public constructor
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        this.view = FragmentDialogSwitchViewBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true);
        return new AlertDialog.Builder(getActivity())
                .setTitle("Switch View")
                .setView(view.getRoot())
                .setPositiveButton("Apply", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
        //method of implementing confirm creation
        //will also implement cancel creation

    }
    public static SwitchViewDialogFragment newInstance(String param1, String param2) {
        SwitchViewDialogFragment fragment = new SwitchViewDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    private void onNegativeButtonClick(DialogInterface dialog, int i) {
        dialog.cancel();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int i) {
        if(view.todayBtn.isChecked()){
            activityModel.switchView(0);
        }
        else if(view.tomorrowBtn.isChecked()){
            activityModel.switchView(1);
        }
        else if(view.pendingBtn.isChecked()){
            activityModel.switchView(2);
        }
        else if(view.recurringBtn.isChecked()){
            activityModel.switchView(3);
        }
        dialog.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_switch_view_dialog, container, false);
    }

    public static SwitchViewDialogFragment newInstance(){
        var fragment = new SwitchViewDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}