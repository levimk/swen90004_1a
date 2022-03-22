//package mopa;
//
//
//import java.util.concurrent.locks.ReentrantLock;
//
//public class FoyerOld extends Room {
//    private volatile Group arrivingGroup;
//    private volatile Group departingGroup;
//    private volatile GroupStatus groupStatus;
//    private enum GroupStatus { NEW_GROUP, OLD_GROUP, EMPTY }
//    private ReentrantLock lock;
//
//    public FoyerOld(Room nextRoom, Room previousRoom) {
//        super(-1);
//        super.setNextRoom(nextRoom);
//        super.setPreviousRoom(previousRoom);
//        groupStatus = GroupStatus.EMPTY;
//        this.lock = new ReentrantLock();
//    }
//
//    public synchronized void lock() {
//        lock.lock();
//    }
//
//    public synchronized void unlock() {
//        lock.unlock();
//    }
//
//    public synchronized void notifyWaiters() {
//        if (lock.isHeldByCurrentThread()) lock.notifyAll();
//    }
//
//    public synchronized void arriveAtMuseum(Group group) throws InterruptedException {
//        while(this.arrivingGroup != null) {
//            wait();
//        }
//        this.arrivingGroup = group;
//        System.out.println(this.arrivingGroup + " arrives at the museum");
//    }
//
//    @Override
//    public synchronized void enterRoom(Group enteringGroup) throws InterruptedException {
//        System.out.println(enteringGroup + " attempting to enter " + this);
//        while(this.group != null || this.lock.isLocked()) {
//            wait();
//        }
//        lock.lock();
//        try {
//            if (arrivingGroup == enteringGroup) throw new Error("Security guard should not be calling enterRoom");
//
//            this.groupStatus = GroupStatus.OLD_GROUP;
//            this.group = enteringGroup;
//            System.out.println(enteringGroup + " enters " + this);
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public synchronized boolean groupCanEnter() {
//        return hasArrivingGroup() && !hasGroupAwaitingForFinalSecurityCheck();
//    }
//
//    public synchronized boolean enterMuseum(boolean groupCanEnter) throws InterruptedException {
//        if (!groupCanEnter) return false;
//        System.out.println("\t\t\t\tF " + arrivingGroup +" entering foyer through security check");
//        while(this.group != null || lock.isLocked()) {
//            System.out.println("lock lock lock");
//            wait();
//        }
//        lock.lock();
//        try {
//            this.group = this.arrivingGroup;
//            this.groupStatus = GroupStatus.NEW_GROUP;
//            this.arrivingGroup = null;
//            System.out.println("\t\t\t\tF2 " + arrivingGroup +" --->");
//        } finally {
//            lock.unlock();
//            return true;
//        }
//    }
//
//    public synchronized void exitMuseum() throws InterruptedException {
//        while(this.departingGroup != null) {
//            wait();
//        }
//        lock.lock();
//        try {
//            if (this.groupStatus.equals(GroupStatus.OLD_GROUP)) {
//                this.departingGroup = this.group;
//                this.group = null;
//            }
//        } finally {
//            lock.unlock();
//        }
//    }
//
//
//    public synchronized void departFromMuseum() throws InterruptedException {
//        if(this.departingGroup != null) {
//            System.out.println(this.departingGroup + " departs from the museum");
//            this.departingGroup = null;
//            notifyAll();
//        }
//    }
//
//    public synchronized boolean hasArrivingGroup() {
//        return this.arrivingGroup != null;
//    }
//
//    public synchronized boolean hasDepartingGroup() { return this.departingGroup != null; }
//
//    @Override
//    public String toString() { return "the foyer"; }
//
//    public synchronized boolean hasGroupAwaitingForFinalSecurityCheck() {
//        System.out.println(group + " hasGroupAwaitingForFinalSecurityCheck$$$$$$$$$$$$$$");
////        if (!lock.isLocked()) lock.lock();
//        lock.lock();
//        boolean result = false;
//        try {
//            if (this.group != null
//                    && this.groupStatus != null
//                    && this.groupStatus.equals(GroupStatus.OLD_GROUP)) {
//                System.out.println(group + " is waiting for final security check*************");
//                result = true;
//            } else {
//                System.out.println(group + " is not waiting for final security check ###############");
//                result = false;
//            }
//        } finally {
//            lock.unlock();
//            return result;
//        }
//    }
//
//    public synchronized boolean hasGroup() {
//        lock.lock();
//        return this.group != null;
//    }
//
//    public synchronized boolean isGroupWaitingToDepart() {
////        lock.lock();
//        if (!lock.isLocked()) lock.lock();
//        System.out.println("isGroupWaitingToDepart?");
//        return this.groupStatus != null && this.groupStatus.equals(GroupStatus.OLD_GROUP);
//    }
//
//    public synchronized boolean hasNewGroupAwaitingToEnterFirstRoom() {
//        lock.lock();
//        return this.group != null && this.groupStatus != null && this.groupStatus.equals(GroupStatus.NEW_GROUP);
//    }
//
//    public synchronized Group getArrivingGroup() { return arrivingGroup; }
//}
