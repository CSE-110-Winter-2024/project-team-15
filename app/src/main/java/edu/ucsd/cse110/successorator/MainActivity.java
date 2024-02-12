package edu.ucsd.cse110.successorator;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.Goals;
import edu.ucsd.cse110.successorator.ui.goallist.GoalListAdapter;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;



public class MainActivity extends AppCompatActivity {
    private GoalListAdapter adapter;
    Drawable strikethroughDrawable;
    private GoalRepository goalRepository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
//        view.placeholderText.setText(R.string.hello_world);
        view.placeholderText.setText(null);
        var strikethroughDrawable = getResources().getDrawable(R.drawable.line, null);
        SuccessoratorApplication app = (SuccessoratorApplication) getApplication();
        goalRepository = app.getGoalRepository();

        adapter = new GoalListAdapter(this, new ArrayList<>(), strikethroughDrawable);
        setContentView(view.getRoot());


    }
    public void onClick(View view){
        if(view.getId() == R.id.goal_text){ //checks for correct view
            Goal goal = (Goal) view.getTag();
            //getting goal associated with view that we tagged earlier
            if(goal!=null && goal.completed()==false) {
                //US7: checking for falseness here to set up for undoing completion in the future
                Goals.markComplete(goalRepository, goal);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        var itemId = item.getItemId();

        if (itemId == R.id.action_bar_menu_swap_views){
            // swapFragments();
        }
        return super.onOptionsItemSelected(item);
    }

}
