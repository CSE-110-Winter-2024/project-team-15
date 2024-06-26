package edu.ucsd.cse110.successorator.ui.goallist.dialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreatePendingGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class CreatePendingGoalDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private FragmentDialogCreatePendingGoalBinding view;
    CreatePendingGoalDialogFragment() {} //empty constructor required

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        this.view = FragmentDialogCreatePendingGoalBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true);
        return new AlertDialog.Builder(getActivity())
                .setTitle("New Goal")
                .setMessage("Provide your Most Important Thing!")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
        //method of implementing confirm creation
        //will also implement cancel creation

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
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

        var goalText = view.goalNameEditText.getText().toString();

        if(!goalText.equals("")){
            int contextToAdd = 0;
            if (view.homeContextButton.isChecked()) {
                contextToAdd = 0;
            } else if (view.workContextButton.isChecked()) {
                contextToAdd = 1;
            } else if (view.schoolContextButton.isChecked()) {
                contextToAdd = 2;
            } else if (view.errandContextButton.isChecked()) {
                contextToAdd = 3;

            } else {
                throw new IllegalStateException("No radio button is checked");
            }
            var goal = new Goal(goalText, null, false, -1,
                    this.activityModel.getListShown(), contextToAdd);
            activityModel.insertIncompleteGoal(goal);
        }

        dialog.dismiss();
    }

    public static CreatePendingGoalDialogFragment newInstance(){
        var fragment = new CreatePendingGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}