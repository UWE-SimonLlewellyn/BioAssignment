/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bioassignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author simon
 */
public class GA {

    public GA() {
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

    public static String printBitString(float[] array, int x) {
        String s = "";
        for (int i = 1; i < array.length + 1; i++) {
            if (((i % 13) != 0)) {
                if (i == x + 1) {
                    s = s + "^";
                }
                s = s + array[i - 1] + " ";
            } else {
                s = s + "(" + array[i - 1] + ")";
            }
        }
        return s;
    }

    // Set the creates a new Indiviuale 
    public static Individual[] initiateArray(int p_size, int gene_size, int NumR, int ConL) {
        Individual[] temp = new Individual[p_size];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = new Individual(gene_size, NumR, ConL);
        } //for i
        return temp;
    }

    public static Individual[] createPopulation(Individual[] array) {
        ////////////////////////////////////////////////////////////////////////////
        // Create a new array of population 
        // Set gene of ConL of floats followed by a int between 0 or 1
        int size = array[0].gene.length;
        int ConL = array[0].ConL;
        for (Individual a : array) {
            for (int j = 1; j < size + 1; j++) {
                if ((j % (ConL + 1)) == 0) {
                    a.gene[j - 1] = new Random().nextInt(2);
                } else {
                    float d = (float) Math.random();
                    a.gene[j - 1] = d;
                }
            }

            a.create_rulebase(); // Loop through population and convert genes to the to the rulebases 
        } //for i        
        return array;
    }

    public static Individual[] tournment(Individual[] original) {
        ///////////////////////////////////////////////////////////////////////////////////////// 
        //Tornement selection
        // selects two random genes from the population
        // comnpares fitness and stored the strongest in Offspring
        int parent1, parent2;
        Individual[] temp = initiateArray(original.length, original[0].gene.length, original[0].NumR, original[0].ConL);
        for (int i = 0; i < original.length; i++) {
            parent1 = new Random().nextInt(original.length);
            parent2 = new Random().nextInt(original.length);

            if (original[parent1].fitness >= original[parent2].fitness) {
                temp[i] = new Individual(original[parent1]);
            } else {
                temp[i] = new Individual(original[parent2]);

            }
        }
        return temp;
    }

    // Crossover fully working as tested. 
    public static Individual[] crossover(Individual[] original) {
        //////////////////////////////////////////////////////////////////////////////////////
        // Crossover
        // Select pairs of genes from the offprint list
        //pick random point and swap bitstring after each other
        // eg. 000|00  == 000|11
        //     111|11  == 111|00
        int gene_size = original[0].gene.length;
        int p_size = original.length;
        int NumR = original[0].rulebase.length;
        int ConL = original[0].rulebase[0].cond.length;
        Individual[] modified = new Individual[p_size];

        for (int i = 0; i < p_size; i += 2) {
            Individual temp1 = new Individual(gene_size, NumR, ConL);
            Individual temp2 = new Individual(gene_size, NumR, ConL);
            int x_point = new Random().nextInt(gene_size);

            for (int j = 0; j < x_point; j++) {
                temp1.gene[j] = original[i].gene[j];
                temp2.gene[j] = original[i + 1].gene[j];
            }

            for (int j = x_point; j < gene_size; j++) {
                temp1.gene[j] = original[i + 1].gene[j];
                temp2.gene[j] = original[i].gene[j];
            }

            temp1.create_rulebase();
            temp2.create_rulebase();
            modified[i] = new Individual(temp1);
            modified[i + 1] = new Individual(temp2);
//            
//           System.out.println(i + " " + printBitString(original[i].gene,x_point));
//            System.out.println(i + " " + printBitString(modified[i].gene,x_point));
//            
//            System.out.println((i+1) + " " + printBitString(original[i+1].gene,x_point));
//           System.out.println((i+1) + " " + printBitString(modified[i+1].gene,x_point));
//            System.out.println("");
        }

        return modified;
    }

    public static Individual[] mutation(Individual[] original, double mute_rate) {
        ///////////////////////////////////////////////////////////////////////////////////
        // Mutation
        // go through the offspring population
        // mutate the bit at a mutation rate of 1/gene_size
        int mutes_per_gene = 0;
        int p_size = original.length; // total number of solutions
        int gene_size = original[0].gene.length; // total lenth of each solotuion

        for (int i = 0; i < p_size; i++) { // Loop over each solution in population
//            System.out.print(i + " ");
//            System.out.print(printBitString(original[i].gene, 0));
//            System.out.println("");
            for (int j = 1; j < gene_size + 1; j++) { // Loop over each value inthe gene

                double d = Math.random(); //give number between 0.0 - 1.0
                if (d < mute_rate) { // in random number < mute_rate change value   
                    if ((j % 13) == 0) {                        
                        if (original[i].gene[j - 1] == (float)0.0) {
                            original[i].gene[j - 1] = (float)1.0;
                        } else {
                            original[i].gene[j - 1] = (float)0.0;
                        }
                    } else {
                        int operand_selection = new Random().nextInt(2);

                        if (operand_selection == 0) {
                            original[i].gene[j - 1] += (float) 0.01;
                        } else {
                            original[i].gene[j - 1] -= (float) 0.01;
                        }

                        mutes_per_gene++;
                    }
                }

            }
//            System.out.print(i + " ");
//            System.out.print(printBitString(original[i].gene, 0));
//            System.out.println("");

            original[i].create_rulebase();
        }
        //  System.out.println("mutes in this population: " + mutes_per_gene);

        return original;
    }

    public static Individual evaluate(Individual[] array, Individual best) {
        // Compares each solution in the offspring population to the fittest found

        Individual temp;
        for (Individual array1 : array) {
            if (array1.fitness > best.fitness) {
                best = array1;
            }
        }
        temp = new Individual(best);
        return temp;
    }

    public static String print_rules(Rule[] rules) {
        String s = "";
        int count = 1;
        for (Rule r : rules) {
            s = s + "Rule " + count + ": ";
            for (int i = 0; i < r.cond.length; i++) {
                s = s + "("+ r.cond[i] + " , " + r.cond[i++] + ")  ";
            }
            s = s + " = " + r.out + "\n";
            count++;
        }
        return s;
    }

}
