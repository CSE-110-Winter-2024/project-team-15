package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.DateTracker;
import edu.ucsd.cse110.successorator.lib.domain.MockDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final GoalRepository goalRepository;
    private int listShown;

    // UI state
    private final MutableSubject<List<Goal>> orderedGoals;
    private final MutableSubject<Boolean> noGoals;
    //private final MutableSubject<ViewNumInfo> numInfo;

    private final MutableSubject<SimpleDateTracker> dateTracker;


    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepository(), app.getDateTracker());
                    });

    // dateTracker is received here through the app. for usage in further methods.
    public MainViewModel(GoalRepository goalRepository, MutableSubject<SimpleDateTracker> dateTracker) {
        this.goalRepository = goalRepository;
        this.dateTracker = dateTracker;
//        this.numInfo = new SimpleSubject<ViewNumInfo>();
 //       numInfo.setValue(ViewNumInfo.getInstance());
        goalRepository.setLastUpdated(dateTracker.getValue().getDate());

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
                    //Yoav debugged and figured out this solution was necessary
                    .filter(goal -> (goal.listNum() == listShown))
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList()));

            noGoals.setValue(orderedGoals.getValue().size() == 0);
        });

        this.dateTracker.observe(timeChange -> {
            // when the date changes, one of two things have happened:
            // 1. the date was manually changed with the arrow
            // 2. the date was changed in the onResume() of GoalListFragment
            // in case 1, this part makes sure the date reflects the desired date and removes
            // completed goals
            // in case 2, it's redundant but not harmful.
            if(!goalRepository.getLastUpdated().equals(timeChange.getDate()) && timeChange.getHour()>=2) {
                goalRepository.setLastUpdated(timeChange.getDate());
                goalRepository.clearCompletedGoals();
            }
        });
    }
    public MutableSubject<List<Goal>> getOrderedGoals(){
        return orderedGoals;
    }


    // for usage in fragment. to goal or not to goal?
    public MutableSubject<Boolean> getNoGoals() {
        return noGoals;
    }
    public int getListShown(){
        return listShown;
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

    public void clearCompletedGoals() {
        // this method is used only once for the onResume() of GoalListFragment
        var rawDateTracker = dateTracker.getValue();
        if(!goalRepository.getLastUpdated().equals(rawDateTracker.getDate()) && rawDateTracker.getHour()>=2) {
            goalRepository.setLastUpdated(rawDateTracker.getDate());
            goalRepository.clearCompletedGoals();
        }
        rawDateTracker.update();
        dateTracker.setValue(rawDateTracker);
    }


    //idk if this works
    public void switchView(int listNum){

        this.listShown = listNum;
        ViewNumInfo.setInstance(listNum);
        //absolute degenerate behaviour
        //I apologize
        goalRepository.prepend(new Goal("a", Integer.MAX_VALUE, true, Integer.MAX_VALUE, 5));
        goalRepository.remove(Integer.MAX_VALUE);

    }

}