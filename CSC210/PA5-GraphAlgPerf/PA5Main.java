
/* AUTHOR: Ruben Tequida
 * FILE: PA5Main.java
 * ASSIGNMENT: Programming Assignment 5 - BoolSAT
 * COURSE: CSc 210; Section C; Spring 2019
 * Purpose: This program takes in an array of strings where the first element
 * if the filename and the second is a command of BACKTRACK, HEURISTIC, MINE, 
 * or TIME. The file being read in is a .mtx which will detail the size of the
 * graph, how many edges will be in the graph, and the which edges connect
 * which nodes and what the edge's weights will be. Then, the program will
 * find the best travel plan based on which command was given. If BACKTRACK
 * was given the code will use recursive backtracking to find the best path.
 * If HEURISTIC is given the code will find one path determined by following
 * the edges with the least costs. If MINE is given the code will find the
 * cost associated with the heuristic approach then use that cost to prune
 * the possible travel paths from the recursive backtracking function and
 * eliminate any paths that would cost more than the heuristic approach. If
 * the TIME command is given the code will run all three algorithms, give the
 * cost associated with each algorithm as well as their respective runtimes.
 * 
 * xample input file:
 * 
 * %%MatrixMarket matrix coordinate real general
 * 3 3 6
 * 1 2 1.0
 * 2 1 2.0
 * 1 3 3.0
 * 3 1 4.0
 * 2 3 5.0
 * 3 2 6.0
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PA5Main {
    /*
     * Purpose: Calls functions to read in the file and create the graph, then
     * calls the method associated with the given command
     * 
     * @param args, which takes in arguments from the command line in the
     * order filename, method command (BACKTRACK, HEURISTIC, MINE, or TIME).
     * 
     * @return None.
     */
    public static void main(String[] args) {
        DGraph graph = makeGraph(args[0]);
        Trip trip = new Trip(graph.getNumNodes());
        trip.chooseNextCity(1);
        Trip minTrip = new Trip(graph.getNumNodes());
        if (args[1].equals("BACKTRACK")) {
            findTripBT(graph, trip, minTrip, minTrip.tripCost(graph));
            System.out.println(minTrip.toString(graph));
        }
        if (args[1].equals("HEURISTIC")) {
            findTripH(trip, 1, graph);
            System.out.println(trip.toString(graph));
        }
        if (args[1].equals("MINE")) {
            findTripM(graph, trip, minTrip);
            System.out.print(minTrip.toString(graph));
        }
        if (args[1].equals("TIME")) {
            getTimes(trip, minTrip, graph);
        }
    }

    /*
     * Purpose: Opens the file and creates DGraph object from the coordinates
     * given in the file.
     * 
     * @param fileName, which is a string of the filename given.
     * 
     * @return graph, which is a DGraph object where every node is a city and
     * every edge is has the travel cost associated with traveling to that
     * city.
     */
    public static DGraph makeGraph(String fileName) {
        Scanner file = null;
        // Attempts to open the given filename.
        try {
            file = new Scanner(new File(fileName));
            // Throws an exception and ends the program if the file isn't found.
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        // Handles the comment lines at the beginning of the file.
        String temp = "%";
        while (temp.startsWith("%")) {
            temp = file.nextLine();
        }
        // Determines the format of the grid.
        String[] format = temp.split(" ");
        DGraph graph = new DGraph(
                Integer.valueOf(format[0]));
        // Fills in the graph with nodes and edges.
        while (file.hasNextLine()) {
            String[] node = file.nextLine().split("\\s+");
            if (!node[0].equals(node[1])) {
                graph.addEdge(Integer.valueOf(node[0]),
                        Integer.valueOf(node[1]), Double.valueOf(node[2]));
            }
        }
        return graph;
    }

    /*
     * Purpose: Finds the best travel path by cost using recursive
     * backtracking.
     * 
     * @param graph is a DGraph object that holds all the cities (nodes) and
     * the cost to travel to the next city (edges). currentTrip is the trip
     * that is currently being evaluated. minTrip is the trip that has the
     * lowest trip cost at the moment. upperLimit is the max cost of the trip
     * that once it is exceeded the current trip is no longer considered for
     * being the fastest trip.
     * 
     * @return None.
     */
    public static void findTripBT(DGraph graph, Trip currentTrip,
            Trip minTrip, double upperLimit) {
        // Base case, when there are no more cities to visit.
        if (currentTrip.citiesLeft().isEmpty()) {
            if (currentTrip.tripCost(graph) < minTrip.tripCost(graph)) {
                minTrip.copyOtherIntoSelf(currentTrip);
                upperLimit = minTrip.tripCost(graph);
                return;
            }
        }
        // Recursive case, choose an unchoosen city, recurse, then unchoose.
        if (currentTrip.tripCost(graph) < upperLimit) {
            for (int city : currentTrip.citiesLeft()) {
                currentTrip.chooseNextCity(city);
                findTripBT(graph, currentTrip, minTrip, upperLimit);
                currentTrip.unchooseLastCity();
            }
        }
    }

    /*
     * Purpose: Finds one travel path by choosing an available node that has
     * the least cost associated to it until it has traveled to every city.
     * 
     * @param currentTrip is the trip that is currently being evaluated.
     * currentCity is the last city that was chosen in the path. graph is a
     * DGraph object that holds all the cities (nodes) and the cost to travel
     * to the next city (edges).
     * 
     * @return None.
     */
    public static void findTripH(Trip currentTrip, int currentCity,
            DGraph graph) {
        // An iteration for every city left to visit.
        for (int i = 2; i <= graph.getNumNodes(); i++) {
            int closestCity = currentTrip.citiesLeft().get(0);
            // Looks at every neighbor and chooses the one with the lowest cost
            for (Integer neighbor : graph.getNeighbors(currentCity)) {
                if (currentTrip.isCityAvailable(neighbor)
                        && graph.getWeight(currentCity,
                                neighbor) <= graph.getWeight(currentCity,
                                        closestCity)) {
                    closestCity = neighbor;
                }
            }
            currentTrip.chooseNextCity(closestCity);
            currentCity = closestCity;
        }
    }

    /*
     * Purpose: First, uses the heuristic approach to find an upper limit to
     * the trip cost and then calls the recursive backtracking method using
     * that upper limit to prune travel paths that would exceed that cost.
     * 
     * @param graph is a DGraph object that holds all the cities (nodes) and
     * the cost to travel to the next city (edges). currentTrip is the trip
     * that is currently being evaluated. minTrip is the trip that has the
     * lowest trip cost at the moment.
     * 
     * @return None.
     */
    public static void findTripM(DGraph graph, Trip currentTrip, Trip minTrip) {
        currentTrip.chooseNextCity(1);
        findTripH(currentTrip, 1, graph);
        double upperLimit = currentTrip.tripCost(graph);
        Trip newTrip = new Trip(graph.getNumNodes());
        newTrip.chooseNextCity(1);
        findTripBT(graph, newTrip, minTrip, upperLimit);
    }

    /*
     * Purpose: Calls the BACKTACK, HEURISTIC, and MINE methods to find their
     * respective costs and run-times.
     * 
     * @param graph is a DGraph object that holds all the cities (nodes) and
     * the cost to travel to the next city (edges). trip is the trip that is
     * currently being evaluated. minTrip is the trip that has the
     * lowest trip cost at the moment.
     * 
     * @return None.
     */
    public static void getTimes(Trip trip, Trip minTrip, DGraph graph) {
        // Evaluating the heuristic approach.
        Trip newTrip = new Trip(graph.getNumNodes());
        long startTime = System.nanoTime();
        findTripH(trip, 1, graph);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("heuristic: cost = " + trip.tripCost(graph) + ", "
                + duration + " milliseconds");
        // Evaluating my algorithm.
        trip.copyOtherIntoSelf(newTrip);
        startTime = System.nanoTime();
        findTripM(graph, trip, minTrip);
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("mine: cost = " + minTrip.tripCost(graph) + ", "
                + duration + " milliseconds");
        // Evaluating the backtracking approach.
        trip.copyOtherIntoSelf(newTrip);
        minTrip.copyOtherIntoSelf(newTrip);
        trip.chooseNextCity(1);
        startTime = System.nanoTime();
        findTripBT(graph, trip, minTrip, minTrip.tripCost(graph));
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("backtrack: cost = " + minTrip.tripCost(graph) + ", "
                + duration + " milliseconds");

    }
}