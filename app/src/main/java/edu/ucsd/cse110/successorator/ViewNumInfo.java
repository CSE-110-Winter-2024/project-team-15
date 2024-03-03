package edu.ucsd.cse110.successorator;

import android.view.View;

import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

// singleton pattern (that's why we use getInstance)
// use the same instance of VNI for everything
// later this can be used to see what dialogs to display
public class ViewNumInfo {
    private int listShown;
    private static MutableSubject<ViewNumInfo> globalListShown = null;
    private ViewNumInfo(int ls){
        this.listShown = ls;
    }
    public static MutableSubject<ViewNumInfo> getInstance(){

        if(globalListShown == null){
            SimpleSubject<ViewNumInfo> temp = new SimpleSubject<ViewNumInfo>();
            temp.setValue(new ViewNumInfo(0));
            globalListShown = temp;
        }
        return globalListShown;
    }
    public static void setInstance(int ls){
        getInstance();
        globalListShown.setValue(new ViewNumInfo(ls));
    }
    public int getListShown(){
        return listShown;
    }
}
