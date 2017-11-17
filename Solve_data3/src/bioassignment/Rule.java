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

    int ConL = 5;
    String[] cond;
    String out;

    // change to a new size of condition
    public Rule(int ConL) {
        this.ConL = ConL;
        this.cond = new String[ConL];
        this.out = "";
    }

    public Rule() {
        this.cond = new String[ConL];
        this.out = "";
    }
}
