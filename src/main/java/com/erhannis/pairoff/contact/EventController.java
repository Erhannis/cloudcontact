/**
 *
 */
package com.erhannis.pairoff.contact;

import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.model.Event;
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

public class EventController {
    static DatabaseHelper dbHelper = new DatabaseHelper();
    static Logger logger = LoggerFactory.getLogger(EventController.class);

    public EventController() {
    }

    public static ModelAndView serveIndex(Request req, Response res, String intendedView) {
        HashMap<String, Object> model = new HashMap<>();

        Datastore ds = dbHelper.getDataStore();
        List<Event> events = ds.find(Event.class).asList();

        model.put("events", events);

        return new ModelAndView(model, intendedView);
    }
    
    public static String handleNewEvent(Request req, Response res) {
        try {
            Datastore ds = dbHelper.getDataStore();
            String locationId = Strings.nullToEmpty(req.queryParams("locationId")).trim();
            Location location = ds.get(Location.class, new ObjectId(locationId));
            if (location == null) {
                res.status(404);
                return "Location not found";
            }
            
            Event e = new Event();
            e.name = Strings.nullToEmpty(req.queryParams("name")).trim();
            e.starttime = Long.parseLong(Strings.nullToEmpty(req.queryParams("starttime")).trim());
            e.duration = Long.parseLong(Strings.nullToEmpty(req.queryParams("duration")).trim());
            //e.sessions;
            e.location = location;
            ds.save(e);
            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        //TODO Return event object?
        return "";
    }

    public static String handleUpdateEvent(Request req, Response res) {
        try {
            String id = Strings.nullToEmpty(req.queryParams("id")).trim();
            Datastore ds = dbHelper.getDataStore();
            String locationId = Strings.nullToEmpty(req.queryParams("locationId")).trim();
            Location location = ds.get(Location.class, new ObjectId(locationId));
            if (location == null) {
                res.status(404);
                return "Location not found";
            }
            
            Event e = ds.get(Event.class, new ObjectId(id));
            e.name = Strings.nullToEmpty(req.queryParams("name")).trim();
            e.starttime = Long.parseLong(Strings.nullToEmpty(req.queryParams("starttime")).trim());
            e.duration = Long.parseLong(Strings.nullToEmpty(req.queryParams("duration")).trim());
            //e.sessions;
            e.location = location;
            ds.save(e);
            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        //TODO Return event object?
        return "";
    }

    // public static Object handleDeleteEvent(Request req, Response res) //TODO Do?
}
