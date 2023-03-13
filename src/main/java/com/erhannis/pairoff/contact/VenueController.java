/**
 *
 */
package com.erhannis.pairoff.contact;

import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.model.Venue;
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

public class VenueController {
    static DatabaseHelper dbHelper = new DatabaseHelper();
    static Logger logger = LoggerFactory.getLogger(VenueController.class);

    public VenueController() {
    }

    public static ModelAndView serveIndex(Request req, Response res, String intendedView) {
        HashMap<String, Object> model = new HashMap<>();

        Datastore ds = dbHelper.getDataStore();
        List<Venue> venues = ds.find(Venue.class).asList();

        model.put("venues", venues);

        return new ModelAndView(model, intendedView);
    }
    
    public static int handleNewVenue(Request req, Response res) {
        try {
            String description = Strings.nullToEmpty(req.queryParams("description")).trim(); //RAINY Rename to "description" or something?
        
            Datastore ds = dbHelper.getDataStore();
            Venue v = new Venue(description);
            ds.save(v);
            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        //TODO Return venue object?
        return res.status();
    }

    public static int handleUpdateVenue(Request req, Response res) {
        try {
            String id = Strings.nullToEmpty(req.queryParams("id")).trim();
            String description = Strings.nullToEmpty(req.queryParams("description")).trim(); //DITTO
            
            Datastore ds = dbHelper.getDataStore();
            Venue v = ds.get(Venue.class, new ObjectId(id));
            v.description = description;
            ds.save(v);
            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        return res.status();
    }

    // public static Object handleDeleteVenue(Request req, Response res) //TODO Do?
}
