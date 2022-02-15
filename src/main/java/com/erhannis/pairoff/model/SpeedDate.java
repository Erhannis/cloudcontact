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

@Entity("speeddate_tbl")
public class SpeedDate {
    public static enum Attendance {
        ON_TIME,
        LATE,
        ABSENT
    }
    
    @Id
    private ObjectId id;
    
    public Date starttime = null;
    public long duration = 0;
    @Reference
    public Location location;
    @Reference
    public User userA;
    @Reference
    public User userB;

    /*
    //TODO How to deal with results, and/or changes over time?
    Users must demonstrate presence at a Session - should they also have to for each SpeedDate?
      That sounds a bit much - people are going to be forgetting, left and right.
    //TODO If a User is stood up, should we try harder to match them more in the future?
        That might be nice, but sounds like it opens a can of worms.  Consider later.
    
    */
    public Attendance userAAttendance;
    public Attendance userBAttendance;
    
    public SpeedDate() {
        
    }
    
    public SpeedDate(Date starttime, long duration, Location location) {
		this.starttime = starttime;
		this.duration = duration;
        this.location = location;
    }
    
    @Override
    public String toString() {
        return "Session{"+id+":" + starttime + ", " + duration + ", @"+location+"}";
    }
}
