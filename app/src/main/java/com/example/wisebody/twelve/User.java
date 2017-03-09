package com.example.wisebody.twelve;

import java.io.Serializable;

/**
 * Created by wisebody on 2016. 6. 28..
 */
public class User implements Serializable {
    public String id;
    public String pass;

    protected User(String id)
    {
        this(id,null);
    }
    protected User(String id, String pass)
    {
        this.id = id; this.pass = pass;
    }

}
