package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Goal {
    // we do not want null strings -- that is lame
    private final @NonNull String contents;
    private final @Nullable Integer id;
    public Goal(@NonNull String contents,
                @Nullable Integer id){
        this.contents = contents;
        this.id = id;
    }
    public @NonNull String contents(){
        return this.contents;
    }
    public @Nullable Integer id(){
        return this.id;
    }
}
