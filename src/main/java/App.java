
/**
 * 
 */

/**
 * @author Seun Matt
 * Date 13 Oct 2016
 * Year 2016
 * (c) SMATT Corporation
 */

import com.bitbucket.thinbus.srp6.js.SRP6JavascriptServerSessionSHA256;
import static spark.Spark.*;

import com.erhannis.pairoff.auth.AuthController;
import com.erhannis.pairoff.auth.ChallengeGen;
import com.erhannis.pairoff.contact.ContactController;
import com.erhannis.pairoff.contact.EventController;
import com.erhannis.pairoff.contact.LocationController;
import com.erhannis.pairoff.contact.SessionController;
import com.erhannis.pairoff.contact.UserController;
import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.index.IndexController;
import com.erhannis.pairoff.model.Event;
import com.erhannis.pairoff.model.User;
import com.erhannis.pairoff.util.JsonTransformer;
import com.erhannis.pairoff.util.Path;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

import spark.template.handlebars.HandlebarsTemplateEngine;

public class App {
   private DatabaseHelper dbHelper = new DatabaseHelper();
   private ChallengeGen gen;
   private SRP6JavascriptServerSessionSHA256 server; 

    Logger logger = LoggerFactory.getLogger(App.class);
    
    public App() {
		//setup Sparkjava
        //this tells sparkjava that our static files are in the public dir
		staticFileLocation("/public");
		port(getHerokuAssignedPort());
		
		//initiate our DatabaseHelper that will map our Model Classes
		new DatabaseHelper();
        
        //ensure user is logged in to have access to protected routes
        before("/s/*", (req, res) -> {
            logger.warn("BEFORE TRIGGERED " + req.url());
            Session session = req.session(true);
            boolean auth = session.attribute(Path.Web.AUTH_STATUS) != null  ? 
                            session.attribute(Path.Web.AUTH_STATUS) : false;
            logger.info("auth status = " + auth);
            if(!auth) {
                if ("GET".equalsIgnoreCase(req.requestMethod())) {
                    logger.warn("Secured Area! Login is REQUIRED");
                    res.redirect("/login");
                    halt(401);
                } else {
                    logger.warn("Secured Area! Login is REQUIRED");
                    res.status(401);
                    halt(401);
                }
            }
        });

        //ensure user is an admin to have access to admin routes
        before("/a/*", (req, res) -> {
            logger.warn("BEFORE TRIGGERED " + req.url());
            Session session = req.session(true);
            boolean auth = session.attribute(Path.Web.AUTH_STATUS) != null  ? 
                            session.attribute(Path.Web.AUTH_STATUS) : false;
            logger.info("auth status = " + auth);
            if(!auth) {
                // Not auth
                if ("GET".equalsIgnoreCase(req.requestMethod())) {
                    logger.warn("Logged-in area denied; redirecting");
                    res.redirect("/login");
                    halt(401);
                } else {
                    logger.warn("Logged-in area denied");
                    res.status(401);
                    halt(401);
                }
            } else {
                // Auth.  Is admin?
                String userId = session.attribute(Path.Web.ATTR_USER_ID).toString();
                Datastore ds = dbHelper.getDataStore();
                User u = ds.get(User.class, new ObjectId(userId));
                if (u.admin) {
                    
                } else {
                    logger.warn("Admin area denied");
                    res.status(401);
                    halt(401);
                }
            }
        });
        
//		Handle homepage routes
		get("/", (req, res) -> IndexController.serveHomePage(req, res), new HandlebarsTemplateEngine());

//		handle authentication routes
        //TODO Probably (almost) ALL the pages want User if it's available - for sidebar
		get("/login",                    (req, res) -> { return AuthController.serveLoginPage(req, res); }, new HandlebarsTemplateEngine());
		post("/post/login",              (req, res) -> { return AuthController.handleLogin(req, res);} );
        post("/post/auth",               (req, res) -> { return AuthController.handleAuth(req, res); } );
        get("/account_signup",           (req, res) -> { return AuthController.serveSignUpPage(req, res); }, new HandlebarsTemplateEngine());
		post("/post/account_signup",     (req, res) -> { return AuthController.handleSignUp(req, res);});
        get("/logout",                   (req, res) -> { return AuthController.handleSignOut(req, res); });
        get("/help",                     (req, res) -> { return new ModelAndView(null, "012000_help.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/account_signup_confirm", (req, res) -> { return requireLoggedIn(req, res, "002000_account_signup_confirm.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/accept_tos",             (req, res) -> { return acceptToS(req, res, true); }, new HandlebarsTemplateEngine());
        get("/s/decline_tos",            (req, res) -> { return acceptToS(req, res, false); }, new HandlebarsTemplateEngine());
        get("/s/account_details",        (req, res) -> { return requireLoggedIn(req, res, "004000_account_details.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/event_signup",           (req, res) -> { return requireLoggedIn(req, res, "005000_event_signup.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/select_event",           (req, res) -> { return requireLoggedIn(req, res, "005500_select_event.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/schedule_signup",        (req, res) -> { return requireLoggedIn(req, res, "006000_schedule_signup.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/schedule",               (req, res) -> { return requireLoggedIn(req, res, "007000_schedule.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/confirm_presence",       (req, res) -> { return requireLoggedIn(req, res, "008000_confirm_presence.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/in_session",             (req, res) -> { return requireLoggedIn(req, res, "009000_in_session.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/matches",                (req, res) -> { return requireLoggedIn(req, res, "010000_matches.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/notifications",          (req, res) -> { return requireLoggedIn(req, res, "011000_notifications.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/uses_of_information",    (req, res) -> { return requireLoggedIn(req, res, "013000_uses_of_information.hbs"); }, new HandlebarsTemplateEngine());

        put("/s/put/account_details",    (req, res) -> {return UserController.handleUpdateUserDetails(req, res); });
        
        post("/s/post/event_signup",     (req, res) -> {return handleEventSignup(req, res); });
        post("/s/post/select_event",     (req, res) -> {return handleSelectEvent(req, res); });
		
//		handle CRUD routes for contacts
		get("/s/contacts", (req, res) -> {return ContactController.serveDashboard(req, res);}, new HandlebarsTemplateEngine());
		delete("/s/delete/contact/:id", (req, res)-> {return ContactController.handleDeleteContact(req, res);}, new JsonTransformer());
        put("/s/put/contact/:id", "application/json", (req, res) -> {return ContactController.handleUpdateContact(req, res);});
        post("/s/post/contact", "application/json", (req, res) -> {return ContactController.handleNewContact(req, res);});
        
        
        
        // Admin
        
        get("/a/admin",                  (req, res) -> { return requireLoggedIn(req, res, "a_001000_admin.hbs"); }, new HandlebarsTemplateEngine());
        get("/a/locations",              (req, res) -> { return LocationController.serveIndex(req, res, "a_002000_locations.hbs"); }, new HandlebarsTemplateEngine());
        get("/a/events",                 (req, res) -> { return EventController.serveIndex(req, res, "a_003000_events.hbs"); }, new HandlebarsTemplateEngine());
        get("/a/sessions",               (req, res) -> { return SessionController.serveIndex(req, res, "a_004000_sessions.hbs"); }, new HandlebarsTemplateEngine());

        post("/a/post/create_location",  (req, res) -> { return LocationController.handleNewLocation(req, res); });
        put("/a/put/update_location",    (req, res) -> { return LocationController.handleUpdateLocation(req, res); });

        post("/a/post/create_event",     (req, res) -> { return EventController.handleNewEvent(req, res); });
        put("/a/put/update_event",       (req, res) -> { return EventController.handleUpdateEvent(req, res); });

        post("/a/post/create_session",   (req, res) -> { return SessionController.handleNewSession(req, res); });
        put("/a/put/update_session",     (req, res) -> { return SessionController.handleUpdateSession(req, res); });
    }

    public ModelAndView requireLoggedIn(Request req, Response res, String intendedView) {
        //get user particulars from the req.session
        //they must not be null, else the user wil be redirected to the login page
        //TODO This doesn't work; it just throws an NPE
        String userId = req.session(false).attribute(Path.Web.ATTR_USER_ID).toString();
        //TODO Does "email" come from the session???
        String email = req.session(false).attribute(Path.Web.ATTR_EMAIL).toString();

        if (userId != null && !userId.isEmpty()) {
            HashMap<String, Object> model = new HashMap<>();
            model.putAll(prepareData(userId));
            model.put("email", (email == null) ? "" : email);

            return new ModelAndView(model, intendedView);
        } else {
            logger.warn("userID not found in Session"); //session expired
            res.header("Location", "/login");
            res.redirect("/login");
            return null;
        }
    }
    
    //TODO Include selectively?
    public HashMap<String, Object> prepareData(String userId) {
        Datastore ds = dbHelper.getDataStore();
        User u = ds.get(User.class, new ObjectId(userId));

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", u.id);
        map.put("userName", u.name);
        map.put("userDOB", u.dob);
        map.put("userPhone", u.phone);
        map.put("userGender", u.gender);
        map.put("userGenderCismaleChecked", u.gender == User.Gender.CISMALE ? "checked" : "");
        map.put("userGenderCisfemaleChecked", u.gender == User.Gender.CISFEMALE ? "checked" : "");
        map.put("userGenderTransmaleChecked", u.gender == User.Gender.TRANSMALE ? "checked" : "");
        map.put("userGenderTransfemaleChecked", u.gender == User.Gender.TRANSFEMALE ? "checked" : "");
        map.put("userGenderOtherChecked", u.gender == User.Gender.OTHER ? "checked" : "");
        map.put("userAttraction", u.attractedTo);
        map.put("userAttractionCismaleChecked", u.attractedTo.contains(User.Gender.CISMALE) ? "checked" : "");
        map.put("userAttractionCisfemaleChecked", u.attractedTo.contains(User.Gender.CISFEMALE) ? "checked" : "");
        map.put("userAttractionTransmaleChecked", u.attractedTo.contains(User.Gender.TRANSMALE) ? "checked" : "");
        map.put("userAttractionTransfemaleChecked", u.attractedTo.contains(User.Gender.TRANSFEMALE) ? "checked" : "");
        map.put("userAttractionOtherChecked", u.attractedTo.contains(User.Gender.OTHER) ? "checked" : "");
        map.put("userAcceptableMinAge", u.minAge);
        map.put("userAcceptableMaxAge", u.maxAge);
        map.put("userMatchInfo", u.getCurrentMatchText(false));
        map.put("userRegisteredEvents", u.registeredEvents);
        map.put("userSelectedEvent", u.selectedEvent);
        
        return map;
    }
    
    public ModelAndView acceptToS(Request req, Response res, boolean accept) {
        //get user particulars from the req.session
        //they must not be null, else the user wil be redirected to the login page
        //TODO This doesn't work; it just throws an NPE
        String userId = req.session(false).attribute(Path.Web.ATTR_USER_ID).toString();
        //TODO Does "email" come from the session???
        String email = req.session(false).attribute(Path.Web.ATTR_EMAIL).toString();

        if (userId != null && !userId.isEmpty()) {
            Datastore ds = dbHelper.getDataStore();
            User u = ds.get(User.class, new ObjectId(userId));
            
            if (accept) {
                u.acceptedTOS = true;
                ds.save(u);

                logger.info("user accepted ToS");
                res.header("Account Details", "/s/account_details"); //THINK Is this where they should go?
                res.redirect("/s/account_details");
                return null;
            } else {
                u.acceptedTOS = false;
                ds.save(u);

                logger.info("user declined ToS");
                Session session = req.session(false); // Sign them out
                if (session != null) {
                    session.invalidate();
                }
                res.header("Home page", "/"); //THINK Is this where they should go?
                res.redirect("/");
                return null;
            }
        } else {
            logger.warn("userID not found in Session"); //session expired
            res.header("Location", "/login");
            res.redirect("/login");
            return null;
        }
    }

	

        
     public static int getHerokuAssignedPort() {
//         this will get the heroku assigned port in production
//         or return 8080 for use in local dev
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 8080; //return 8080 on localhost
    }

		
	public static void main(String[] args) {
		new App();
	}

    //TODO Move somewhere more fitting
    public String handleEventSignup(Request req, Response res) {
        //DO Require proper page order
        try {
            String eventCode = Strings.nullToEmpty(req.queryParams("eventCode")).trim();

            //TODO Not really a good event code mechanism
            Datastore ds = dbHelper.getDataStore();
            Event e = ds.get(Event.class, new ObjectId(eventCode));
            if (e != null) {
                String userId = req.session(false).attribute(Path.Web.ATTR_USER_ID).toString();
                User u = ds.get(User.class, new ObjectId(userId));
                u.registeredEvents.add(e);
                ds.save(u);
                
                logger.info("registered for event");
                res.status(200);
                return (e.name);
            } else {
                logger.info("incorrect event code");
                res.status(422);
                return "incorrect event code";
            }
        } catch (Exception e) {
            //TODO Just throw the exception?
            logger.info("error parsing data from handleEventSignup \n");
            e.printStackTrace();
            res.status(500);
        }

        return res.body();
    }

    //TODO Move somewhere more fitting
    public String handleSelectEvent(Request req, Response res) {
        //DO Require proper page order
        try {
            String eventId = Strings.nullToEmpty(req.queryParams("eventId")).trim();

            Datastore ds = dbHelper.getDataStore();
            Event e = ds.get(Event.class, new ObjectId(eventId));
            if (e != null) {
                String userId = req.session(false).attribute(Path.Web.ATTR_USER_ID).toString();
                User u = ds.get(User.class, new ObjectId(userId));
                boolean success = u.setSelectedEvent(e);
                ds.save(u);

                if (success) {
                    logger.info("selected event");
                    res.status(200);
                    return (e.name);
                } else {
                    logger.info("failed to select event");
                    res.status(500);
                    return ("failed to select event");
                }
            } else {
                logger.info("incorrect event id");
                res.status(422);
                return "incorrect event id";
            }
        } catch (Exception e) {
            //TODO Just throw the exception?
            logger.info("error parsing data from handleSelectEvent \n");
            e.printStackTrace();
            res.status(500);
        }

        return res.body();
    }
    
}
