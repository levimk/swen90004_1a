package mopa;


public class SecurityGuard extends Thread {
    private Foyer foyer;
    private Location location;

    private enum Location {
        OUTSIDE,
        INSIDE,
    }

    public SecurityGuard(Foyer foyer) {
        this.foyer = foyer;
        this.location = Location.OUTSIDE;
        Thread.currentThread().setName("SG");
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                boolean escortedGroupInside, escortedGroupOutside;
                if(iAmOutside()) {
                    if(!foyer.hasArrivingGroup()) sleep(Params.MAX_SECURITY_INTERVAL);
//                    escortedGroupInside = foyer.enterFrontEntrance();
//                    System.out.println("escorted inside? " + escortedGroupInside);
//                    sleep(Params.SECURITY_TIME);
                    sleep(Params.SECURITY_TIME);
                    moveInside(foyer.enterFrontEntrance());
                } else {
                    if(!foyer.hasGroup()) sleep(Params.MAX_SECURITY_INTERVAL);
//                    escortedGroupOutside = foyer.exitFrontEntrance();
//                    sleep(Params.SECURITY_TIME);
//                    moveOutside(escortedGroupOutside);
                    sleep(Params.SECURITY_TIME);
                    moveOutside(foyer.exitFrontEntrance());
                }
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    private boolean iAmOutside() {
        return location.equals(Location.OUTSIDE);
    }

    private void moveInside(boolean escortedGroup) {
        location = Location.INSIDE;
        if (!escortedGroup) System.out.println("security guard goes in");
    }

    private void moveOutside(boolean escortedGroup) {
        location = Location.OUTSIDE;
        if (!escortedGroup) System.out.println("security guard goes out");
    }
}
