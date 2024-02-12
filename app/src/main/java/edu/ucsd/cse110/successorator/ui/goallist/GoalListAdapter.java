package edu.ucsd.cse110.successorator.ui.goallist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ListItemGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
//import edu.ucsd.cse110.successorator.R;

public class GoalListAdapter extends ArrayAdapter<Goal> {
    Drawable strikethroughDrawable;
    public GoalListAdapter(Context context, List<Goal> goals, Drawable strikethroughDrawable){
        super(context, 0, new ArrayList<>(goals));
        this.strikethroughDrawable = strikethroughDrawable;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var goal = getItem(position);
        assert goal != null;

        // Check if a view is being reused...
        ListItemGoalBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemGoalBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemGoalBinding.inflate(layoutInflater, parent, false);
        }

        // populate view with goal data
        binding.goalText.setText(goal.contents());
        binding.goalText.setTag(goal);//so that we can access goal to update complete later for onClick
        if (goal.completed()) {//US7 adding strikethrough
            binding.goalText.setForeground(strikethroughDrawable); //strikethroughDrawable is found in GoalListFragment
        }

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
