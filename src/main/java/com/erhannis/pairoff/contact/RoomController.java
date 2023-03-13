/**
 *
 */
package com.erhannis.pairoff.contact;

import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.model.Room;
import com.erhannis.pairoff.model.User;
import com.erhannis.pairoff.util.Misc;
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

public class RoomController {
    static DatabaseHelper dbHelper = new DatabaseHelper();
    static Logger logger = LoggerFactory.getLogger(RoomController.class);

    public RoomController() {
    }

    public static ModelAndView serveIndex(Request req, Response res, String intendedView) {
        HashMap<String, Object> model = new HashMap<>();

        Datastore ds = dbHelper.getDataStore();
        List<Room> rooms = ds.find(Room.class).asList();

        model.put("rooms", rooms);

        return new ModelAndView(model, intendedView);
    }
    
    public static int handleNewRoom(Request req, Response res) {
        try {
            String location = Strings.nullToEmpty(req.queryParams("location")).trim();
            Integer maxParticipants = Misc.tryParseInt(req.queryParams("maxParticipants"));
        
            Datastore ds = dbHelper.getDataStore();
            Room r = new Room(location, maxParticipants);
            ds.save(r);
            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        //TODO Return room object?
        return res.status();
    }

    public static int handleUpdateRoom(Request req, Response res) {
        try {
            String id = Strings.nullToEmpty(req.queryParams("id")).trim();
            String location = Strings.nullToEmpty(req.queryParams("location")).trim();
            Integer maxParticipants = Misc.tryParseInt(req.queryParams("maxParticipants"));
            
            Datastore ds = dbHelper.getDataStore();
            Room r = ds.get(Room.class, new ObjectId(id));
            r.location = location;
            r.maxParticipants = maxParticipants;
            ds.save(r);
            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        return res.status();
    }

    // public static Object handleDeleteRoom(Request req, Response res) //TODO Do?
}
