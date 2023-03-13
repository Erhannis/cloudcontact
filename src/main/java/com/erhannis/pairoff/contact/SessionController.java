/**
 *
 */
package com.erhannis.pairoff.contact;

import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.model.Event;
import com.erhannis.pairoff.model.Room;
import com.erhannis.pairoff.model.Session;
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

public class SessionController {
    static DatabaseHelper dbHelper = new DatabaseHelper();
    static Logger logger = LoggerFactory.getLogger(SessionController.class);

    public SessionController() {
    }

    public static ModelAndView serveIndex(Request req, Response res, String intendedView) {
        //THINK This is for the admin page - perhaps it should permit event selection here?  ...Nah, it seems reasonable to only deal with one event at a time, for now.
        HashMap<String, Object> model = new HashMap<>();

        Datastore ds = dbHelper.getDataStore();
        
        String userId = req.session(false).attribute(Path.Web.ATTR_USER_ID).toString();
        User u = ds.get(User.class, new ObjectId(userId));
        Event e = u.getSelectedEvent(); //DUMMY Handle null event
        model.put("event", e);
        model.put("sessions", e.sessions);

        return new ModelAndView(model, intendedView);
    }
    
    public static String handleNewSession(Request req, Response res) {
        //DUMMY Fix
        try {
            Datastore ds = dbHelper.getDataStore();

            //THINK Should permit specifying event?
            String userId = req.session(false).attribute(Path.Web.ATTR_USER_ID).toString();
            User u = ds.get(User.class, new ObjectId(userId));
            Event event = u.getSelectedEvent(); //DUMMY Handle null event
            if (event == null) {
                res.status(404);
                return "No event selected";
            }
            
//            String eventId = Strings.nullToEmpty(req.queryParams("eventId")).trim();
//            Event event = ds.get(Event.class, new ObjectId(eventId));
//            if (event == null) {
//                res.status(404);
//                return "Event not found";
//            }

            String roomId = Strings.nullToEmpty(req.queryParams("roomId")).trim();
            Room room = ds.get(Room.class, new ObjectId(roomId));
            if (room == null) {
                res.status(404);
                return "Room not found";
            }
            
            
            Session x = new Session();
            x.starttime = Long.parseLong(Strings.nullToEmpty(req.queryParams("starttime")).trim());
            x.duration = Long.parseLong(Strings.nullToEmpty(req.queryParams("duration")).trim());
            //x.sessions;
            x.sessionLocation = room;
            //DITTO //CHECK speedDates, or maxParticipants?
            
            event.sessions.add(x);
            
            ds.save(x, event); //THINK Is this atomic, a transaction?  Like, could one be saved but not the other?

            res.status(200);
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
        }

        //TODO Return session object?
        return "";
    }

    public static String handleUpdateSession(Request req, Response res) {
        //DUMMY Fix
        try {
            String id = Strings.nullToEmpty(req.queryParams("id")).trim();
            Datastore ds = dbHelper.getDataStore();
            
            String roomId = Strings.nullToEmpty(req.queryParams("roomId")).trim();
            Room room = ds.get(Room.class, new ObjectId(roomId));
            if (room == null) {
                res.status(404);
                return "Room not found";
            }
            
            //DUMMY Doesn't currently permit moving to different event...maybe that's ok
            
            Session x = ds.get(Session.class, new ObjectId(id));
            x.starttime = Long.parseLong(Strings.nullToEmpty(req.queryParams("starttime")).trim());
            x.duration = Long.parseLong(Strings.nullToEmpty(req.queryParams("duration")).trim());
            //x.sessions;
            x.sessionLocation = room;
            //DITTO //CHECK speedDates, or maxParticipants?
            
            ds.save(x);
            
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
