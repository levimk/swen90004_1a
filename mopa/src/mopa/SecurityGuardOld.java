//package mopa;
//
//import java.util.PriorityQueue;
//
//public class SecurityGuardOld extends Thread {
//    private Foyer foyer;
//    private Location location;
//    private boolean finishedWaiting;
//    private PriorityQueue<Group> groupQueue;
//
//    private enum Location {
//        OUTSIDE_MUSEUM,
//        INSIDE_MUSEUM,
//    }
//
//    public SecurityGuardOld(Foyer foyer) {
//        this.foyer = foyer;
//        this.finishedWaiting = false;
//        this.location = Location.OUTSIDE_MUSEUM;
//        this.groupQueue = new PriorityQueue<>();
//        Thread.currentThread().setName("SG");
//    }
//
//    @Override
//    public void run() {
//        while(!isInterrupted()) {
//            try {
//                if(location.equals(Location.INSIDE_MUSEUM)) {
//                    handleInsideState();
//                } else {
//                    handleOutsideState();
//                }
//            } catch (InterruptedException e) {
//                this.interrupt();
//            }
//        }
//    }
//
//    private void handleInsideState() throws InterruptedException {
//        if (location.equals(Location.OUTSIDE_MUSEUM)) {
//            throw new Error("Security guard location error: handling inside state from outide");
//        }
////        foyer.lock();
//        try {
//            if (foyer.hasGroupAwaitingForFinalSecurityCheck()) {
//                sleep(Params.SECURITY_TIME);
//                foyer.exitMuseum();
//                moveLocation(Location.OUTSIDE_MUSEUM);
//                startWaiting();
//            } else {
//                if(finishedWaiting) {
//                    moveLocation(Location.OUTSIDE_MUSEUM);
//                    System.out.println("security guard goes out");
//                    startWaiting();
//                    sleep(Params.SECURITY_TIME);
//
//                } else {
//                    sleep(Params.MAX_SECURITY_INTERVAL); // ?
//                    finishWaiting();
//                }
//            }
//        } finally {
////            foyer.unlock();
//        }
//    }
//
//    private void handleOutsideState() throws InterruptedException {
//        if (location.equals(Location.INSIDE_MUSEUM)) {
//            throw new Error("Security guard location error: handling outside state from inside");
//        }
////        foyer.lock();
//        System.out.println("SG: handling outside state");
//        try {
//            if (foyer.hasGroupAwaitingForFinalSecurityCheck()) {
//                System.out.println("SG: group waiting for SC");
//                moveLocation(Location.INSIDE_MUSEUM);
//                sleep(Params.SECURITY_TIME);
//                System.out.println("security guard goes in");
//                finishWaiting();
//            } else {
//                boolean escortedGroup = foyer.enterMuseum(foyer.groupCanEnter());
//                if (escortedGroup) {
//                    System.out.println("SG: escorting arriving group");
////                    foyer.enterMuseum();
//                    System.out.println("-- escorting new group into foyer --");
//                    System.out.println("\t\t\t\tSG escorting " + foyer.getArrivingGroup() +" into foyer via security check");
//                    sleep(Params.SECURITY_TIME);
//                    moveLocation(Location.INSIDE_MUSEUM);
//                }
////                if(foyer.hasArrivingGroup()) {
////                    System.out.println("SG: escorting arriving group");
////                    foyer.enterMuseum();
////                    System.out.println("-- escorting new group into foyer --");
////                    System.out.println("\t\t\t\tSG escorting " + foyer.getArrivingGroup() +" into foyer via security check");
////                    sleep(Params.SECURITY_TIME);
////                    moveLocation(Location.INSIDE_MUSEUM);
////                }
//                else {
//                    if(finishedWaiting) {
//                        System.out.println("SG: going inside without group");
//                        moveLocation(Location.INSIDE_MUSEUM);
//                        System.out.println("security guard goes in");
//                        startWaiting();
//                        sleep(Params.SECURITY_TIME);
//                    } else {
//                        System.out.println("SG: waiting outside");
//                        sleep(Params.MAX_SECURITY_INTERVAL); // ?
//                        finishWaiting();
//                    }
//                }
//            }
//        } finally {
////            foyer.unlock();
//        }
//    }
//
//    private void moveLocation(Location newLocation) {
//        System.out.println("\t\tSG moved to: " + newLocation);
//        this.location = newLocation;
//    }
//
//    private void finishWaiting() { this.finishedWaiting = true; }
//    private void startWaiting() {
//        this.finishedWaiting = false;
//    }
//}
