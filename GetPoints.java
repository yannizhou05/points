/**
 * Read the transactions from a CSV file.
 * Spend points based on the argument rules.
 *     - oldest points to be spent first
 *     - no payer's points to go negative
 * Return all payer point balances.
 **/

import java.io.*;  
import java.util.Scanner;  
import java.util.List; 
import java.util.ArrayList;
import java.util.Map; 
import java.util.HashMap; 
import java.util.TreeMap; 
import java.util.stream.Collectors;
import java.time.Instant;

public class GetPoints {
    public static void main(String[] args) {
        // get points passed as valid argumen, which is a single integer with positive value
        if (args.length != 1) {
            System.out.println("Please enter a valid positive integer.");
            return;
        }

        int totalPoints = 0;
        try {
            totalPoints = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid positive integer.");
            return;
        }   

        if (totalPoints < 0) {
            System.out.println("Please enter a valid positive integer.");
            return;
        } 

        // read from csv file
        List<Integer> points = new ArrayList<>();
        List<String> payers = new ArrayList<>();
        List<Instant> dates = new ArrayList<>();
        int sum = 0;
        try (Scanner scanner = new Scanner(new File("transactions.csv"))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                payers.add(data[0]);
                points.add(Integer.parseInt(data[1]));
                dates.add(Instant.parse(data[2]));
                sum += Integer.parseInt(data[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (sum < totalPoints) {
            System.out.println("Not enough balances for given input of points.");
            return;
        }

        // print out the output for displaying all payer point balances
        Map<String, Integer> res = calculatePoints(points, payers, dates, totalPoints);
        System.out.println(res.entrySet().stream()
                        .map(e -> String.format("\t\"%s\":\"%s\"", e.getKey(), e.getValue()))
                        .collect(Collectors.joining(",\n", "{\n", "\n}")));
    }

    /**
     * @param dates ArrayList of Instant recording the timestamp of transction.
     * @param payers ArrayList of String recording the names of payers.
     * @param points ArrayList of Integer recording the number of points per payer per transction.
     * @param totalPoints Integer represents the number of points spent.
     * 
     * @return the HashMap recording all payer point balances after spending a specific amount of points.
    **/
    private static Map<String, Integer> calculatePoints(List<Integer> points, List<String> payers, 
                                                            List<Instant> dates, int totalPoints) {
        Map<String, Integer> record = new HashMap<>();
        Map<Instant, Integer> recordDate = new TreeMap<>();
        for (int i = 0; i < dates.size(); i++) {
            recordDate.put(dates.get(i), i);
        }

        for (Instant d : recordDate.keySet()) {
            int index = recordDate.get(d);
            int p = points.get(index);
            int currentP = record.getOrDefault(payers.get(index), 0);
            if (p >= totalPoints || totalPoints == 0) {
                currentP += p - totalPoints;
                record.put(payers.get(index), currentP);
                totalPoints = 0;
            } else {
                record.put(payers.get(index), 0);
                totalPoints -= p;
            }
        }
        return record;
    }
}