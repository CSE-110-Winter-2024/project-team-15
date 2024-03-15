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
        String goalText = view.goalNameEditText.getText().toString();
        int recurrenceId = view.radioGroup.getCheckedRadioButtonId();
        int recurrenceType = activityModel.resolveRecurrenceType(recurrenceId);

        if (!goalText.equals("")) {
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


            // date picked from date picker
            int dayCreated = view.datePicker.getDayOfMonth();
            int monthCreated = view.datePicker.getMonth() + 1; // month but jan is 0, so add 1
            int yearCreated = view.datePicker.getYear();

            // let's see its representation as a LocalDateTime object
            ComplexDateTracker myTracker = ComplexDateTracker.getInstance().getValue();
            LocalDateTime representation = myTracker.datePickerToLocalDateTime(yearCreated, monthCreated, dayCreated);

            // Now call the ViewModel method to handle goal creation
            activityModel.createRecurringGoal(goalText, recurrenceType, representation, contextToAdd);

            dialog.dismiss();
        }
    }

    public static CreateRecurringGoalDialogFragment newInstance(){
        var fragment = new CreateRecurringGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
