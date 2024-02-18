package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.DateTracker;
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

    private final DateTracker dateTracker;


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
        this.dateTracker = new DateTracker();
        goalRepository.setLastUpdated(dateTracker.getDate());

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
        this.orderedGoals = new SimpleSubject<>();
        this.noGoals = new SimpleSubject<>();
        this.noGoals.setValue(true);

        goalRepository.findAll().observe(goals -> {
            if (goals == null) return;
            orderedGoals.setValue(goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList()));

            noGoals.setValue(goals.size() == 0);
        });
    }
    public MutableSubject<List<Goal>> getOrderedGoals(){
        return orderedGoals;
    }

    // for usage in fragment. to goal or not to goal?
    public MutableSubject<Boolean> getNoGoals() {
        return noGoals;
    }

    public void toggleCompleted(Goal goal) {
        // SRP issue fixed by forward passing
        goalRepository.toggleCompleteGoal(goal);

    }

    //lab makes dialogFragment call a method that is only
    //in goalRepository using a mainViewModel.  This method is here bc
    //I don't think I can call that method without this being here
    //Ethan blurb
    // we're also going to need to add this to a viewModelTest - Keren
    public void insertIncompleteGoal(Goal goal) {
        goalRepository.insertUnderIncompleteGoals(goal);
    }

    public void clearCompletedGoals(){
        dateTracker.update();
        if(!goalRepository.getLastUpdated().equals(dateTracker.getDate()) && dateTracker.getHour()>2) {
            goalRepository.setLastUpdated(dateTracker.getDate());
            goalRepository.clearCompletedGoals();
        }
    }


}
