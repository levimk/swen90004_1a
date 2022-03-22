package mopa;

public class Room {
    private int id;
    private enum AdjacentRoomFlags { PREVIOUS, NEXT }
    protected Room nextRoom;
    protected Room previousRoom;
    protected volatile Group group;

    public Room(int id) {
        this.id = id;
    }

    public Group getGroup() { return group; }

    public synchronized void enterRoom(Group enteringGroup) throws InterruptedException {
//        System.out.println(enteringGroup + " attempting to enter " + this);
        while(this.group != null) {
            wait();
        }
        this.group = enteringGroup;
        System.out.println(enteringGroup + " enters " + this);
    }

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

    public void setPreviousRoom(Room room) {
        setAdjacentRoom(AdjacentRoomFlags.PREVIOUS, room);
    }

    public void setNextRoom(Room room) {
        setAdjacentRoom(AdjacentRoomFlags.NEXT, room);
    }

    private void setAdjacentRoom(AdjacentRoomFlags flag, Room room) {
        try {
            if(flag == AdjacentRoomFlags.NEXT && this.nextRoom == null) {
                this.nextRoom = room;
            } else if (flag == AdjacentRoomFlags.PREVIOUS && this.previousRoom == null) {
                this.previousRoom = room;
            }else {
                throw new Exception(flag.toString() + " room already set for " + this);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public int getId() { return id; }

    public String toString() { return "room " + id; }
}
