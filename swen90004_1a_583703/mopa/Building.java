// Name: Levi McKenzie-Kirkbright
// Student ID: 583 703

package mopa;

/**
 * The physical building, complete with a foyer and some rooms.
 *
 * @author levim@student.unimelb.edu.au
 *
 */

public class Building {
    private Foyer foyer;
    private Room[] rooms;

    /**
     * Constructor for the building
     * @param numberOfRooms : the number of rooms in the building (excluding the foyer)
     * @throws Exception : various nested exception may bubble - prevents incorrect initialisation of rooms and foyer
     */
    public Building(int numberOfRooms) throws Exception{
        setupRoomsAndFoyer(numberOfRooms);
    }

    /**
     * Create the rooms, create the foyer, and link them all together.
     * @param numberOfRooms: the number of rooms to put in the building
     */
    private void setupRoomsAndFoyer(int numberOfRooms) throws Exception {
        spawnRooms(numberOfRooms);
        initializeFoyer();
        linkRoomsAndFoyer();
    }

    /**
     * Creates the list of rooms.
     * @param numberOfRooms: the number of rooms to create
     */
    private void spawnRooms(int numberOfRooms) {
        rooms = new Room[numberOfRooms];
        for(int i = 0; i < numberOfRooms; i++) {
            rooms[i] = new Room(i);
        }
    }

    /**
     * Creates the foyer and links it to the first and last rooms
     */
    private void initializeFoyer() throws IndexOutOfBoundsException{
        foyer = new Foyer(rooms[0], rooms[rooms.length-1]);
    }

    /**
     * Links all the rooms to one another and links the foyer to the first and last rooms.
     * @throws NullPointerException: if foyer is null do not attempt to link rooms
     */
    private void linkRoomsAndFoyer() throws Exception {
        if (foyer == null) {
            throw new NullPointerException("Error: foyer must be initialised before linking rooms");
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

    /**
     * Getter for the foyer
     * @return Foyer: the foyer
     */
    public Foyer getFoyer() {
        return foyer;
    }

    /**
     * Getter for individual rooms
     * @param i : index of the room to get
     * @return : room at index i
     * @throws IndexOutOfBoundsException : catch index out of bounds, e.g. if the number of rooms is set to 0
     */
    public Room getRoom(int i) throws IndexOutOfBoundsException { return rooms[i]; }
}
