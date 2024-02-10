package edu.ucsd.cse110.successorator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
//        view.placeholderText.setText(R.string.hello_world);
        view.placeholderText.setText(null);

        setContentView(view.getRoot());
    }
    TextView listItemGoalOnClickListener = (TextView) findViewById(R.id.goal_text);
    listItemGoalOnClickListener.setOnClickListener


    public void onClick(View v){

    }
    // for the button
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
