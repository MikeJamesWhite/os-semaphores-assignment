/**
 * Simulator.java
 * Driver program for running the semaphore simulation
 * 
 * Author: Mike James White
 * Date: 19/05/2018
 */

import java.io.*;
import java.util.*;

public class Simulator {
    public static final int MILLIS_TO_MINUTES = 33;
    public static final int START_HOUR = 9;
    
    public static void main (String[] args) {
        int numPeople = 0;
        int numBranches = 0;
        Person[] people = null;

        // parse input file
        try {
            File f = new File(args[0]);
            Scanner in = new Scanner(f);

            numPeople = Integer.parseInt(in.nextLine());
            numBranches = Integer.parseInt(in.nextLine());
            people = new Person[numPeople];
            for (int i = 0; i < numPeople; i++) {
                people[i] = new Person(in.nextLine());
            }

            in.close();
        }
        catch (Exception e) { e.printStackTrace(); return; }

        // run simulation
        Taxi t = new Taxi();

        try {
        t.advance();
        t.advance();
        t.request(1, 1);
        t.hail(3, 1);
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}