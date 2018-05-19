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

        // clear console and print program intro
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("SEMAPHORE SIMULATION v1.0 by Mike James White (WHTMIC023)");
        System.out.println("---------------------------------------------------------");
        System.out.println();
        System.out.println("Number of branches (including HQ): " + numBranches);
        System.out.println("Number of people: " + numPeople);
        System.out.println();

        // run simulation
        Taxi t = new Taxi(numBranches, numPeople);
        for (Person p : people) p.start();
        t.start();
    }
}