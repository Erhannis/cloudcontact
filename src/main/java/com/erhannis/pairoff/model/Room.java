/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("room_tbl")
public class Room {
    
    @Id
    private ObjectId id;
    
    public String location = ""; // E.g. "4th floor, Emerald Plaza"
    public Integer maxParticipants; // Optional...kinda.
    
    
    public Room() {
        
    }
    
    public Room(String location, Integer maxParticipants) {
		this.location = location;
        this.maxParticipants = maxParticipants;
    }
    
    @Override
    public String toString() {
        return "Room{" + id + ":\"" + location + "\", max: " + maxParticipants + "}";
    }
}
