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

// allowed to use this import and be SRP as long as we don't modify it here
import java.time.LocalDateTime;
import edu.ucsd.cse110.successorator.lib.domain.ComplexDateTracker;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateRecurringGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;

public class CreateRecurringGoalDialogFragment extends DialogFragment{
    private MainViewModel activityModel;
    private FragmentDialogCreateRecurringGoalBinding view;
    CreateRecurringGoalDialogFragment() {} //empty constructor required

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        this.view = FragmentDialogCreateRecurringGoalBinding.inflate(getLayoutInflater());
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

        //tried to write switch, was getting errors, feel free to change this to a switch if it'll work
        if(recurrenceId == R.id.daily_button){
                recurrenceType = 1;}
        else if(recurrenceId == R.id.weekly_button){
                recurrenceType = 2;}
        else if(recurrenceId == R.id.monthly_button){
                recurrenceType = 3;}
        else if(recurrenceId == R.id.yearly_button){
                recurrenceType = 4;}

        // date picked from datepicker
        var dayCreated = view.datePicker.getDayOfMonth(); // actual day not Sunday (ie the 7th)
        var monthCreated  = view.datePicker.getMonth(); // month but jan is 0 and dec is 11
        var yearCreated = view.datePicker.getYear(); // 2024


        // let's see its representation as a LocalDateTime object
        ComplexDateTracker myTracker = ComplexDateTracker.getInstance().getValue();
        LocalDateTime representation = myTracker.datePickerToLocalDateTime(yearCreated, monthCreated, dayCreated);

        // now we can easily extract the day
        int dayOfWeekToRecur = representation.getDayOfWeek().getValue(); // 1 is monday.

        // and the week of month (woohoo)
        int weekOfMonthToRecur = myTracker.getWeekOfMonth(representation);

        // and make a new goal
        if(!goalText.equals("")){
            var goal = new Goal(goalText, null, false, -1, this.activityModel.getListShown());
            goal = goal.withRecurrenceData(recurrenceType, dayCreated, monthCreated, yearCreated,
                    dayOfWeekToRecur, weekOfMonthToRecur, false);

            activityModel.insertIncompleteGoal(goal);
        }

        dialog.dismiss();
    }

    public static CreateRecurringGoalDialogFragment newInstance(){
        var fragment = new CreateRecurringGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
