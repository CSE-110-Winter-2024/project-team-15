package edu.ucsd.cse110.successorator.lib.domain;

public class Goal {
    private final String contents;
    private Goal(String contents){
        this.contents = contents;
    }
    public String getContents(){
        return this.contents;
    }
    /*
     * make new goals with this static factory method
     * (can change if needed)
     */
    public static Goal withContents(String contents){
        return new Goal(contents);
    }
}
