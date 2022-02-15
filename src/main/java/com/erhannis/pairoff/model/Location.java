/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("location_tbl")
public class Location {
    
    @Id
    private ObjectId id;
    
    public String location = "";
    
    
    public Location() {
        
    }
    
    public Location(String location) {
		this.location = location;
    }
    
    @Override
    public String toString() {
        return "Location{" + id + ":\"" + location + "\"}";
    }
}
