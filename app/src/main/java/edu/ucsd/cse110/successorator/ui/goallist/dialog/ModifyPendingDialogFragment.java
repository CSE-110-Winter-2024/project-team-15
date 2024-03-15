package edu.ucsd.cse110.successorator.ui.goallist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogModifyPendingGoalsBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogSwitchViewBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyPendingDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyPendingDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentDialogModifyPendingGoalsBinding view;
    private int goalId;
    private static Goal globalGoal; // i hate this

    public ModifyPendingDialogFragment() {
        // Required empty public constructor
    }

    public static ModifyPendingDialogFragment newInstance(Goal goal) {
        ModifyPendingDialogFragment fragment = new ModifyPendingDialogFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_GOAL_ID, goal.id());
        globalGoal = goal;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.goalId = globalGoal.id();

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_modify_pending_goals, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        this.view = FragmentDialogModifyPendingGoalsBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true);
        return new AlertDialog.Builder(getActivity())
                .setTitle("Modify the pending Goal Status")
                .setView(view.getRoot())
                .setPositiveButton("Apply", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
        //method of implementing confirm creation
        //will also implement cancel creation

    }

    private void onNegativeButtonClick(DialogInterface dialog, int i) {
        dialog.cancel();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int i) {
        if(view.pendingToTodayBtn.isChecked()){
            Log.i("mytag","today"+goalId);
            activityModel.changePendingGoalStatus(this.goalId, 0);
        }
        else if(view.pendingToTomorrowBtn.isChecked()){
            Log.i("mytag","tomorrow"+goalId);
            activityModel.changePendingGoalStatus(this.goalId, 1);
        }
        else if(view.pendingToFinishBtn.isChecked()){
            Log.i("mytag","Finish"+goalId);
            Goal locGoal = globalGoal.withComplete(true).withListNum(0);
            activityModel.pendingRemove(this.goalId);
            activityModel.insertCompleteGoal(locGoal);
        }
        else if(view.pendingToDeleteBtn.isChecked()){
            Log.i("mytag","Delete"+goalId);
            activityModel.pendingRemove(this.goalId);
        }
        dialog.dismiss();
    }
}