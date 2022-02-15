/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("session_tbl")
public class Session {
    
    @Id
    private ObjectId id;
    
    public Date starttime = null;
    public long duration = 0;
    @Reference
    public Location location;
    @Reference
    public List<SpeedDate> speedDates = new ArrayList<SpeedDate>();
    
    
    public Session() {
        
    }
    
    public Session(Date starttime, long duration, Location location) {
		this.starttime = starttime;
		this.duration = duration;
        this.location = location;
    }
    
    @Override
    public String toString() {
        return "Session{"+id+":" + starttime + ", " + duration + ", @"+location+"}";
    }
}