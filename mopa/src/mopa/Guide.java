package mopa;

public class Guide extends Thread {
    private String id;
    private Room room;
    private Room nextRoom;
    private Location location;

    private enum Location {
        OWN_ROOM,
        NEXT_ROOM,
    }

    public Guide(Room room, Room nextRoom) {
        this.room = room;
        this.location = Location.OWN_ROOM;
        this.nextRoom = nextRoom;
        this.id = room.getId() == -1 ? "f" : Integer.toString(room.getId());
        nameThread();
    }

    private void nameThread() {
        String threadName = room.getClass().getName().equals("Foyer") ? "G-Foyer" : "G-" + id;
        Thread.currentThread().setName(threadName);
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                if (location.equals(Location.OWN_ROOM) && room.hasGroup()) {
                    Group escortingGroup = room.leaveRoom();
                    sleep(Params.WALKING_TIME);
                    changeLocation(Location.NEXT_ROOM);
                    nextRoom.enterRoom(escortingGroup);
                    returnToOwnRoom();
                }
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    private void returnToOwnRoom() throws InterruptedException {
        sleep(Params.WALKING_TIME);
        changeLocation(Location.OWN_ROOM);
    }

    private void changeLocation(Location newLocation) throws Error {
        if (location.equals(newLocation)) throw new Error("Guide error: attempting to change to current location (" + location + ")" );
        location = newLocation;
    }

    @Override
    public String toString() {
        return "Guide #" + id + " (" + room + ", " + nextRoom + ")";
    }
}
