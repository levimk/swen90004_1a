package mopa;

public class Building {
    private Foyer foyer;
    private Room[] rooms;

    public Building(int numberOfRooms) {
        setupRoomsAndFoyer(numberOfRooms);
    }

    private void setupRoomsAndFoyer(int numberOfRooms) {
        spawnRooms(numberOfRooms);
        initializeFoyer();
        linkRoomsAndFoyer();
    }

    private void spawnRooms(int numberOfRooms) {
        rooms = new Room[numberOfRooms];
        for(int i = 0; i < numberOfRooms; i++) {
            rooms[i] = new Room(i);
        }
    }

    private void initializeFoyer() {
        foyer = new Foyer(rooms[0], rooms[rooms.length-1]);
    }

    private void linkRoomsAndFoyer() throws Error {
        if (foyer == null) {
            throw new Error("Error: foyer must be initialised before linking rooms");
        }

        // link the first room to the foyer
        rooms[0].setPreviousRoom(foyer);

        // only link "intermediate rooms" if there are 2 or more rooms
        if (rooms.length > 1) {
            // link the first room to the second room
            rooms[0].setNextRoom(rooms[1]);

            // link all the rooms except the first and last rooms
            for(int j = 1; j < rooms.length - 1; j++) {
                rooms[j].setPreviousRoom(rooms[j - 1]);
                rooms[j].setNextRoom(rooms[j + 1]);
            }

            // link the final room to the penultimate room
            rooms[rooms.length-1].setPreviousRoom(rooms[rooms.length-2]);
        }

        // link the final room to the foyer
        rooms[rooms.length-1].setNextRoom(foyer);
    }

    public Foyer getFoyer() {
        return foyer;
    }

    public Room getRoom(int i) throws IndexOutOfBoundsException { return rooms[i]; }
}
