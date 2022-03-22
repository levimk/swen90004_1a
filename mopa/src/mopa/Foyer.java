package mopa;

class Foyer extends Room {
    private Group arrivingGroup;
    private Group departingGroup;
    private GroupStatus groupStatus;

    private enum GroupStatus {
        EMPTY,
        STARTING_TOUR,
        COMPLETED_TOUR
    }
//    private Group tourCompleteGroup;

    public Foyer(Room firstRoom, Room finalRoom) {
        super(-1);
        nextRoom = firstRoom;
        previousRoom = finalRoom;
        groupStatus = GroupStatus.EMPTY;
    }

    /*
      PRODUCER-CONSUMER
    */
    public synchronized void arriveAtMuseum(Group newGroup) throws InterruptedException {
        // wait for the group that has already arrived to actually enter the museum
        while(arrivingGroup != null) wait();
        arrivingGroup = newGroup;
        System.out.println(arrivingGroup + " arrives at the museum");
    }

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
      SECURITY GUARD
    */
    public synchronized boolean enterFrontEntrance() {
//        System.out.println("[enterFrontEntrance]");
        if (arrivingGroup != null && group == null) {
//            System.out.println("[" + arrivingGroup + " moves through the foyer]");
            group = arrivingGroup;
            arrivingGroup = null;
            groupStatus = GroupStatus.STARTING_TOUR;
            notifyAll();
            return true;
        }
//        System.out.println("[enterFrontEntrance] - FALSE");
        return false;
    }

    public synchronized boolean exitFrontEntrance() throws InterruptedException {
//        System.out.println("[exitFrontEntrance]");
        // Only exit if there is a group in the foyer that has completed their tour
        if (group != null && groupStatus.equals(GroupStatus.COMPLETED_TOUR)) {
//            System.out.println(group + " leaving foyer to depart"); // TODO: remove
            // But wait for the group that is outside to actually depart
            while(departingGroup != null) wait();
            departingGroup = group;
            group = null;
            groupStatus = GroupStatus.EMPTY;
            notifyAll();
            return true;
        }
//        System.out.println("[exitFrontEntrance] - FALSE");
        return false;
    }

    public synchronized boolean hasDepartingGroup() {
        return departingGroup != null;
    }

    public synchronized boolean hasArrivingGroup() {
        return arrivingGroup != null;
    }
    /*
      End SECURITY GUARD
    */

    /*
      GUIDE
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