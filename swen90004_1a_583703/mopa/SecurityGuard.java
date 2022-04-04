// Name: Levi McKenzie-Kirkbright
// Student ID: 583 703

package mopa;

/**
 * The guard that performs security checks and makes sure that there is only ever one group in the foyer at a time.
 *
 * @author levim@student.unimelb.edu.au
 *
 */

public class SecurityGuard extends Thread {
    private Foyer foyer;
    private Location location;

    /**
     * The guard is either inside the foyer or outside in the arrival/departure area
     */
    private enum Location {
        OUTSIDE,
        INSIDE,
    }

    /**
     * Constructore for the security guard
     * @param foyer : the foyer the security guard is responsible for
     */
    public SecurityGuard(Foyer foyer) {
        this.foyer = foyer;
        this.location = Location.OUTSIDE;
        Thread.currentThread().setName("SG");
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                if(iAmOutside()) {
                    // Wait for a new group to arrive at the museum then before moving inside, either by
                    // him/herself or whilst taking the new group through the security check to the foyer.
                    if(!foyer.hasArrivingGroup()) sleep(Params.MAX_SECURITY_INTERVAL);
                    sleep(Params.SECURITY_TIME);
                    moveInside(foyer.enterFrontEntrance());
                } else {
                    // Wait for an old group to enter the foyer to depart the museum before moving outside, either by
                    // him/herself or whilst taking the old group through the security check to the departure area.
                    if(!foyer.hasGroup()) sleep(Params.MAX_SECURITY_INTERVAL);
                    sleep(Params.SECURITY_TIME);
                    moveOutside(foyer.exitFrontEntrance());
                }
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    /**
     * Check is the guard is outside.
     * @return : true if and only if the guard is currently outside
     */
    private boolean iAmOutside() {
        return location.equals(Location.OUTSIDE);
    }

    /**
     * Guard moves inside
     * @param withGroup : print a message if and only if the guard moves inside without a group
     */
    private void moveInside(boolean withGroup) {
        location = Location.INSIDE;
        if (!withGroup) System.out.println("security guard goes in");
    }

    /**
     * Guard moves outside
     * @param withGroup : print a message if and only if the guard moves outside without a group
     */
    private void moveOutside(boolean withGroup) {
        location = Location.OUTSIDE;
        if (!withGroup) System.out.println("security guard goes out");
    }
}
