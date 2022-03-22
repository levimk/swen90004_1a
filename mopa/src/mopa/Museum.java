package mopa;

//import java.util.Arrays;

public class Museum {
    private Building building;
    private Guide[] guides;
    private SecurityGuard securityGuard;
    private static Museum instance;

    public static Museum getInstance(int numberOfRooms) {
        if(instance == null) {
            instance = new Museum(numberOfRooms);
        }
        return instance;
    }

    private Museum(int numberOfRooms) {
        building = new Building(numberOfRooms);
        securityGuard = new SecurityGuard(building.getFoyer());

        int numberOfGuides = numberOfRooms + 1; // 1 guide between every room + 2 in foyer
        guides = new Guide[numberOfGuides];
        guides[0] = new Guide(building.getFoyer(), building.getFoyer().getNextRoom()); // first guide: foyer/room0
        for (int i = 0; i < numberOfRooms; i++) {
            guides[i+1] = new Guide(building.getRoom(i), building.getRoom(i).getNextRoom());
        }
    }

    public Foyer getFoyer() {
        return building.getFoyer();
    }

    public SecurityGuard getSecurityGuard() {
        return securityGuard;
    }

    public Guide[] getGuides() {
        return guides;
    }

    public boolean areGuidesAlive() {
        boolean guides_alive = true;
        for(Guide guide : getGuides()) {
            guides_alive = guides_alive && guide.isAlive();
        }
        return guides_alive;
    }

    public boolean isSecurityGuardAlive() {
        return securityGuard.isAlive();
    }
}
