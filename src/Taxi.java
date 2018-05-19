import java.util.concurrent.Semaphore;

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
    private static int numPeople;
    private static Semaphore[] branches;
    private boolean start = true;
    private boolean stopped = true;

    // constructor
    public Taxi (int numBranches, int numPeople) { 
        taxi = this; currentBranch = 0;
        direction = Direction.WAITING;
        startTime = System.currentTimeMillis();

        branches = new Semaphore[numBranches];
        for (int i = 0; i < numBranches; i++) {
            branches[i] = new Semaphore(0);
        }
        Taxi.numPeople = numPeople;
    }

    // return taxi instance
    public static Taxi getTaxi() {
        return taxi;
    }

    // decrement numPeople
    public static void finishWork() {
        numPeople--;
    }

    // get access to the branch's semaphore
    public static Semaphore getBranch(int branchNum) {
        return branches[branchNum];
    }

    private boolean shouldStop(int branchNum) {
        return branches[branchNum].hasQueuedThreads();
    }

    // gets the current simulation time as a String
    public static String getCurrentTime() {
        long timePassed = System.currentTimeMillis() - startTime;
        int minutes = (int) (timePassed / Simulator.MILLIS_TO_MINUTES);
        int hours = minutes / 60;
        minutes -= hours * 60;

        if (minutes >= 10) return "" + (Simulator.START_HOUR + hours) % 24 + ":" + minutes;
        else return "" + (Simulator.START_HOUR + hours) % 24 + ":0" + minutes;
    }
    
    // note a new hail by a Person for pickup at a specific branch
    public void hail(int originBranch, int personID) throws InterruptedException {
        System.out.println(getCurrentTime() + " branch " + originBranch + ": person " + personID + " hail");
        if (direction == Direction.WAITING) {
            if (originBranch < currentBranch) { 
                direction = Direction.INBOUND;
                System.out.println("Changed to inbound");
            }
            else if (originBranch > currentBranch)  {
                direction = Direction.OUTBOUND;
                System.out.println("Changed to outbound");
            }
        }
        branches[originBranch].acquire();
    }

    // note a new dropoff request by a Person for a specific branch
    public void request(int requestedBranch, int personID) throws InterruptedException {
        System.out.println(getCurrentTime() + " branch " + currentBranch + ": person " + personID + " request " + requestedBranch);
        if (direction == Direction.WAITING) {
            if (requestedBranch < currentBranch) { 
                direction = Direction.INBOUND;
                System.out.println("Changed to inbound");
            }
            else if (requestedBranch > currentBranch)  {
                direction = Direction.OUTBOUND;
                System.out.println("Changed to outbound");
            }
        }
        branches[requestedBranch].acquire();
    }

    // move to the next branch in the current direction
    public void advance() throws InterruptedException {
        boolean change = true;

        switch (direction) {
            case OUTBOUND:
                currentBranch += 1;

                // check if state should change
                for (int i = currentBranch + 1; i < branches.length; i++) {
                    if (shouldStop(i)) { change = false; break; }
                }

                if (!change) break;
                else {
                    for (int i = currentBranch - 1; i > 0; i--) {
                        if (shouldStop(i)) { change = false; break; }
                    }
                }

                if (change) { 
                    direction = Direction.WAITING;
                    System.out.println("Changed to waiting");
                }
                else { 
                    direction = Direction.INBOUND;
                    System.out.println("Changed to inbound");
                }

                break;
            
            case INBOUND:
                currentBranch -= 1;

                // check if state should change
                for (int i = currentBranch - 1; i > 0; i--) {
                    if (shouldStop(i)) { change = false; break; }
                }

                if (change) break;
                else {
                    for (int i = currentBranch + 1; i < branches.length; i++) {
                        if (shouldStop(i)) { change = false; break; }
                    }
                }

                if (change) { 
                    direction = Direction.WAITING;
                    System.out.println("Changed to waiting");
                }
                else {
                    direction = Direction.OUTBOUND;
                    System.out.println("Changed to outbound");
                }

                break;
            
            default: System.err.println("Error: no direction...");
        }
        sleep(2 * Simulator.MILLIS_TO_MINUTES);
    }

    public void run() {
        try {
            while (numPeople > 0) {
                if (shouldStop(currentBranch)) {
                    if (!start) System.out.println(getCurrentTime() + " branch " + currentBranch + ": taxi arrive");
                    else start = false;

                    branches[currentBranch].release(numPeople);
                    stopped = true;
                    sleep(1 * Simulator.MILLIS_TO_MINUTES);
                    branches[currentBranch].drainPermits();
                }
                
                if (direction != Direction.WAITING) {
                    if (stopped) {
                        System.out.println(getCurrentTime() + " branch " + currentBranch + ": taxi depart");
                        stopped = false;
                    }
                    advance();
                }
            }
        }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}