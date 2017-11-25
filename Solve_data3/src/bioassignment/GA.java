/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bioassignment;

import java.util.ArrayList;
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

    public static String printRulesBitString(Rule[] temp) {
       String s ="";
        for (int i = 0; i<temp.length;i++){
            s += "Rule " + (i+1) + ": ";
            for(int j=0; j<temp[i].cond.length;j++){
                float a = temp[i].cond[j], b = temp[i].cond[++j];
                
                if( a < b){
                    if(a >= 0.4){
                        s += "1";
                    }else if(b<=0.6){
                        s += "0";
                    }else{
                        s += "#";
                    }
                }else{
                    if(b >= 0.4){
                        s += "1";
                    }else if(a<=0.6){
                        s += "0";
                    }else{
                        s += "#";
                    }
                }
            }
            s += " = "+ temp[i].out + "\n";
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

        }

        return modified;
    }

    public static Individual[] mutation(Individual[] original, double mute_rate, float mute_size) {
        ///////////////////////////////////////////////////////////////////////////////////
        // Mutation
        // go through the offspring population
        // mutate the bit at a mutation rate of 1/gene_size        
        int p_size = original.length; // total number of solutions
        int gene_size = original[0].gene.length; // total lenth of each solotuion


        for (int i = 0; i < p_size; i++) { // Loop over each solution in population
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
                            original[i].gene[j - 1] += mute_size ;
                            if(original[i].gene[j - 1] > 1.0) 
                                original[i].gene[j - 1] = (float)1.0;
                        } else {
                            original[i].gene[j - 1] -= mute_size;
                            if(original[i].gene[j - 1] < 0.0) 
                                original[i].gene[j - 1] = (float)0.0;
                        }
                    }
                }
            }
            original[i].create_rulebase();
        }
//         System.out.println("mutes in this population: " + mutes_per_gene);
        return original;
    }
    
        public static void score_fitness(Individual solution, ArrayList<Data> data) {
        // fit needs needs to score the data value between ranges
        //E.G. 	DATA =    0.25       0.65       0.6         0.24    = 1
	//      Rule = (0.1,0.27) (0.5,0.75) (0.18,0.80) (0.01,0.27) = 1
        // fitness =       y    +     y    +      y     +      y     + y = 1         
        solution.fitness = 0;
        for (int i = 0; i < data.size(); i++) {
            for (Rule rulebase : solution.rulebase) {
                if (matches_cond(data.get(i).variables, rulebase.cond) == true) {
                    if (rulebase.out == data.get(i).type) {
                        solution.fitness++;
                    }
                    break; // note it is important to get the next data item after a match
                }
            }
        }
    }
        
    public static boolean matches_cond(float[] data, float[] rule) {
        int k = 0;
        for (int i = 0; i < data.length; i++) {            
            if( rule[k] > rule[k+1] ){
                if((data[i] > rule[k]) || (rule[k+1]>data[i])){
                    return false;
                }
            }else{            
                if((data[i] < rule[k]) || (rule[k+1]<data[i])){
                    return false;
                }
            }
            k+=2;
        }
        return true;
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
                s = s + "("+ r.cond[i++] + " , " + r.cond[i] + ")  ";
            }
            s = s + " = " + r.out + "\n";
            count++;
        }
        return s;
    }

}
