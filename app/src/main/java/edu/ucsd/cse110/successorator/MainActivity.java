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
import edu.ucsd.cse110.successorator.ui.goallist.dialog.SwitchViewDialogFragment;

public class MainActivity extends AppCompatActivity {
    private MutableSubject<SimpleDateTracker> dateTracker;
    private Integer daysForwarded;
    private boolean isShowingList = true;
    private String dayOfWeek;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this.dateTracker = SimpleDateTracker.getInstance();
        this.dateTracker = SimpleDateTracker.getInstance();
        this.daysForwarded = 0;
        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        view.placeholderText.setText(null);

        setContentView(view.getRoot());

        ViewNumInfo.getInstance().observe(vni -> {
            var rawDateTracker = dateTracker.getValue();
            String dateString = rawDateTracker.getDate();
            int listShown = vni.getListShown();
            switch (listShown){
                case 0:
                    dayOfWeek = "Today, ";
                    break;
                case 1:
                    dayOfWeek = "Tomorrow, ";
                    dateString = rawDateTracker.getNextDate();
                    break;
                case 2:
                    dayOfWeek = "Pending";
                    dateString = "";
                    break;
                case 3:
                    dayOfWeek = "Recurring";
                    dateString = "";
                    break;
                default:
                    dayOfWeek = "invalid";
            }
            setTitle(dayOfWeek + dateString);

        });
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
        else if  (item.getItemId() == R.id.action_bar_menu_swap_lists){
            var dialogFragment = SwitchViewDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "SwitchViewDialogFragment");
        }

        return super.onOptionsItemSelected(item);
    }

    // this violates OCP since we might want to change Today to something else
    // i suggest we replace the strings in the if statement with numbers from ViewNumInfo
    // today and tomorrow
    @Override
    public void onResume(){
        super.onResume();
        var rawDateTracker = dateTracker.getValue();
        int listNum = ViewNumInfo.getInstance().getValue().getListShown();

        if(rawDateTracker.getHour()>=2) {
            if(dayOfWeek.equals("Today, ")){
                setTitle(dayOfWeek + rawDateTracker.getDate());
            } else if(dayOfWeek.equals("Tomorrow, ")){

                setTitle(dayOfWeek + rawDateTracker.getNextDate());
            }
            else{setTitle(dayOfWeek);}
        }

    }

}