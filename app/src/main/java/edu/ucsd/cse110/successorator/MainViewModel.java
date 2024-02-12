package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;


import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final GoalRepository goalRepository;

    // UI state
    private final MutableSubject<List<Goal>> orderedGoals;
    private final MutableSubject<Boolean> noGoals;

    private final MutableSubject<List<Integer>> goalOrdering;
//    private final MutableSubject<String> displayedText;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepository());
                    });

    public MainViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;

        /* PLANS:
         * 1. Observe goalRepository so that when it changes, the updated list of goals
         *    is given to the ListView.
         *    (might need to add an observer to GoalListFragment)
         * 2. When goalRepository changes, update noGoals to be True iff goalRepository
         *    is empty.
         * 3. When noGoals changes, either enable or disable the TextView that displays
         *    the empty goal list (ITERATION 2)
         */
        // Create the observable subjects.
        this.goalOrdering = new SimpleSubject<>();
        this.orderedGoals = new SimpleSubject<>();
        this.noGoals = new SimpleSubject<>();

        // Initialize...
        noGoals.setValue(true);

        // When the list of goals changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(goals -> {
            if (goals == null) { // not ready yet, ignore
                noGoals.setValue(true); // No goals available
                orderedGoals.setValue(Collections.emptyList()); // Clear the list
                return;
            }
            // sort here

            var ordering = new ArrayList<Integer>();
            for (int i = 0; i < goals.size(); i++) {
                ordering.add(i);
            }
            goalOrdering.setValue(ordering); // order of goals
            noGoals.setValue(false); // now there are goals
        });

        // if ordering changes, then update ordered goals
        goalOrdering.observe(ordering -> {
            if (ordering == null) return;

            var goals = new ArrayList<Goal>();
            for(var id: ordering){
                var goal = goalRepository.find(id).getValue();
                if (goal == null) return; // don't need to set noGoals here
                goals.add(goal);
            }

            this.orderedGoals.setValue(goals);

        });
    }
    public MutableSubject<List<Goal>> getOrderedGoals(){
        return orderedGoals;
    }

    // for usage in fragment. to goal or not to goal?
    public MutableSubject<Boolean> getNoGoals() {
        return noGoals;
    }

}