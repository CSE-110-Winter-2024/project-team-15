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

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.ViewNumInfo;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.ComplexDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class CreateGoalDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    //private FragmentDialogCreateGoalBinding view;
    private FragmentDialogCreateGoalBinding view;
    CreateGoalDialogFragment() {} //empty constructor required

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        this.view = FragmentDialogCreateGoalBinding.inflate(getLayoutInflater());
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
        var recurrenceId = view.radioGroup.getCheckedRadioButtonId();
        int recurrenceType = 0;

        // first if is slightly unnecessary but whatever
        if(recurrenceId == R.id.one_time_button) {
            recurrenceType = 0;
        } else if (recurrenceId == R.id.daily_button) {
            recurrenceType = 1;
        } else if (recurrenceId == R.id.weekly_button) {
            recurrenceType = 2;
        } else if (recurrenceId == R.id.monthly_button){
            recurrenceType = 3;
        } else if (recurrenceId == R.id.yearly_button){
            recurrenceType = 4;
        }

        // objects necessary to extract goal recurrence data
        ComplexDateTracker myTracker = ComplexDateTracker.getInstance().getValue();
        int currentList = ViewNumInfo.getInstance().getValue().getListShown();
        LocalDateTime myTime = myTracker.getLocalDateTime();

        // move the time forwards if we're in the tomorrow view since it defaults to today
        if(currentList == 1) {
            myTime.plusDays(1);
        }

        // let's see if this works
        int dayCreated = myTime.getDayOfMonth();
        int monthCreated  = myTime.getMonthValue();
        int yearCreated = myTime.getYear();
        int dayOfWeekToRecur = myTime.getDayOfWeek().getValue(); // 1 is monday.
        int weekOfMonthToRecur = myTracker.getWeekOfMonth(myTime);


        // now we need to add goals.. it gets SLIGHTLY trickier here
        if(!goalText.equals("")){
            Goal goal;

            // some conditions based on what the goal is are necessary
            // if it's not one time, we need to add a recurrence template to the recurrence view
            if(recurrenceType != 0) {
                goal = new Goal(goalText, null, false, -1, /*this.activityModel.getListShown()*/ 3);
                goal = goal.withRecurrenceData(recurrenceType, dayCreated, monthCreated, yearCreated,
                        dayOfWeekToRecur, weekOfMonthToRecur, false);

                activityModel.insertIncompleteGoal(goal);
            }

            // as one does for any type of goal, add it to the current view
            goal = new Goal(goalText, null, false, -1, this.activityModel.getListShown());
            goal = goal.withRecurrenceData(0, dayCreated, monthCreated, yearCreated,
                    dayOfWeekToRecur, weekOfMonthToRecur, false);

            activityModel.insertIncompleteGoal(goal);

            // if also happens to be daily AND starts TODAY, we need to add it to the tomorrow view
            if(recurrenceType == 1 && currentList == 0) {
                // this isn't so dry but it is easy :(
                myTime.plusDays(1);

                dayCreated = myTime.getDayOfMonth();
                monthCreated  = myTime.getMonthValue();
                yearCreated = myTime.getYear();
                dayOfWeekToRecur = myTime.getDayOfWeek().getValue(); // 1 is monday.
                weekOfMonthToRecur = myTracker.getWeekOfMonth(myTime);

                goal = new Goal(goalText, null, false, -1, /*this.activityModel.getListShown()*/ 1);
                goal = goal.withRecurrenceData(0, dayCreated, monthCreated, yearCreated,
                        dayOfWeekToRecur, weekOfMonthToRecur, false);

                activityModel.insertIncompleteGoal(goal);
            }

            // can't think of any more cases
        }

        dialog.dismiss();
    }

    public static CreateGoalDialogFragment newInstance(){
        var fragment = new CreateGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}