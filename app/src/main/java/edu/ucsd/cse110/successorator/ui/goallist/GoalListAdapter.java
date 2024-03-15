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

import edu.ucsd.cse110.successorator.ViewNumInfo;
import edu.ucsd.cse110.successorator.RecurrenceTitleAssembler;
import edu.ucsd.cse110.successorator.databinding.ListItemGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.ui.goallist.dialog.ModifyPendingDialogFragment;
import edu.ucsd.cse110.successorator.ui.goallist.dialog.SwitchViewDialogFragment;
//import edu.ucsd.cse110.successorator.R;

public class GoalListAdapter extends ArrayAdapter<Goal> {

    private final Supplier<Drawable> strikethroughSupplier;
    private final Consumer<Goal> onGoalClicked;

    private final Consumer<Goal> onRecurringGoalLongPressed;
    private final Supplier<Drawable> homeContextSupplier;

    private final Supplier<Drawable> workContextSupplier;

    private final Supplier<Drawable> schoolContextSupplier;

    private final Supplier<Drawable> errandsContextSupplier;

    private final Supplier<Drawable> circleSupplier;

    private final Consumer<Goal> onPendingGoalLongPressed;

    public GoalListAdapter(
            Context context,
            List<Goal> goals,
            Supplier<Drawable> strikethroughSupplier,
            Consumer<Goal> onGoalClicked,
            Consumer<Goal> onRecurringGoalLongPressed,
            Consumer<Goal> onPendingGoalLongPressed,

            Supplier<Drawable> homeContextSupplier,
            Supplier<Drawable> workContextSupplier,
            Supplier<Drawable> schoolContextSupplier,
            Supplier<Drawable> errandsContextSupplier,
            Supplier<Drawable> circleSupplier
    ){
        super(context, 0, new ArrayList<>(goals));
        this.strikethroughSupplier = strikethroughSupplier;
        this.onGoalClicked = onGoalClicked;
        this.onRecurringGoalLongPressed = onRecurringGoalLongPressed;
        this.onPendingGoalLongPressed = onPendingGoalLongPressed;

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
        if(ViewNumInfo.getInstance().getValue().getListShown() == 3) {
            // not exactly OCP ...
            // i apologize too. maybe ill make function later
            RecurrenceTitleAssembler titleAssembler = new RecurrenceTitleAssembler(goal);
            binding.goalText.setText(titleAssembler.makeTitle());
        } else {
            binding.goalText.setText(goal.contents());
        }

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

        // recurring and pending should not be able to be marked as complete
        // let me know if this should be moved directly into toggleComplete
        binding.goalText.setOnClickListener(v -> {
            if ((goal.listNum() != 2) && (goal.listNum() != 3)){
                onGoalClicked.accept(goal);
            }
        });

        binding.goalText.setOnLongClickListener(v -> {
            if (ViewNumInfo.getInstance().getValue().getListShown() == 3){
                onRecurringGoalLongPressed.accept(goal);
            }
            if (ViewNumInfo.getInstance().getValue().getListShown() == 2){
                onPendingGoalLongPressed.accept(goal);
            }
            return true;
        });

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