package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;


import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.ComplexDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final GoalRepository goalRepository;

    // listShown holds the "index" of the view you're in
    // 0 == today, 1 == tmrw, 2 == pending, 3 == recurring
    // you can see this in MainActivity which observes ViewNumInfo
    // this should definitely integrate ViewNumInfo, perhaps later, since we use it here
    //private int listShown;

    // UI state
    private final MutableSubject<List<Goal>> orderedGoals;
    private final MutableSubject<Boolean> noGoals;
    //private final MutableSubject<ViewNumInfo> numInfo;

    //private final MutableSubject<SimpleDateTracker> dateTracker;
    private final MutableSubject<ComplexDateTracker> dateTracker;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepository(), app.getDateTracker());
                    });

    // dateTracker is received here through the app. for usage in further methods.
    public MainViewModel(GoalRepository goalRepository, MutableSubject<ComplexDateTracker> dateTracker) {
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
                    .filter(goal -> (goal.listNum() == getListShown()))
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
                // get with the times old man
                goalRepository.setLastUpdated(timeChange.getDate());
                goalRepository.clearCompletedGoals();

                // we need to do rollover here too

                int dayOfMonth = dateTracker.getValue().getNextDateDayOfMonth();
                int monthOfYear = dateTracker.getValue().getNextDateMonthOfYear();
                int year = dateTracker.getValue().getNextDateYear();
                int dayOfWeek = dateTracker.getValue().getNextDateDayOfWeek();

                //weekOfMonth correlates to how many times dayOfWeek has occurred this month.
                //this value is found by integer dividing the dayOfMonth-1 by 7 and adding one.
                //a value of 1 is subtracted from dayOfMonth so that the 1st-7th are 1st occurrences,
                //8th-14th are second occurrences, and so on.
                int weekOfMonth = ((dayOfMonth-1) / 7) + 1;
                int numDaysInLastMonth = dateTracker.getValue().getNextDateLastMonthNumDays();

                //a weekOfMonth value of 6 is assigned to days which correspond to the 5th week of
                //the previous month.  The last day of a given month for which a weekOfMonth value of
                //6 should be assigned is equal to 35days(5 weeks) - the # of days in the previous month.
                if(dayOfMonth <= (35 - numDaysInLastMonth)  ){
                    weekOfMonth = 6;
                }
                boolean isLeapYear = dateTracker.getValue().getNextDateIsLeapYear();

                goalRepository.addRecurrencesToTomorrowForDate(dayOfMonth, monthOfYear, year,
                        dayOfWeek, weekOfMonth, isLeapYear);

                //get list of recurring goals
                //filter out all the ones that aren't supposed to be refreshed
                //add copies of the remaining recurring goals to the tomorrow view (without recurrence)
                //update the remaining recurring goals with the next date of recurrence

                // adding recurrence to tmrw

                //other method that functions quite well should be intact if I don't get it right
                //goalRepository.refreshRecurrence();

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

    // "index" of the view you're in
    // BTW this should call viewnuminfo's get listShown instead of having a member variable
    // i'm also confused what happens if this is called without an instance of ViewNumInfo
    // (since its not initialized)
    // (ans: ^ INTS are never null so itll just be 0, appropriately too)
    // FIXED
    public int getListShown(){
        //return listShown;
        return ViewNumInfo.getInstance().getValue().getListShown();
    }

    public void toggleCompleted(Goal goal) {
        // SRP issue fixed by forward passing
        goalRepository.toggleCompleteGoal(goal);

    }

    public void recurringRemove(Goal goal){
        goalRepository.remove(goal.id());
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

        // violates SRP but fine for now (let's delete these comments if there isn't a simple fix)
        // shouldn't check date tracker in a clearing goals method
        if(!goalRepository.getLastUpdated().equals(rawDateTracker.getDate()) && rawDateTracker.getHour()>=2) {
            goalRepository.setLastUpdated(rawDateTracker.getDate());

            goalRepository.clearCompletedGoals();
        }
        rawDateTracker.update();
        dateTracker.setValue(rawDateTracker);
    }


    //idk if this works
    //   it does but it should utilize observers and ViewNumInfo in a different way mb later
    //   first in listShown just call getListShown from viewnuminfo
    //   second use observers to notify instead of the hacky way
    public void switchView(int listNum){

        //this.listShown = listNum;
        ViewNumInfo.setInstance(listNum);
        //absolute degenerate behaviour
        //I apologize
        //    i'm so incredibly confused why you do this
        //    answer: per yoav it's to call the notify method
        //    to fix this, we can just observe the list number, but since this works and we
        //    have deadlines it's fine to keep it this way. let's just review later
        //    \ '-' /
        goalRepository.prepend(new Goal("a", Integer.MAX_VALUE, true, Integer.MAX_VALUE, 5));
        goalRepository.remove(Integer.MAX_VALUE);

    }

}