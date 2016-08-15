package config;

import edu.udo.cs.wvtool.main.WVTWordVector;

import java.util.ArrayList;

/**
 * Created by pc on 05/08/2016.
 */
public class Interest {
    private int[] tfVector;

    public Interest(int [] tfVector){
        this.tfVector= tfVector;
    }

    public int[] getTfVector(){
        return this.tfVector;
    }
}
