package game.juan.andenginegame0.ygamelibs.Cheep.Data;

import java.util.ArrayList;

/**
 * Created by juan on 2018. 3. 13..
 *
 */

public class DataArrayList<E> extends ArrayList<E> {
    private String id;
    private String type;
    public DataArrayList(String pType, String pId){
        this.type = pType;
        this.id = pId;
    }
    public String getType(){
        return this.type;
    }
    public String getID(){return this.id;}
}
