/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("venue_tbl")
public class Venue {
    
    @Id
    private ObjectId id;
    
    public String description = "";
    
    
    public Venue() {
        
    }
    
    public Venue(String description) {
		this.description = description;
    }
    
    @Override
    public String toString() {
        return "Venue{" + id + ":\"" + description + "\"}";
    }
}
