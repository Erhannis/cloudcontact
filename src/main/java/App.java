
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
import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.index.IndexController;
import com.erhannis.pairoff.util.JsonTransformer;
import com.erhannis.pairoff.util.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Session;

import spark.template.handlebars.HandlebarsTemplateEngine;

public class App {
    
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
        before("/*/", (req, res) -> {
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
		get("/login", (req, res) -> { return AuthController.serveLoginPage(req, res); }, new HandlebarsTemplateEngine());
		post("/post/login", (req, res) -> { return AuthController.handleLogin(req, res);} );
        post("/post/auth", (req, res) -> {return AuthController.handleAuth(req, res); } );
        get("/account_signup", (req, res) -> { return AuthController.serveSignUpPage(req, res); }, new HandlebarsTemplateEngine());
		post("/post/account_signup", (req, res) -> {return AuthController.handleSignUp(req, res);});
        get("/logout", (req, res) -> { return AuthController.handleSignOut(req, res); });
		
		
//		handle CRUD routes for contacts
		get("/contacts/", (req, res) -> {return ContactController.serveDashboard(req, res);}, new HandlebarsTemplateEngine());
		delete("/delete/contact/:id", (req, res)-> {return ContactController.handleDeleteContact(req, res);}, new JsonTransformer());
        put("/put/contact/:id", "application/json", (req, res) -> {return ContactController.handleUpdateContact(req, res); });
        post("/post/contact/", "application/json", (req, res) -> { return ContactController.handleNewContact(req, res);} );
                
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
