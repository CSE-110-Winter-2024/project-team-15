//package edu.ucsd.cse110.successorator.data.db;
//
//import androidx.annotation.Nullable;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import edu.ucsd.cse110.successorator.lib.domain.Goal;
//import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
//import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
//import edu.ucsd.cse110.successorator.lib.util.Observer;
//import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
//import edu.ucsd.cse110.successorator.lib.util.Subject;
//
//public class MockGoalRepository implements GoalRepository {
//
//    //private final GoalsDao goalsDao;
//    private List<Goal> goals;
//    private List<Subject<Goal>> goalSubject;
//    private MutableSubject<List<Goal>> subjectListGoal;
//    private String lastUpdated;
//    private int lastUpdatedYear;
//    private LocalDate localDate;
//    private int largestId;
//
//    public MockGoalRepository( /*GoalsDao goalsDao */) {
//        // Constructor body is typically not commented out as it's needed for instantiation
//        //this.goalsDao = goalsDao;
//        this.goalSubject = new ArrayList<>();
//        this.goals = new ArrayList<>();
//        this.subjectListGoal = new SimpleSubject<>();
//        subjectListGoal.setValue(new ArrayList<>());
//        this.localDate = LocalDate.now();
//        this.largestId = 0;
//    }
//
//    public Subject<Goal> find(int id) {
//        // Commented out the method body
//        /*
//        LiveData<GoalEntity> entityLiveData = goalsDao.findAsLiveData(id);
//        LiveData<Goal> goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
//        return new LiveDataSubjectAdapter<>(goalLiveData);
//        */
//
//        return goalSubject.get(id);
//    }
//
//    public Subject<List<Goal>> findAll() {
//        // Commented out the method body
//        /*
//        var entitiesLiveData = goalsDao.findAllAsLiveData();
//        var goalsLiveData = Transformations.map(entitiesLiveData, entities ->{
//            return entities.stream()
//                    .map(GoalEntity::toGoal)
//                    .collect(Collectors.toList());
//        });
//        return new LiveDataSubjectAdapter<>(goalsLiveData);
//        */
//        return subjectListGoal;
//        //return null;
//
//
//    }
//
//    public void setLastRecurrence(LocalDate last) {
//        // Commented out the method body
//        /*
//        goalsDao.insert(GoalEntity.fromGoal(
//                new Goal("IF YOU SEE THIS, YOU ARE LOST", 9999,
//                        false, 9999, 9999, 5)
//                        .withRecurrenceData(0, last.getDayOfMonth(), last.getMonthValue(),
//                                last.getYear(),0,0)));
//        */
//        this.localDate = last;
//    }
//
//    public LocalDate getLastRecurrence() {
//        // Commented out the method body
//        /*
//        var ourEntity = goalsDao.find(9999);
//        if (ourEntity == null){
//            setLastRecurrence(LocalDate.now());
//            ourEntity = goalsDao.find(9999);
//        }
//        var ourGoal = ourEntity.toGoal();
//        return LocalDate.of(ourGoal.yearStarting(), ourGoal.monthStarting(),
//                ourGoal.dayStarting());
//        */
//        return this.localDate;
//    }
//
//    // All other methods follow the same pattern, so they'll be briefly shown as commented:
//
//    public Subject<List<Goal>> findAll(int listNum) {
//
//        return null;
//    }
//
//    public void save(Goal goal) {
//        // Commented out
//    }
//
//    public void save(List<Goal> goals) {
//        // Commented out
//    }
//
//    public void prepend(Goal goal) {
//        // Commented out
//    }
//
//    public void insertUnderIncompleteGoals(Goal goal) {
//        if (goal.id() == null || goal.id() == -1) {
//            goals.add(goal.withId(largestId));
//            this.largestId++;
//        }
//        else
//            goals.set(goal.id(), goal);
//    }
//
//    public void addDaylies(int day, int month, int year) {
//        // Commented out
//    }
//
//    public void addWeeklies(int day, int month, int year, int dayOfWeek) {
//        // Commented out
//    }
//
//
//    public void addRecurrencesToTomorrowForDate(int day, int month, int year, int dayOfWeek,
//                                                int weekOfMonth, boolean isLeapYear) {
//        for (int i = 0; i < goals.size(); i++) {
//
//            if (goals.get(i).recurrenceType() == 3) {
//                Goal toAdd = goals.get(i).withoutRecurrence().withListNum(1);
//                goals.add(toAdd);
//            }
//
//        }
//
//    }
//
//    public void insertUnderIncompleteGoalsWithContext(Goal goal) {
//        insertUnderIncompleteGoals(goal);
//    }
//
//    public void toggleCompleteGoal(Goal goal) {
//        // Commented out
//    }
//
//    public void remove(int id) {
//        // Commented out
//    }
//
//    public void clearCompletedGoals() {
//        // let sjust make a for loop later
//    }
//
//    public String getLastUpdated() {
//        return this.lastUpdated;
//    }
//
//    public void setLastUpdated(String lastUpdated, int lastUpdatedYear) {
//        this.lastUpdated = lastUpdated;
//        this.lastUpdatedYear = lastUpdatedYear;
//    }
//
//    public Subject<List<Goal>> findAllWithRecurrence() {
//        return null;
//    }
//
//    public void refreshRecurrence() {
//    }
//}
