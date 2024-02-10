package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Goal {
    // we do not want null strings -- that is lame
    private final @NonNull String contents;
    private final @Nullable Integer id;

    private final @NonNull Boolean completed;
    private final @Nullable int sortOrder;

    public Goal(@NonNull String contents, @Nullable Integer id,
                @NonNull Boolean completed, int sortOrder) {
        this.contents = contents;
        this.id = id;
        this.completed = completed;
        this.sortOrder = sortOrder;
    }

    public @NonNull String contents(){
        return this.contents;
    }
    public @Nullable Integer id(){
        return this.id;
    }
    public @NonNull Boolean completed() { return this.completed; }
    public int sortOrder() {
        return this.sortOrder;
    }
}