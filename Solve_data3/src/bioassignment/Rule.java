/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bioassignment;

/**
 *
 * @author sa-llewellyn
 */
public class Rule {

    int ConL = 5; //hard coded
    float[] cond;
    int out;

    // change to a new size of condition
    public Rule(int ConL) {
        this.ConL = ConL;
        this.cond = new float[ConL];
        this.out = 2;
    }

    public Rule() {
        this.cond = new float[ConL];
        this.out = 2;
    }
}
