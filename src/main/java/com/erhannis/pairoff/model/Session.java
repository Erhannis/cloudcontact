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
    public Room sessionLocation; // This is a specific location at an event, like "4th floor, Emerald Plaza"
    @Reference
    public List<SpeedDate> speedDates = new ArrayList<SpeedDate>(); //THINK Should a distinction be made between a slot and an actual SD between two specific people?
    
    
    public Session() {
        
    }
    
    public Session(long starttime, long duration, Room sessionLocation) {
		this.starttime = starttime;
		this.duration = duration;
        this.sessionLocation = sessionLocation;
    }
    
    @Override
    public String toString() {
        return "Session{"+id+":" + starttime + ", " + duration + ", @"+sessionLocation+"}";
    }
}
