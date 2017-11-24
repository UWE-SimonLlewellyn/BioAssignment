/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bioassignment;

/**
 *
 * @author simon
 */
public class Individual {

    int NumR;
    int ConL;
    float[] gene;
    int fitness = 0;
    Rule[] rulebase;

    // public Individual(){}
    public Individual(int n, int NumR, int ConL) {
        this.gene = new float[n];
        this.fitness = 0;
        this.ConL = ConL;
        this.NumR = NumR;
        create_rulebase();
       
    }

    public Individual(Individual n) {
        this.ConL = n.ConL;
        this.NumR = n.NumR;
        this.gene = n.gene;
        this.fitness = n.fitness;
        this.rulebase = n.rulebase;
        
    }

    public void getFitness() {
        System.out.println("The fitness is " + fitness);
    }


    public void create_rulebase() {
        int fitness = 0;
        int k = 0;

        this.rulebase = new Rule[NumR];
        for (int i = 0; i < NumR; i++) {
            rulebase[i] = new Rule(ConL);
            for (int j = 0; j < ConL; j++) {
                rulebase[i].cond[j] = gene[k++];
            }
            rulebase[i].out = (int) gene[k++];
        }

    }

}
