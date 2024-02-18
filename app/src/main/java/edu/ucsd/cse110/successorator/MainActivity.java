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
import edu.ucsd.cse110.successorator.ui.goallist.dialog.CreateGoalDialogFragment;

public class MainActivity extends AppCompatActivity {
    private DateTracker dateTracker;
    private boolean isShowingList = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dateTracker = new SimpleDateTracker();
        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
//        view.placeholderText.setText(R.string.hello_world);
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
        if (item.getItemId() == R.id.action_bar_menu_swap_views){

            var dialogFragment = CreateGoalDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "CreateGoalDialogFragment");

        } else if (item.getItemId() == R.id.action_bar_menu_forward_day){
//            dateTracker = new MockDateTracker();
//            setTitle(dateTracker.getDate());
            dateTracker.forwardUpdate();
            setTitle(dateTracker.getDate());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(dateTracker.getHour()>=2){
            setTitle(dateTracker.getDate());
        }
    }

}