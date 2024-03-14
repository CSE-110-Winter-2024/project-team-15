package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;


import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.DateTracker;
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

        //this is setting last updated field without recurrences getting triggered, can explain
        //the problem in morning
        goalRepository.setLastUpdated(dateTracker.getValue().getDate(),
                ComplexDateTracker.getInstance().getValue().getYear());

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

        // handling time change encapsulated :)
        this.dateTracker.observe(timeChange -> {
            assert timeChange != null; // the compiler said so ...
            handleDateChange(timeChange);
        });

    }

    public MutableSubject<List<Goal>> getOrderedGoals(){
        return orderedGoals;
    }


    // for usage in fragment. to goal or not to goal?
    public MutableSubject<Boolean> getNoGoals() {
        return noGoals;
    }

    // this is a function now so we can test it
    public void handleDateChange(DateTracker timeChange) {
        // when the date changes, one of two things have happened:
        // 1. the date was manually changed with the arrow
        // 2. the date was changed in the onResume() of GoalListFragment
        // in case 1, this part makes sure the date reflects the desired date and removes
        // completed goals
        // in case 2, it's redundant but not harmful.
        if (!goalRepository.getLastUpdated().equals(timeChange.getDate()) && timeChange.getHour() >= 2) {
            goalRepository.clearCompletedGoals();

            int dayOfMonth = dateTracker.getValue().getNextDateDayOfMonth();
            int monthOfYear = dateTracker.getValue().getNextDateMonthOfYear();
            int year = dateTracker.getValue().getNextDateYear();
            int dayOfWeek = dateTracker.getValue().getNextDateDayOfWeek();
            int weekOfMonth = dateTracker.getValue().getNextDateWeekOfMonth();
            boolean isLeapYear = dateTracker.getValue().getNextDateIsLeapYear();

            goalRepository.addRecurrencesToTomorrowForDate(dayOfMonth, monthOfYear, year, dayOfWeek, weekOfMonth, isLeapYear);

            //moved below addRecurrences for timing reasons, can explain in the morning
            goalRepository.setLastUpdated(timeChange.getDate(), ComplexDateTracker.getInstance().getValue().getYear());
            goalRepository.setLastRecurrence(ComplexDateTracker.getInstance().getValue().getDateTime().toLocalDate());
        }
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
            goalRepository.setLastUpdated(rawDateTracker.getDate(), rawDateTracker.getYear());

            goalRepository.clearCompletedGoals();
            //need to run recurrences here so they work when app is opened rather than just when mocking

            int dayOfMonth = dateTracker.getValue().getNextDateDayOfMonth();
            int monthOfYear = dateTracker.getValue().getNextDateMonthOfYear();
            int year = dateTracker.getValue().getNextDateYear();
            int dayOfWeek = dateTracker.getValue().getNextDateDayOfWeek();
            int weekOfMonth = dateTracker.getValue().getNextDateWeekOfMonth();
            boolean isLeapYear = dateTracker.getValue().getNextDateIsLeapYear();

            goalRepository.addRecurrencesToTomorrowForDate(dayOfMonth, monthOfYear, year,
                    dayOfWeek, weekOfMonth, isLeapYear);

            goalRepository.setLastRecurrence(ComplexDateTracker.getInstance().getValue().
                    getDateTime().toLocalDate());
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

    // Add a method to handle positive button click action from the dialog
    public void createRecurringGoal(String goalText, int recurrenceType, LocalDateTime representation) {
        if(!goalText.equals("")) {
            int dayOfWeekToRecur = representation.getDayOfWeek().getValue(); // 1 is Monday.
            int weekOfMonthToRecur = dateTracker.getValue().getWeekOfMonth(representation);
            var goal = new Goal(goalText, null, false, -1, getListShown());
            goal = goal.withRecurrenceData(recurrenceType, representation.getDayOfMonth(),
                    representation.getMonthValue(), representation.getYear(),
                    dayOfWeekToRecur, weekOfMonthToRecur, false);

            insertIncompleteGoal(goal);
        }
    }

    // Add a method to resolve the recurrence type based on selected radio button id
    public int resolveRecurrenceType(int recurrenceId) {
        if(recurrenceId == R.id.daily_button){
            return 1;
        } else if(recurrenceId == R.id.weekly_button){
            return 2;
        } else if(recurrenceId == R.id.monthly_button){
            return 3;
        } else if(recurrenceId == R.id.yearly_button){
            return 4;
        }
        return 0; // default or unknown type
    }


}