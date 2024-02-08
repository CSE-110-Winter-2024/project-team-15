package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

public class Goal {
    // we do not want null strings -- that is lame
    private final @NonNull String contents;
    public Goal(@NonNull String contents){
        this.contents = contents;
    }
    public @NonNull String contents(){
        return this.contents;
    }
    /*
     * make new goals with this static factory method
     * (can change if needed)
     */
    public static Goal withContents(@NonNull String contents){
        return new Goal(contents);
    }
}
