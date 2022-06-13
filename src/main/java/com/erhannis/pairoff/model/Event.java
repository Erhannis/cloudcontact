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

@Entity("event_tbl")
public class Event {    
    @Id
    public ObjectId id;
    
    public String name;
    public long starttime = 0;
    public long duration = 0;
    @Reference
    public Location location;
    @Reference
    public List<Session> sessions = new ArrayList<Session>();
    
    
    public Event() {
        
    }
    
    public Event(String name, long starttime, long duration, Location location) {
        this.name = name;
		this.starttime = starttime;
		this.duration = duration;
        this.location = location;
    }
    
    @Override
    public String toString() {
        return "Event{"+id+":" + name + ", " + starttime + ", " + duration + ", @"+location+"}";
    }
}
