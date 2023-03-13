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
    public User userA;
    @Reference
    public User userB;
    
    //THINK I removed "location" because it's redundant with Session containing this SpeedDate...I think.  Unless, to preserve a record despite changes?

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
    
    public SpeedDate(Date starttime, long duration) {
		this.starttime = starttime;
		this.duration = duration;
    }
    
    @Override
    public String toString() {
        return "SpeedDate{"+id+":" + starttime + ", " + duration + ", ("+userA+":"+userB+")}";
    }
}
