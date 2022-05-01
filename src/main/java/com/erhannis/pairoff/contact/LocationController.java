/**
 *
 */
package com.erhannis.pairoff.contact;

import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.model.Location;
import com.erhannis.pairoff.model.User;
import com.erhannis.pairoff.util.Path;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.List;
import org.bson.types.ObjectId;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class LocationController {
    static DatabaseHelper dbHelper = new DatabaseHelper();
    static Logger logger = LoggerFactory.getLogger(LocationController.class);

    public LocationController() {
    }

    public static ModelAndView serveIndex(Request req, Response res, String intendedView) {
        HashMap<String, Object> model = new HashMap<>();

        Datastore ds = dbHelper.getDataStore();
        List<Location> locations = ds.find(Location.class).asList();

        model.put("locations", locations);

        return new ModelAndView(model, intendedView);
    }
    
    public static int handleNewLocation(Request req, Response res) {
        try {
            String location = Strings.nullToEmpty(req.queryParams("location")).trim();
        
            Datastore ds = dbHelper.getDataStore();
            Location l = new Location(location);
            ds.save(l);
            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        //TODO Return location object?
        return res.status();
    }

    public static int handleUpdateLocation(Request req, Response res) {
        try {
            String id = Strings.nullToEmpty(req.queryParams("id")).trim();
            String location = Strings.nullToEmpty(req.queryParams("location")).trim();
            
            Datastore ds = dbHelper.getDataStore();
            Location l = ds.get(Location.class, new ObjectId(id));
            l.location = location;
            ds.save(l);
            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        return res.status();
    }

    // public static Object handleDeleteLocation(Request req, Response res) //TODO Do?
}
