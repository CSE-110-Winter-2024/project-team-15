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
public class GoalListFragment extends Fragment {

    private MainViewModel activityModel;
    private FragmentGoalListBinding view;
    private GoalListAdapter adapter;

    public GoalListFragment() {
        // Required empty public constructor
    }


    public static GoalListFragment newInstance() {
        GoalListFragment fragment = new GoalListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: MainViewModel stuff
        /*REPLACE*/
        // init adapter
        // this.adapter = new GoalListAdapter(requireContext(), InMemoryDataSource.DEFAULT_GOALS);
        // Need to implement MainViewModel's observer that changes the given list.
        // Otherwise it will always only show the default.
        // (look into labs 4 and 5 for this)

        // init model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        var strikethroughDrawable = getResources().getDrawable(R.drawable.line, null);

        // init the adapter (with empty list for now)
        this.adapter = new GoalListAdapter(requireContext(), List.of(), strikethroughDrawable);
        activityModel.getOrderedGoals().observe(goals -> {
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
        view.goalList.setAdapter(adapter);
        return view.getRoot();
    }

    // why onViewCreated?
    // well after onCreateView we have a guarantee the variables are initialized
    // so now we can make any changes we want after initialization
    // and using observers!

    // let's not pretend i fully understand this
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // init
        super.onViewCreated(view, savedInstanceState);

        // just doing the same thing as oncreate..
        activityModel.getOrderedGoals().observe(goals -> {
            if (goals == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(goals));
            adapter.notifyDataSetChanged();
        });

        // TODO: replace findViewById with the normal way
        TextView noGoalsView = view.findViewById(R.id.noGoalsView);
        activityModel.getNoGoals().observe(noGoalsState -> {
            // has to be initialized because otherwise null errors
            if (getView() != null) {
                if (noGoalsState) {
                    noGoalsView.setVisibility(View.VISIBLE);
                } else {
                    noGoalsView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}