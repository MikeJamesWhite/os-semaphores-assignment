/**
 * Person.java
 * Class which represents a person who needs to be picked up and transported by a Taxi.
 * 
 * Author: Mike James White
 * Date: 19/05/2018
 */

import java.util.*;

public class Person extends Thread {
    public int id;
    private int currentBranch;
    public Queue<int[]> workPattern;

    // parse an input file string and attain attributes for new Person object
    public Person(String s) {
        currentBranch = 0;

        s = s.trim();
        
        // get ID
        int begin = s.indexOf(' ');
        id = Integer.parseInt(s.substring(0, s.indexOf(' ')));

        // discard start of string, remove brackets, and split into an array
        String [] split = s.substring(begin + 1).replace("(", "").replace(")", "").split(", ");

        // create workPattern array
        workPattern = new ArrayDeque<int[]>();
        for (int i = 0; i < split.length/2; i++) {
            workPattern.add( new int[] { Integer.parseInt(split[i*2]), Integer.parseInt(split[(i*2) + 1]) });
        }
    }

    // get to required branch, work until done and work again if another job in queue, else terminate thread
    public void work() throws InterruptedException {
        int[] currentJob = workPattern.poll();

        // if not already at branch
        if (currentBranch != currentJob[0]) {
            // hail taxi and wait for pickup
            Taxi.getTaxi().hail(currentBranch, id);

            // request a dropoff location, then wait
            Taxi.getTaxi().request(currentJob[0], id);
            currentBranch = currentJob[0]; 
        }

        // now arrived at requested branch -- sleep until current job is finished
        sleep(currentJob[1] * Simulator.MILLIS_TO_MINUTES);

        // work again if there are more jobs, else thread terminates
        if (!workPattern.isEmpty()) work();
    }

    public void run() {
        try {
            work();
        }
        catch (InterruptedException e) { e.printStackTrace(); }

        Taxi.finishWork();
        System.out.println("Person " + id + " finished work!");
    }
}