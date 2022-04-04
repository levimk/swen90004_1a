// Name: Levi McKenzie-Kirkbright
// Student ID: 583 703

package mopa;

/**
 * Each guide escorts the group in his/her respective room.
 *
 * @author levim@student.unimelb.edu.au
 *
 */

public class Guide extends Thread {
    private final String id;
    private final Room room;
    private final Room nextRoom;
    private Location location;

    /**
     * A guide is either at their own room or waiting with the group for the next room to become empty
     */
    private enum Location {
        OWN_ROOM,
        NEXT_ROOM,
    }

    /**
     * Constructor for the guide
     * @param room: the guide's room
     * @param nextRoom: the next room that groups will go to in their tour
     */
    public Guide(Room room, Room nextRoom) {
        this.room = room;
        this.location = Location.OWN_ROOM;
        this.nextRoom = nextRoom;
        this.id = room.getId() == -1 ? "f" : Integer.toString(room.getId());
        nameThread();
    }

    /**
     * Name the thread
     */
    private void nameThread() {
        String threadName = room.getClass().getName().equals("Foyer") ? "G-Foyer" : "G-" + id;
        Thread.currentThread().setName(threadName);
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                // If the guide is in their own room, and it has a group
                if (location.equals(Location.OWN_ROOM) && room.hasGroup()) {
                    // Escort the group to the next room
                    Group escortingGroup = room.leaveRoom();
                    sleep(Params.WALKING_TIME);
                    changeLocation(Location.NEXT_ROOM); // Guide is now at entrance to the next room
                    nextRoom.enterRoom(escortingGroup); // Guide waits with the group until the next room is empty
                    returnToOwnRoom(); // guide returns to his/her room
                }
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    /**
     * Return the guide from the entrance of the next room to his/her own room.
     * @throws InterruptedException : safely interrupt the thread
     */
    private void returnToOwnRoom() throws InterruptedException {
        sleep(Params.WALKING_TIME);
        changeLocation(Location.OWN_ROOM);
    }

    /**
     * Change the location of the guide
     * @param newLocation: the guide moves back and forth between his/her own room and the next room in the museum
     * @throws Error : cannot change location to the current location
     */
    private void changeLocation(Location newLocation) throws Error {
        if (location.equals(newLocation)) throw new Error("Guide error: attempting to change to current location (" + location + ")" );
        location = newLocation;
    }

    /**
     * String representation of the Guide
     * @return String representation of the Guide
     */
    @Override
    public String toString() {
        return "Guide #" + id + " (" + room + ", " + nextRoom + ")";
    }
}
