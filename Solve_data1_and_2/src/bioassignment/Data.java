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
public class Data {
    int Vars; 
    int[] variables;
    int type;
    
    public Data(int Vars){
        this.Vars = Vars;
        this.variables = new int[Vars];
    }
    
    @Override
    public  String toString(){
        String s = "";
        for(int v:variables){
            s = s+v;
        }
      return  s+type;
      
      
}
}
