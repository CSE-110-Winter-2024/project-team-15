package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.DateTracker;
import edu.ucsd.cse110.successorator.lib.domain.MockDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
import edu.ucsd.cse110.successorator.ui.goallist.dialog.CreateGoalDialogFragment;

// MAIN ACTIVITY does not know about date
public class MainActivity extends AppCompatActivity {
    private DateTracker dateTracker;
    private Integer daysForwarded;
    private MainViewModel activityModel;
    private boolean isShowingList = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this.dateTracker = new SimpleDateTracker();
        this.dateTracker = SuccessoratorApplication.getDateTracker();
        this.daysForwarded = 0;
        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        view.placeholderText.setText(null);

        // this is very bad
        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        setContentView(view.getRoot());
    }


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
            daysForwarded++;
            dateTracker.setForwardBy(daysForwarded);
            dateTracker.update();
            onResume();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(dateTracker.getHour()>=2) {
            setTitle(dateTracker.getDate());
        }
        this.activityModel.clearCompletedGoals();
    }

}