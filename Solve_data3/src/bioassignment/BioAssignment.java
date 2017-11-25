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
import java.util.Scanner;


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
        String filename = "data3.txt";

        // ArrayList of Data objects from the file
        ArrayList<Data> data_set = create_data_set(filename);
        ArrayList<Data> training_set = new ArrayList<>();
        ArrayList<Data> test_set = new ArrayList<>();
        
        // Create both training and test set
        for(Data t:data_set){
            double d = Math.random();
            if(d<0.75) training_set.add(t);
            else test_set.add(t);
        }
        

        int NumR = 10; // number of rules
        int ConL = data_set.get(0).Vars * 2; // condition length = each conditon from that data set need two values to indicate a range
        int p_size = 100; // population size - MUST BE A EVEN NUMBER
        int itteration = 2000; // amoutn of generations 
        int gene_size = (ConL + 1) * NumR; // size of gene per solution
        double mute_rate = (1 / ((double) gene_size));
        float mute_size = (float)0.01;
        Individual best = new Individual(gene_size, NumR, ConL); // Store the best solution found
        Individual[] population = GA.initiateArray(p_size, gene_size, NumR, ConL);
        Individual[] offspring = GA.initiateArray(p_size, gene_size, NumR, ConL);

        //Created an iniitial population with random genes
        population = GA.createPopulation(population);

 
        
        for (Individual pop : population) {
            GA.score_fitness(pop, training_set); // works
        }
        printFitness(population);

        int generation = 0;
          while (generation < itteration) {
            // System.out.println("\n\n-------------------------------------------------");

            // create offspring using tourniment selection
            offspring = GA.tournment(population);
            for (Individual pop : offspring) {
                GA.score_fitness(pop, training_set);
            }
            //  System.out.println("Tourn");
//               printFitness(offspring);

       ////////// Everything up to this works////////////////////////////////////

////            // Perform crossover
            offspring = GA.crossover(offspring);
            for (Individual pop : offspring) {
                GA.score_fitness(pop, training_set);
            }
//            //  System.out.println("X-over");
//            //   printFitness(offspring);

////          
//            // Perform mutation 
            offspring = GA.mutation(offspring, mute_rate, mute_size);
            for (Individual pop : offspring) {
                GA.score_fitness(pop, training_set);
            }
//            //   System.out.println("After Mute");
//            //     printFitness(offspring);

//            // evaluate
            best = GA.evaluate(offspring, best);
            
            for (int i = 0; i < p_size; i++) {
                population[i] = new Individual(offspring[i]);
            }
            // Loop through population and convert genes to the to the rulebases
            for (Individual pop : population) {
                GA.score_fitness(pop, training_set);
            }
//

            generation++;
//            System.out.println("Generation: " + generation);
//            //   printFitness(population);
//            System.out.println("Best fitness is " + best.fitness);
            csv += best.fitness + ",";
        }
//        String gh = "";
//        for (int i = 0; i < best.gene.length; i++) {
//            gh += best.gene[i];
//        }
//        System.out.println(gh);
        System.out.println("Trained using " + training_set.size() + " sets of data");
        System.out.println("Best fitness using training set " + best.fitness);        
        System.out.println(GA.print_rules(best.rulebase));
        System.out.println(csv);

        GA.score_fitness(best, test_set);
        System.out.println("Tested using " + test_set.size() + " pieces of data");
        System.out.println("Best fitness over test set " + best.fitness );
        System.out.println("Equals " + (100/test_set.size())*best.fitness + "% accuracy");

        
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
                if ((line1.charAt(i) != '\n')) {
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
        ArrayList<Data> tempA = new ArrayList<>();
        
        for (String a : file_array) {
            ArrayList<Float> temp = new ArrayList<>();
            Scanner scanner = new Scanner(a);
            scanner.useDelimiter(" ");
            while (scanner.hasNext()) {             
                    temp.add(scanner.nextFloat());
            }
        
            Data data_temp = new Data(temp.size() - 1);
            for (int i = 0; i <= data_temp.Vars - 1; i++) {
                data_temp.variables[i] = temp.get(i);
            }
            data_temp.type = Character.getNumericValue(a.charAt(a.length()-1));
            tempA.add(data_temp);
    }
    return tempA ;
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


    
   

}
