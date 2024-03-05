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
        var dayCreated = view.datePicker.getDayOfMonth();
        //january = 0
        var monthCreated  = view.datePicker.getMonth();
        //four digits
        var yearCreated = view.datePicker.getYear();


        //need to use calender here to get dayOfWeek and then math for weekOfMonth

        if(!goalText.equals("")){
            //momentarily using datetext to know how to parse
            //recurrenceType unused as goals do not have that field yet
            var goal = new Goal(goalText, null, false, -1, this.activityModel.getListShown());



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
