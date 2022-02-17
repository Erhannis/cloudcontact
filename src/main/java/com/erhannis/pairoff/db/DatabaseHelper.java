/**
 *
 */
package com.erhannis.pairoff.db;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.erhannis.pairoff.model.User;
import com.erhannis.pairoff.contact.Contact;
import com.erhannis.pairoff.model.Event;
import com.erhannis.pairoff.model.Location;
import com.erhannis.pairoff.model.Match;
import com.erhannis.pairoff.model.Session;
import com.erhannis.pairoff.model.SpeedDate;
import com.erhannis.pairoff.util.Path;

/**
 * @author Seun Matt Date 13 Oct 2016 Year 2016 (c) SMATT Corporation
 */
public class DatabaseHelper {
    /**
     * Constructor cloud contacts
     */
    private static Morphia morphia = new Morphia();
    private static Datastore datastore = null;

    private static Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

    public DatabaseHelper() {
        if (!morphia.isMapped(Contact.class)) {
            morphia.map(Contact.class);
            morphia.map(User.class);
            morphia.map(Event.class);
            morphia.map(Location.class);
            morphia.map(Match.class);
            morphia.map(Session.class);
            morphia.map(SpeedDate.class);
            initDatastore();
        } else {
            logger.info("Database Class Mapped Already!");
        }
    }

    void initDatastore() {

        ProcessBuilder processBuilder = new ProcessBuilder();
        MongoClient mongoClient;

        //this will fetch the MONGODB_URI environment variable on heroku
        //that holds the connection string to our database created by the heroku mLab add on
        String HEROKU_MLAB_URI = processBuilder.environment().get("MONGODB_URI");

        if (HEROKU_MLAB_URI != null && !HEROKU_MLAB_URI.isEmpty()) {
            //heroku environ
            logger.error("Remote MLAB Database Detected");
            mongoClient = new MongoClient(new MongoClientURI(HEROKU_MLAB_URI));
            datastore = morphia.createDatastore(mongoClient, Path.Database.HEROKU_DB_NAME);
        } else {
            //local environ
            logger.info("Local Database Detected");
            mongoClient = new MongoClient(Path.Database.HOST, Path.Database.PORT);
            datastore = morphia.createDatastore(mongoClient, Path.Database.LOCAL_DBNAME);
        }

        datastore.ensureIndexes();
        logger.info("Database connection successful and Datastore initiated");
    }

    public Datastore getDataStore() {
        if (datastore == null) {
            initDatastore();
        }

        return datastore;
    }
}
