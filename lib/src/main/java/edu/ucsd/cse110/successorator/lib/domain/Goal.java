package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Goal {
    // we do not want null strings -- that is lame
    private final @NonNull String contents;
    private final @Nullable Integer id;

    private final @NonNull Boolean completed;

    public Goal(@NonNull String contents,
                @Nullable Integer id, @NonNull Boolean completed) {
        this.contents = contents;
        this.id = id;
        this.completed = completed;
    }

    public @NonNull String contents(){
        return this.contents;
    }
    public @Nullable Integer id(){
        return this.id;
    }
    public @NonNull Boolean completed() { return this.completed; }
}
