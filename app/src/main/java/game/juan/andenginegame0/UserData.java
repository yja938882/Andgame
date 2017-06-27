package game.juan.andenginegame0;

import java.io.Serializable;

/**
 * Created by juan on 2017. 6. 27..
 */

public class UserData implements Serializable {
    private static final long serialVersionUID = 1L;
    String mString="";

    public UserData(){}

    public UserData(String string) {
        this.mString += string;
    }

    public String getString() {
        return mString;
    }


}
