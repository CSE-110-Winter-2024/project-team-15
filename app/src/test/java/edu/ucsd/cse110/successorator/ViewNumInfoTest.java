package edu.ucsd.cse110.successorator;

import static org.junit.Assert.*;

import org.junit.Test;

public class ViewNumInfoTest {

    // you can remove the ugly ** comments if needed
    // it just separates the different tests
    @Test
    public void getInstance() {
        ViewNumInfo.resetInstance();

        /* ************************************************************************************* */
        // Checking that we get a new instance if there's not one already..
        // Given: No instances of ViewNumInfo

        // When: getInstance is called for the first time
        ViewNumInfo instance1 = ViewNumInfo.getInstance().getValue();

        // Then: A new instance should be created
        assertNotNull(instance1);

        /* ************************************************************************************* */
        // Checking that we always use the same instance..
        // Given: Now there's an existing instance of ViewNumInfo
        // When: getInstance is called again
        ViewNumInfo instance2 = ViewNumInfo.getInstance().getValue();

        // Then: They're the same instance (singleton pattern ftw)
        assertEquals(instance1, instance2);

        /* ************************************************************************************* */
    }

    @Test
    public void setInstance() {
        ViewNumInfo.resetInstance();

        /* ************************************************************************************* */
        // Checking if we can set the instance..
        // Not sure if I can call getListShown here because we're still testing it
        // .. but I do it anyway

        // Given: An instance of ViewNumInfo with the default view (which is 0)
        int oldView = ViewNumInfo.getInstance().getValue().getListShown();

        // When: setInstance is called with a new value that is NOT default (2)
        int newView = 2;
        ViewNumInfo.setInstance(newView);

        // Then: The view value of the instance should be updated
        int updatedView = ViewNumInfo.getInstance().getValue().getListShown();
        assertEquals(newView, updatedView);
        assertNotEquals(oldView, updatedView);

        /* ************************************************************************************* */

    }

    @Test
    public void getListShown() {
        ViewNumInfo.resetInstance();

        /* ************************************************************************************* */
        // Checking getting the default view..
        // Given: An instance of ViewNumInfo
        ViewNumInfo instance1 = ViewNumInfo.getInstance().getValue();

        // When: getListShown is called without setting listShown
        int listShown = instance1.getListShown();

        // Then: The default listShown value should be 0
        assertEquals(0, listShown);

        /* ************************************************************************************* */
        // Functionally the same test as "Checking if we can set the instance"
        // Not sure if I can call setInstance here because we're still testing it
        // .. but I do it anyway
        // I can fix this if needed, it might violate unit testing laws ...
        // Tests can be combined, maybe?
        // Checking getting a non-default view..

        // Given: An instance of ViewNumInfo with the default view (which is 0)
        // int oldView = ViewNumInfo.getInstance().getValue().getListShown();

        // When: setInstance is called with a new value that is NOT default (2)
        ViewNumInfo.setInstance(2);

        // Then: The view value of the instance should be updated
        int updatedView = ViewNumInfo.getInstance().getValue().getListShown();
        assertEquals(2, updatedView);

        /* ************************************************************************************* */

    }
}