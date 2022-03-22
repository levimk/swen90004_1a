package mopa;

/**
 * Each guide escorts the group in his/her respective room.
 *
 * @author levim@student.unimelb.edu.au
 *
 */

public class Room {
    private final int id;
    protected Room nextRoom;
    protected Room previousRoom;
    protected volatile Group group;

    public Room(int id) {
        this.id = id;
    }

    /**
     * Allow a group to (COVID-)safely enter the room (i.e. only let the next group in when it is empty).
     * @param enteringGroup: the next to enter this room
     * @throws InterruptedException : safely interrupt thread
     */
    public synchronized void enterRoom(Group enteringGroup) throws InterruptedException {
        while(this.group != null) {
            wait();
        }
        this.group = enteringGroup;
        System.out.println(enteringGroup + " enters " + this);
    }

    /**
     * The current group has finished and is moving to the next room. Let the next group know.
     * @return Group: the current group
     * @throws InterruptedException : safely interrupt thread
     */
    protected synchronized Group leaveRoom() throws InterruptedException {
        System.out.println(this.group + " leaves " + this);
        Group leavingGroup = this.group;
        this.group = null;
        notifyAll();
        return leavingGroup;
    }

    public synchronized boolean hasGroup() {
        return group != null;
    }

    public Room getNextRoom() { return nextRoom; }

    public void setPreviousRoom(Room room) throws Exception {
        if (this.previousRoom == null) {
            this.previousRoom = room;
        } else {
            throw new Exception("Error: previous room already set for " + this);
        }
    }

    public void setNextRoom(Room room) throws Exception {
        if (this.nextRoom == null) {
            this.nextRoom = room;
        } else {
            throw new Exception("Error: next room already set for " + this);
        }
    }

    public int getId() { return id; }

    public String toString() { return "room " + id; }
}
