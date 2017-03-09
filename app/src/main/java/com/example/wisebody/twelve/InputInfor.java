package com.example.wisebody.twelve;

import java.io.Serializable;

/**
 * Created by wisebody on 2016. 6. 30..
 */
public class InputInfor implements Serializable {

    protected String id;
    protected String pass;
    protected String repass;
    protected InputInfor(String id, String pass, String repass)
    {
        this.id = id;
        this.pass = pass;
        this.repass = repass;
    }
    protected InputInfor()
    {

    }
}
