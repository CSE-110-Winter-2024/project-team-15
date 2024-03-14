package edu.ucsd.cse110.successorator.ui.goallist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentFocusModeOptionsBinding;

public class FocusModeDialogFragment extends DialogFragment {

    private MainViewModel activityModel;
    private FragmentFocusModeOptionsBinding view;

    MenuItem item;

    FocusModeDialogFragment(MenuItem item){this.item = item;}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentFocusModeOptionsBinding.inflate(getLayoutInflater());
//MS2-US7 focus mode will now be using the context icons as indicators
// -Sheridan
        view.homeContextButton.setOnClickListener(v -> {
            activityModel.focusView(0);
            item.setIcon(R.drawable.homecontext);
            this.dismiss();
        });
        view.workContextButton.setOnClickListener(v -> {
            activityModel.focusView(1);
            item.setIcon(R.drawable.workcontext);
            this.dismiss();
        });
        view.schoolContextButton.setOnClickListener(v -> {
            activityModel.focusView(2);
            item.setIcon(R.drawable.schoolcontext);
            this.dismiss();
        });
        view.errandContextButton.setOnClickListener(v -> {
            activityModel.focusView(3);
            item.setIcon(R.drawable.errandscontext);
            this.dismiss();
        });
        view.cancelFocusButton.setOnClickListener(v -> {
            activityModel.focusView(6);
            item.setIcon(R.drawable.ic_menu);
            this.dismiss();
        });
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

    public void setItem(MenuItem item){
        this.item = item;
    }
    //Allows us to change the icon
    //-Sheridan



    public static FocusModeDialogFragment newInstance(MenuItem item){
        var fragment = new FocusModeDialogFragment(item);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


}
