package edu.ucsd.cse110.successorator.ui.goallist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.ListItemGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class GoalListAdapter extends ArrayAdapter<Goal> {
    public GoalListAdapter(Context context, List<Goal> goals){
        super(context, 0, new ArrayList<>(goals));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var goal = getItem(position);
        assert goal != null;

        // Check if a view is being reused...
        ListItemGoalBinding binding;
        if (convertView != null){
            // if so, bind to it
            binding = ListItemGoalBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemGoalBinding.inflate(layoutInflater, parent, false);
        }

        // populate view with goal data
        binding.goalText.setText(goal.contents());

        return binding.getRoot();
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    public long getItemId(int position){
        var goal = getItem(position);
        assert goal != null;

        var id = goal.id();
        assert id != null;

        return id;
    }
}