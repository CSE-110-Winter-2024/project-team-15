package edu.ucsd.cse110.successorator.ui.goallist;

import android.content.Context;
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
import edu.ucsd.cse110.successorator.databinding.ListItemGoalBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
//import edu.ucsd.cse110.successorator.R;

public class GoalListAdapter extends ArrayAdapter<Goal> {

    private final Supplier<Drawable> strikethroughSupplier;
    private final Consumer<Goal> onGoalClicked;

    public GoalListAdapter(
            Context context,
            List<Goal> goals,
            Supplier<Drawable> strikethroughSupplier,
            Consumer<Goal> onGoalClicked
    ){
        super(context, 0, new ArrayList<>(goals));
        this.strikethroughSupplier = strikethroughSupplier;
        this.onGoalClicked = onGoalClicked;
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
            int recurrenceForTitle = goal.recurrenceType();
            String monthDay = goal.monthStarting() + "/" + goal.dayStarting();

            //  TODO: FIX THIS INTO A FUNCTION OR SOMETHING
            String dayStr = goal.dayOfWeekToRecurStr();          //sun, mon, etc
            String weekStr = goal.weekOfMonthToRecurStrSuffix(); //1st 4th 8th etc
            String recurType = goal.recurrenceTypeStr();         // daily weekly monthly yearly

            switch (recurrenceForTitle) {
                case 1:
                    //recurType = "daily";
                    binding.goalText.setText(goal.contents() + recurType);
                    break;
                case 2:
                    //recurType = "weekly";
                    binding.goalText.setText(goal.contents() + recurType + dayStr);
                    break;
                case 3:
                    //recurType = "monthly";
                    binding.goalText.setText(goal.contents() + recurType + weekStr + dayStr);
                    break;
                case 4:
                    //recurType = "yearly";
                    binding.goalText.setText(goal.contents() + ", yearly on " + monthDay);
                    break;
                default:
                    binding.goalText.setText(goal.contents());
                    break;
            }

        } else {
            binding.goalText.setText(goal.contents());
        }

        if (goal.completed()) {//US7 adding strikethrough
            var drawable = strikethroughSupplier.get();
            binding.goalText.setForeground(drawable); //strikethroughDrawable is found in GoalListFragment
//            binding.goalText.setForegroundGravity(Gravity.FILL_HORIZONTAL);
//            binding.goalText.setForegroundGravity(Gravity.CENTER_VERTICAL);
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