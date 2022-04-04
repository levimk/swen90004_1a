// Name: Levi McKenzie-Kirkbright
// Student ID: 583 703

package mopa;

/**
 * The "Museum" is more than just brick, mortar and priceless art. It is also its people. It is an institution.
 *
 * @author levim@student.unimelb.edu.au
 *
 */

public class Museum {
    private Building building;
    private final Guide[] guides;
    private final SecurityGuard securityGuard;
    private static Museum instance;

    /**
     * Singleton: only one museum per simulation
     * @param numberOfRooms: the number of rooms to put in the museum
     * @return Museum: the museum instance
     */
    public static Museum getInstance(int numberOfRooms) {
        if(instance == null) {
            instance = new Museum(numberOfRooms);
        }
        return instance;
    }

    /**
     * Constructor for the museum
     * @param numberOfRooms: the number of rooms to put in the museum
     */
    private Museum(int numberOfRooms) {
        try {
            building = new Building(numberOfRooms);
        } catch (Exception e) {
            System.exit(1);
        }
        securityGuard = new SecurityGuard(building.getFoyer());

        int numberOfGuides = numberOfRooms + 1; // 1 guide between every room + 2 in foyer
        guides = new Guide[numberOfGuides];
        guides[0] = new Guide(building.getFoyer(), building.getFoyer().getNextRoom()); // first guide: foyer/room0
        for (int i = 0; i < numberOfRooms; i++) {
            guides[i+1] = new Guide(building.getRoom(i), building.getRoom(i).getNextRoom());
        }
    }

    /**
     * Getter for the foyer
     * @return the building's foyer
     */
    public Foyer getFoyer() {
        return building.getFoyer();
    }

    /**
     * Getter for the security guard
     * @return the building's security guard
     */
    public SecurityGuard getSecurityGuard() {
        return securityGuard;
    }

    /**
     * Getter for the guides
     * @return the building's guides
     */
    public Guide[] getGuides() {
        return guides;
    }

    /**
     * Check that the guide threads are alive
     * @return  boolean: true if and only if all the guide threads are alive
     */
    public boolean areGuidesAlive() {
        boolean guides_alive = true;
        for(Guide guide : getGuides()) {
            guides_alive = guides_alive && guide.isAlive();
        }
        return guides_alive;
    }

    /**
     * Check that the security guard thread is alive
     * @return boolean: true if and only if the guard thread is alive
     */
    public boolean isSecurityGuardAlive() {
        return securityGuard.isAlive();
    }
}
