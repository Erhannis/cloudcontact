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
    
    public long starttime = 0; //THINK This was Date, but I changed it to long to match Event.starttime; should?
    public long duration = 0;
    @Reference
    public Location location; //CHECK Something's weird.  This is either a different category of Location from the Event's Location, or something's wrong.
    @Reference
    public List<SpeedDate> speedDates = new ArrayList<SpeedDate>();
    //DUMMY Either the Session or the Location needs a maxParticipants count.
    
    
    public Session() {
        
    }
    
    public Session(long starttime, long duration, Location location) {
		this.starttime = starttime;
		this.duration = duration;
        this.location = location;
    }
    
    @Override
    public String toString() {
        return "Session{"+id+":" + starttime + ", " + duration + ", @"+location+"}";
    }
}
