// Name: Levi McKenzie-Kirkbright
// Student ID: 583 703

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

    /**
     * Constructore for the room
     * @param id : the ID of the room
     */
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

    /**
     * Tell the caller if this room has a group
     * @return : true if and only if the room has a group
     */
    public synchronized boolean hasGroup() {
        return group != null;
    }

    /**
     * Getter for the next room in the tour
     * @return Room: the next room
     */
    public Room getNextRoom() { return nextRoom; }

    /**
     * Setter: connect this room to the previous room
     * @param room : the previous room
     * @throws Exception : prevent the previous room from being overridden
     */
    public void setPreviousRoom(Room room) throws Exception {
        if (this.previousRoom == null) {
            this.previousRoom = room;
        } else {
            throw new Exception("Error: previous room already set for " + this);
        }
    }

    /**
     * Setter: connect this room to the next room
     * @param room : the next room
     * @throws Exception : prevent the next room from being overridden
     */
    public void setNextRoom(Room room) throws Exception {
        if (this.nextRoom == null) {
            this.nextRoom = room;
        } else {
            throw new Exception("Error: next room already set for " + this);
        }
    }

    /**
     * Getter for this room's ID
     * @return int: the room's ID
     */
    public int getId() { return id; }

    /**
     * String representation of the room
     * @return string representing the room
     */
    public String toString() { return "room " + id; }
}
