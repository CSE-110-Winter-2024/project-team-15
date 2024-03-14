package edu.ucsd.cse110.successorator.ui.goallist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentFocusModeOptionsBinding;

public class FocusModeDialogFragment extends DialogFragment {

    private MainViewModel activityModel;
    private FragmentFocusModeOptionsBinding view;

    FocusModeDialogFragment(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentFocusModeOptionsBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true);
        view.homeContextButton.setOnClickListener(v -> activityModel.focusView(0));
        view.workContextButton.setOnClickListener(v -> activityModel.focusView(1));
        view.schoolContextButton.setOnClickListener(v -> activityModel.focusView(2) );
        view.errandContextButton.setOnClickListener(v -> activityModel.focusView(3));
        view.cancelFocusButton.setOnClickListener(v -> activityModel.focusView(6));
        return new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        this.view = FragmentFocusModeOptionsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }





    public static FocusModeDialogFragment newInstance(){
        var fragment = new FocusModeDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


}
