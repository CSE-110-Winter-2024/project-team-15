package edu.ucsd.cse110.successorator.ui.goallist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentGoalListBinding;
// removing this since i don't want to use default cards
//import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateFragment extends Fragment {

    private MainViewModel activityModel;
    private FragmentDateBinding view;
    //private GoalListAdapter adapter;

    public DateFragment() {
        // Required empty public constructor
    }


    public static DateFragment newInstance() {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        activityModel.getDateChanged().observe(date -> {
            if (goals == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(goals));
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        this.view = FragmentGoalListBinding.inflate(inflater, container, false);

        setupMvp();
        return view.getRoot();
    }

    /**
     * Model and view are both ready. Do binding.
     */
    private void setupMvp() {
        // M - > V
        view.goalList.setAdapter(adapter);
        activityModel.getNoGoals().observe(isNoGoals -> {
            if (isNoGoals == null) return;
            view.noGoalsView.setVisibility(isNoGoals ? View.VISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //this.activityModel.clearCompletedGoals();

    }
}