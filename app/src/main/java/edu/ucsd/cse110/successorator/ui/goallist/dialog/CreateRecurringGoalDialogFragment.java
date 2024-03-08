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

import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateRecurringGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
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

        //characteristics of data commented after testing
        //selected value
        //something strange going on
        // math should nt be done here
        // date picked from datepicker
        var dayCreated = view.datePicker.getDayOfMonth(); // actual day not Sunday (ie the 7th)
        var monthCreated  = view.datePicker.getMonth(); // month but jan is 0 and dec is 11
        var yearCreated = view.datePicker.getYear(); // 2024

        // "weeks SINCE this day"
        //int weekOfMonthToRecur = ((int)dayCreated / 7);

        // needs review
//        Date selectedDate = new Date(yearCreated, monthCreated, dayCreated);
//        Calendar dayOfWeekFinder = Calendar.getInstance();
//        dayOfWeekFinder.setTime(selectedDate);
//        int dayOfWeekToRecur = dayOfWeekFinder.get(Calendar.DAY_OF_WEEK);
        //need to use calender here to get dayOfWeekToRecur

        // for easy usage why dont we just use a calendar
        // convert datepicker to calendar
        Calendar ins = (Calendar) Calendar.getInstance().clone();
        ins.set(Calendar.YEAR, yearCreated);
        ins.set(Calendar.MONTH, monthCreated);
        ins.set(Calendar.DAY_OF_MONTH, dayCreated);

        // now we can easily extract the day
        int dayOfWeekToRecur = ins.get(Calendar.DAY_OF_WEEK); // 1 is sat.


        // Now we need to extract the amount of times this dayOfWeek has appeared
        // since we want something like "3rd Tuesday" ..
        //
        // we do this as seen below:
        //
        // if we go to the first day of the month
        // and then go to the first time our desired dayOfWeek appeared
        //
        // then subtracting our day of month by the first time day of week appears gives us the
        // amount of days between them
        //
        // dividing that by 7 gives us the amount of weeks between them
        ins.set(Calendar.DAY_OF_MONTH, 1);
        ins.set(Calendar.DAY_OF_WEEK, dayOfWeekToRecur);
        int weekOfMonthToRecur = ((dayCreated - ins.get(Calendar.DAY_OF_MONTH)) / 7);


//        // TEST CODE UNSURE IF BETTER
//        ins.set(Calendar.DAY_OF_MONTH, 1);
//
//        int occurrenceCount = 0;
//        while (ins.get(Calendar.DAY_OF_MONTH) <= dayCreated) {
//            if (ins.get(Calendar.DAY_OF_WEEK) == dayOfWeekToRecur) {
//                occurrenceCount++;
//            }
//
//            ins.add(Calendar.DAY_OF_MONTH, 1);
//        }
//
//        int weekOfMonthToRecur = occurrenceCount;
//        // TEST CODE ENDS HERE


        if(!goalText.equals("")){
            //recurrenceType unused as goals do not have that field yet
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
