package edu.ucsd.cse110.successorator.ui.goallist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentGoalListBinding;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalListFragment extends Fragment {

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
        this.adapter = new GoalListAdapter(requireContext(), InMemoryDataSource.DEFAULT_GOALS);
        // More MainViewModel stuff after this as well

    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
       // return inflater.inflate(R.layout.fragment_goal_list, container, false);
        this.view = FragmentGoalListBinding.inflate(inflater, container, false);
        view.goalList.setAdapter(adapter); return view.getRoot();
    }

}