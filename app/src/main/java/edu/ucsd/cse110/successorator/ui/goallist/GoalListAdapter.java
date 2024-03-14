package edu.ucsd.cse110.successorator.ui.goallist;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.ucsd.cse110.successorator.databinding.ListItemGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
//import edu.ucsd.cse110.successorator.R;

public class GoalListAdapter extends ArrayAdapter<Goal> {

    private final Supplier<Drawable> strikethroughSupplier;
    private final Consumer<Goal> onGoalClicked;

    private final Supplier<Drawable> homeContextSupplier;

    private final Supplier<Drawable> workContextSupplier;

    private final Supplier<Drawable> schoolContextSupplier;

    private final Supplier<Drawable> errandsContextSupplier;

    private final Supplier<Drawable> circleSupplier;

    public GoalListAdapter(
            Context context,
            List<Goal> goals,
            Supplier<Drawable> strikethroughSupplier,
            Consumer<Goal> onGoalClicked,
            Supplier<Drawable> homeContextSupplier,
            Supplier<Drawable> workContextSupplier,
            Supplier<Drawable> schoolContextSupplier,
            Supplier<Drawable> errandsContextSupplier,
            Supplier<Drawable> circleSupplier
    ){
        super(context, 0, new ArrayList<>(goals));
        this.strikethroughSupplier = strikethroughSupplier;
        this.onGoalClicked = onGoalClicked;
        this.homeContextSupplier = homeContextSupplier;
        this.workContextSupplier = workContextSupplier;
        this.schoolContextSupplier = schoolContextSupplier;
        this.errandsContextSupplier = errandsContextSupplier;
        this.circleSupplier = circleSupplier;
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

        //MS2-US8 I refactored this part according to lecture to use hash table instead of cases
        //This also made it easier to gray out
        //-Sheridan
        Drawable[] contextForegrounds = {homeContextSupplier.get(), workContextSupplier.get(), schoolContextSupplier.get(), errandsContextSupplier.get()};
        String[] colors = {"#EDDB35", "#56AEF4", "#956AE1", "#6AE46F"};


        binding.contextIcon.setImageDrawable(circleSupplier.get());

        Drawable contextForeground = contextForegrounds[goal.context()];
        String color= colors[goal.context()];
        binding.contextIcon.setForeground(contextForeground); //sets icon itself
        binding.contextIcon.setColorFilter(Color.parseColor(color)); //sets background color

        if (goal.completed()) {
            binding.goalText.setForeground(strikethroughSupplier.get());
            color = "#808080";
            binding.contextIcon.setColorFilter(Color.parseColor(color)); //grayed out icon for complete goal
        } else {
            binding.goalText.setForeground(null);

        }

        binding.goalText.setOnClickListener(v -> onGoalClicked.accept(goal));

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