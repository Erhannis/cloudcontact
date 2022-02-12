/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("session_tbl")
public class Session {
    
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    
    public Date starttime = null;
    public long duration = 0;
    
    
    public Session() {
        
    }
    
    public Session(Date starttime, long duration) {
		this.starttime = starttime;
		this.duration = duration;
    }
    
    @Override
    public String toString() {
        return "SESSION " + id + " (" + starttime + ", " + duration + ")";
    }
}
