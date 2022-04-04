// Name: Levi McKenzie-Kirkbright
// Student ID: 583 703

package mopa;

/**
 * The foyer is a specialised room that allows visiting groups to enter and exit the museum safely.
 *
 * @author levim@student.unimelb.edu.au
 *
 */

class Foyer extends Room {
    private Group arrivingGroup;
    private Group departingGroup;
    private GroupStatus groupStatus;

    /**
     * Group statuses help keep track of whether the group in the foyer is entering the museum after arriving or
     * leaving the museum after finishing their tour.
     */
    private enum GroupStatus {
        EMPTY,
        STARTING_TOUR,
        COMPLETED_TOUR
    }

    public Foyer(Room firstRoom, Room finalRoom) {
        super(-1);
        nextRoom = firstRoom;
        previousRoom = finalRoom;
        groupStatus = GroupStatus.EMPTY;
    }

    /*
      PRODUCER-CONSUMER METHODS
    */

    /**
     * Drop off a new group to the museum.
     * @param newGroup : the new group
     * @throws InterruptedException : safely interrupt the thread
     */
    public synchronized void arriveAtMuseum(Group newGroup) throws InterruptedException {
        // wait for the group that has already arrived to actually enter the museum
        while(arrivingGroup != null) wait();
        arrivingGroup = newGroup;
        System.out.println(arrivingGroup + " arrives at the museum");
    }

    /**
     * The group departs the museum.
     * @throws InterruptedException : safely interrupt the thread
     */
    public synchronized void departFromMuseum() throws InterruptedException {
        // wait for a group to exit the foyer
        while(departingGroup == null) wait();
        System.out.println(departingGroup + " departs from the museum");
        departingGroup = null;
        notifyAll();
    }

    /*
      End PRODUCER-CONSUMER
    */

    /*
      SECURITY GUARD METHODS
    */

    /**
     * Security check newly arrived group and bring them into the foyer.
     * @return boolean: true if and only if a group moved from the arrival area into the foyer
     */
    public synchronized boolean enterFrontEntrance() {
        if (arrivingGroup != null && group == null) {
            group = arrivingGroup;
            arrivingGroup = null;
            groupStatus = GroupStatus.STARTING_TOUR;
            notifyAll();
            return true;
        }
        return false;
    }

    /**
     * Security check groups that have completed their tour and bring them out of the foyer to the departure zone.
     * @return boolean: true if and only if a group moved from the foyer area to the departure zone
     */
    public synchronized boolean exitFrontEntrance() throws InterruptedException {
        // Only exit if there is a group in the foyer that has completed their tour
        if (group != null && groupStatus.equals(GroupStatus.COMPLETED_TOUR)) {
            // But wait for the group that is outside to actually depart
            while(departingGroup != null) wait();
            departingGroup = group;
            group = null;
            groupStatus = GroupStatus.EMPTY;
            notifyAll();
            return true;
        }
        return false;
    }

    public synchronized boolean hasArrivingGroup() {
        return arrivingGroup != null;
    }
    /*
      End SECURITY GUARD
    */

    /*
      GUIDE METHODS
    */

    /**
     * Enter the foyer (from the final room), keeping track of the fact that this group is departing (not arriving)
     * @param enteringGroup: the next to enter this room
     * @throws InterruptedException : safely interrupt the thread
     */
    @Override
    public synchronized void enterRoom(Group enteringGroup) throws InterruptedException {
//        System.out.println(enteringGroup + " attempting to enter foyer");
        // Only enter the room when it is empty
        while(this.group != null) {
            wait();
        }
        groupStatus = GroupStatus.COMPLETED_TOUR;
        this.group = enteringGroup;
        System.out.println(enteringGroup + " enters the foyer");
    }

    /**
     * Leave the foyer and enter the first room.
     * @throws InterruptedException : safely interrupt the thread
     */
    @Override
    protected synchronized Group leaveRoom() throws InterruptedException {
        // Only leave the foyer (into room 0) if there is a group that is starting their tour.
        while(group == null || (group != null && !groupStatus.equals(GroupStatus.STARTING_TOUR))) wait();
        System.out.println(group + " leaves the foyer");
        groupStatus = GroupStatus.EMPTY;
        Group leavingGroup = group;
        group = null;
        notifyAll();
        return leavingGroup;
    }
    /*
      End GUIDE
    */
}