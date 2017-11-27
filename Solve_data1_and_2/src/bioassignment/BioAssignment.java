/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bioassignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author simon
 */
public class BioAssignment {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String csv = "";
        // Add the filename either "data1.txt" or "data2.txt"
        String filename = "data2.txt";

        // ArrayList of Data objects from the file
        ArrayList<Data> data_set = create_data_set(filename);

        int NumR = 5; // number of rules
        int ConL = data_set.get(0).Vars; // condition length
        int p_size = 100; // population size - MUST BE AND EVEN NUMBER
        int itteration = 500; // amoutn of generations 
        int multiGA = 200; // controlls how many differnt GA to run.
        int correct_rules = 0;
        int gene_size = (ConL + 1) * NumR; // size of gene per solution
        double mute_rate = 0.02; // Equal to 2% chance of mutation

        Individual global_best = new Individual(gene_size, NumR, ConL); // Store the best solution found
        Individual[] population = GA.initiateArray(p_size, gene_size, NumR, ConL);
        Individual[] offspring = GA.initiateArray(p_size, gene_size, NumR, ConL);

        int index = 0;
        while (index < multiGA) { // Start if loop that allow multple GAs to run sequentially 
            Individual best = new Individual(gene_size, NumR, ConL); // Store the best solution found
            //Created an iniitial population with random genes
            population = GA.createPopulation(population);

            for (Individual pop : population) {
                score_fitness(pop, data_set); // works
            }

            int generation = 0;
            while (generation < itteration) {

                // create offspring using tourniment selection
                offspring = GA.tournment(population);
                for (Individual pop : offspring) {
                    score_fitness(pop, data_set);
                }

                // Perform crossover
                offspring = GA.crossover(offspring);
                for (Individual pop : offspring) {
                    score_fitness(pop, data_set);
                }

                // Perform mutation 
                offspring = GA.mutation(offspring, mute_rate);
                for (Individual pop : offspring) {
                    score_fitness(pop, data_set);
                }

                // evaluate
                best = GA.evaluate(offspring, best);

                for (int i = 0; i < p_size; i++) {
                    population[i] = new Individual(offspring[i]);
                }

                generation++;
                csv += best.fitness + ",";
            }
            csv += "\n";
            System.out.println("Best fitness is " + best.fitness);
            if(best.fitness == 64 ) correct_rules++;
            index++;
            //check completed GA's best solution and compare with the previous GA
            if (best.fitness > global_best.fitness) {
                global_best = new Individual(best);
            }
        }
        System.out.println("After " + multiGA + " GAs, " + correct_rules + " correctly identified all " + data_set.size() +" items in the data set.");
        System.out.println(GA.print_rules(global_best.rulebase));
        System.out.println(csv);
    }

    public static String file_to_string(String filename) throws FileNotFoundException, IOException {
        // Read the Data file and create a single String of the contents
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine(); // this will read the first line
        String line1 = null, s = "";
        while ((line1 = reader.readLine()) != null) { //loop will run from 2nd line
            for (int i = 0; i < line1.length(); i++) {
                if ((line1.charAt(i) == '0') || (line1.charAt(i) == '1')) {
                    s = s + line1.charAt(i);
                }
            }
        }
        return s;
    }

    public static ArrayList<String> file_to_string_array(String filename) throws FileNotFoundException, IOException {
        ArrayList<String> array = new ArrayList<>();
        // Read the Data file and create a single String of the contents
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine(); // this will read the first line
        String line1 = null;
        while ((line1 = reader.readLine()) != null) {
            String s = "";
            for (int i = 0; i < line1.length(); i++) {
                if ((line1.charAt(i) == '0') || (line1.charAt(i) == '1')) {
                    s = s + line1.charAt(i);
                }
            }
            array.add(s);
        }
        return array;
    }

    // Read the data from file and passes back array list. 
    public static ArrayList<Data> create_data_set(String filename) throws IOException {
        ArrayList<String> file_array = file_to_string_array(filename);
        int size_data = file_array.get(0).length() - 1;
        int k = 0;
        ArrayList<Data> tempA = new ArrayList<>();

        for (String a : file_array) {
            Data temp = new Data(size_data);
            for (int i = 0; i < size_data; i++) {
                temp.variables[i] = Character.getNumericValue(a.charAt(i));
            }
            temp.type = Character.getNumericValue(a.charAt(size_data));
            tempA.add(temp);
        }
        return tempA;
    }

    public static boolean matches_cond(int[] data, String[] rule) {
        for (int i = 0; i < data.length; i++) {
            String s = "" + data[i];  // Changing int[] to String[]
            if ((rule[i].equals(s) != true) && (rule[i].equals("#") != true)) {
                return false;
            }
        }
        return true;
    }

    public static void printFitness(Individual[] array) {
        //Score the fitness by adding all the '1' in the gene
        // Print the fitness
        System.out.println("Fitness");
        int avFitness = 0;
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i].fitness + " ");
            avFitness = avFitness + array[i].fitness;
        }//for i
        System.out.println("\nTotal of all fitness = " + avFitness + "\nAverage fitness = " + (avFitness / array.length) + "\n");
    }

    public static void score_fitness(Individual solution, ArrayList<Data> data) {

        solution.fitness = 0;
        for (int i = 0; i < data.size(); i++) {
            for (Rule rulebase : solution.rulebase) {
                if (matches_cond(data.get(i).variables, rulebase.cond) == true) {
                    String s = "" + data.get(i).type;
                    if (rulebase.out.equals(s) == true) {
                        solution.fitness++;
                    }
                    break; // note it is important to get the next data item after a match
                }
            }
        }
    }

}
