package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.DateTracker;
import edu.ucsd.cse110.successorator.lib.domain.MockDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.ui.goallist.dialog.CreateGoalDialogFragment;

// MAIN ACTIVITY does not know about date
public class MainActivity extends AppCompatActivity {
    private MutableSubject<SimpleDateTracker> dateTracker;
    private Integer daysForwarded;
    private boolean isShowingList = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this.dateTracker = SimpleDateTracker.getInstance();
        this.dateTracker = SimpleDateTracker.getInstance();
        this.daysForwarded = 0;
        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        view.placeholderText.setText(null);

        setContentView(view.getRoot());
    }

    // for the button

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.action_bar_menu_swap_views) {

            var dialogFragment = CreateGoalDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "CreateGoalDialogFragment");

        } else if (item.getItemId() == R.id.action_bar_menu_forward_day){
            // 1. update the DateTracker object within the subject
            // 2. keep track of daysForwarded increasing
            // 3. make the subject hold the new DateTracker object
            var rawDateTracker = this.dateTracker.getValue();
            daysForwarded++;
            rawDateTracker.setForwardBy(daysForwarded);
            rawDateTracker.update();
            this.dateTracker.setValue(rawDateTracker);
            onResume(); // onResume nicely sets the title

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        var rawDateTracker = dateTracker.getValue();
        if(rawDateTracker.getHour()>=2) {
            setTitle(rawDateTracker.getDate());
        }

    }

}