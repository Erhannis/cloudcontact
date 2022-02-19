
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
import com.erhannis.pairoff.contact.UserController;
import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.index.IndexController;
import com.erhannis.pairoff.model.User;
import com.erhannis.pairoff.util.JsonTransformer;
import com.erhannis.pairoff.util.Path;
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
                logger.warn("Secured Area! Login is REQUIRED");
                res.redirect("/login");
               halt(401);
            }
        });
                
//		Handle homepage routes
		get("/", (req, res) -> IndexController.serveHomePage(req, res), new HandlebarsTemplateEngine());

//		handle authentication routes
        //TODO Probably (almost) ALL the pages want User if it's available - for sidebar
		get("/login",                    (req, res) -> { return AuthController.serveLoginPage(req, res); }, new HandlebarsTemplateEngine());
		post("/post/login",              (req, res) -> { return AuthController.handleLogin(req, res);} );
        post("/post/auth",               (req, res) -> {return AuthController.handleAuth(req, res); } );
        get("/account_signup",           (req, res) -> { return AuthController.serveSignUpPage(req, res); }, new HandlebarsTemplateEngine());
		post("/post/account_signup",     (req, res) -> {return AuthController.handleSignUp(req, res);});
        get("/logout",                   (req, res) -> { return AuthController.handleSignOut(req, res); });
        get("/help",                     (req, res) -> { return new ModelAndView(null, "012000_help.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/account_signup_confirm", (req, res) -> { return requireLoggedIn(req, res, "002000_account_signup_confirm.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/account_details",        (req, res) -> { return requireLoggedIn(req, res, "004000_account_details.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/event_signup",           (req, res) -> { return requireLoggedIn(req, res, "005000_event_signup.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/select_event",           (req, res) -> { return requireLoggedIn(req, res, "005500_select_event.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/schedule_signup",        (req, res) -> { return requireLoggedIn(req, res, "006000_schedule_signup.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/schedule",               (req, res) -> { return requireLoggedIn(req, res, "007000_schedule.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/confirm_presence",       (req, res) -> { return requireLoggedIn(req, res, "008000_confirm_presence.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/in_session",             (req, res) -> { return requireLoggedIn(req, res, "009000_in_session.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/matches",                (req, res) -> { return requireLoggedIn(req, res, "010000_matches.hbs"); }, new HandlebarsTemplateEngine());
        get("/s/notifications",          (req, res) -> { return requireLoggedIn(req, res, "011000_notifications.hbs"); }, new HandlebarsTemplateEngine());
		
        post("/post/account_details", (req, res) -> {return UserController.handleUpdateUserDetails(req, res); });
		
//		handle CRUD routes for contacts
		get("/s/contacts", (req, res) -> {return ContactController.serveDashboard(req, res);}, new HandlebarsTemplateEngine());
		delete("/s/delete/contact/:id", (req, res)-> {return ContactController.handleDeleteContact(req, res);}, new JsonTransformer());
        put("/s/put/contact/:id", "application/json", (req, res) -> {return ContactController.handleUpdateContact(req, res); });
        post("/s/post/contact", "application/json", (req, res) -> { return ContactController.handleNewContact(req, res);} );
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
        map.put("userAttraction", u.attractedTo);
        map.put("userAcceptableMinAge", u.minAge);
        map.put("userAcceptableMaxAge", u.maxAge);
        map.put("userMatchInfo", u.getCurrentMatchText(false));

        return map;
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

	
}
