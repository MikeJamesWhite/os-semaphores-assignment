/**
 * Person.java
 * Class which represents a taxi which is tasked with transporting People when hailed.
 * 
 * Author: Mike James White
 * Date: 19/05/2018
 */

enum Direction {
    OUTBOUND, INBOUND, WAITING;
} 

public class Taxi extends Thread {
    private static Taxi taxi;
    private static int currentBranch;
    private static Direction direction;
    private static long startTime;

    // construct a new taxi object
    public Taxi () { taxi = this; currentBranch = 0; direction = Direction.OUTBOUND; startTime = System.currentTimeMillis(); }
    
    // note a new hail by a Person for pickup at a specific branch
    public void hail(int originBranch, int personID) {
        System.out.println(getCurrentTime() + " branch " + originBranch + ": person " + personID + " hail");
    }

    // note a new dropoff request by a Person for a specific branch
    public void request(int requestedBranch, int personID) {
        System.out.println(getCurrentTime() + " branch " + currentBranch + ": person " + personID + " request " + requestedBranch);
    }

    // move to the next branch in the current direction
    public void advance() throws InterruptedException {
        System.out.println(getCurrentTime() + " branch " + currentBranch + ": taxi depart");
        switch (direction) {
            case OUTBOUND:
                currentBranch += 1;
                break;
            
            case INBOUND:
                currentBranch -= 1;
                break;
            
            default: System.err.println("Error: no direction...");
        }
        sleep(2 * Simulator.MILLIS_TO_MINUTES);

        System.out.println(getCurrentTime() + " branch " + currentBranch + ": taxi arrive");
    }

    // stop at the current branch
    public void pause() {

    }

    public Taxi getTaxi() {
        return taxi;
    }

    // gets the current simulation time as a String
    public static String getCurrentTime() {
        long timePassed = System.currentTimeMillis() - startTime;
        int minutes = (int) (timePassed / Simulator.MILLIS_TO_MINUTES);
        int hours = minutes / 60;
        minutes -= hours * 60;

        if (minutes >= 10) return "" + hours + ":" + minutes;
        else return "" + (Simulator.START_HOUR + hours) % 24 + ":0" + minutes;
    }

}