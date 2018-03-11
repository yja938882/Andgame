package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import java.util.ArrayList;

/**
 * Created by juan on 2018. 3. 9..

 */

public class DynamicsArrayList<E> extends ArrayList<E> {
    private String type;
    public DynamicsArrayList(String pType){
        super();
        this.type = pType;
    }
    public String getType(){
        return this.type;
    }
}
