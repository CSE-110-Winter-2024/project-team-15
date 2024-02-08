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
    private final MutableSubject<List<Integer>> cardOrdering;
    private final MutableSubject<List<Goal>> orderedCards;
    private final MutableSubject<Goal> topCard;
    private final MutableSubject<Boolean> isShowingFront;
    private final MutableSubject<String> displayedText;

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

        // Create the observable subjects.
        this.cardOrdering = new SimpleSubject<>();
        this.orderedCards = new SimpleSubject<>();
        this.topCard = new SimpleSubject<>();
        this.isShowingFront = new SimpleSubject<>();
        this.displayedText = new SimpleSubject<>();

        // Initialize...
        isShowingFront.setValue(true);

        // When the list of cards changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var ordering = new ArrayList<Integer>();
            for (int i = 0; i < cards.size(); i++) {
                ordering.add(i);
            }
            cardOrdering.setValue(ordering);
        });

        // When the ordering changes, update the ordered card.
        cardOrdering.observe(ordering -> {
            if (ordering == null) return;

            var cards = new ArrayList<Goal>();
            for(var id: ordering){
                var card = goalRepository.find(id).getValue();
                if (card == null) return;
                cards.add(card);
            }
            this.orderedCards.setValue(cards);
            // TODO: see if topCard changes to the right value
            this.topCard.setValue(cards.get(0));
        });

        // When the top card changes, update the displayed text and display the front side.
        topCard.observe(card -> {
            if (card == null) return;

            displayedText.setValue(card.front());
            isShowingFront.setValue(true);
        });

        // When isShowingFront changes, update the displayed text.
        isShowingFront.observe(isShowingFront -> {
            if (isShowingFront == null) return;

            var card = topCard.getValue();
            if (card == null) return;

            var text = isShowingFront ? card.front() : card.back();
            displayedText.setValue(text);
        });
    }

    public MutableSubject<String> getDisplayedText() {
        return displayedText;
    }

    public void flipTopCard() {
        var isShowingFront = this.isShowingFront.getValue();
        if (isShowingFront == null) return;
        this.isShowingFront.setValue(!isShowingFront);
    }

    public void stepForward() {
        var ordering = this.cardOrdering.getValue();
        if (ordering == null) return;

        var newOrdering = new ArrayList<>(ordering);
        Collections.rotate(newOrdering, -1);
        this.cardOrdering.setValue(newOrdering);
    }

    public void stepBackward() {
        var ordering = this.cardOrdering.getValue();
        if (ordering == null) return;

        var newOrdering = new ArrayList<>(ordering);
        Collections.rotate(newOrdering, 1);
        this.cardOrdering.setValue(newOrdering);
    }
    public MutableSubject<List<Goal>> getOrderedCards(){
        return orderedCards;
    }

    public void shuffle() {
        var ordering = this.cardOrdering.getValue();
        if (ordering == null) return;

        var newOrdering = new ArrayList<>(ordering);
        Collections.shuffle(newOrdering);
        this.cardOrdering.setValue(newOrdering);
    }
}
