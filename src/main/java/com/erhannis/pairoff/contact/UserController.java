/**
 *
 */
package com.erhannis.pairoff.contact;

import com.erhannis.pairoff.db.DatabaseHelper;
import com.erhannis.pairoff.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erhannis.pairoff.util.Path;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import spark.Request;
import spark.Response;

public class UserController {
    static DatabaseHelper dbHelper = new DatabaseHelper();
    static Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController() {
    }

    public static int handleUpdateUserDetails(Request req, Response res) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            logger.info("raw body in handleUpdateUser = \n" + req.body());

            //JsonNode data = objectMapper.readTree(req.body());

            //TODO The path actually includes a user id....
//            String userId = req.params("id");
//            String thisUserId = req.session(false).attribute(Path.Web.ATTR_USER_ID);
//            if (!Objects.equal(userId, thisUserId)) {
//                logger.info("User not authorized to update different user's info; failing");
//                res.status(403);
//                return res.status();
//            }
            String userId = req.session(false).attribute(Path.Web.ATTR_USER_ID);
            
            Datastore ds = dbHelper.getDataStore();
            User u = ds.get(User.class, new ObjectId(userId));
            //req.queryParams()
            u.name = Strings.nullToEmpty(req.queryParams("userName")).trim();
            u.dob = LocalDate.parse(Strings.nullToEmpty(req.queryParams("userDOB")).trim()); //TODO Check date parsing
            u.phone = Strings.nullToEmpty(req.queryParams("userPhone")).trim();
            u.gender = User.Gender.valueOf(req.queryParams("userGender"));
            u.attractedTo.clear();
            if (Boolean.valueOf(req.queryParams("userAttractionCismale"))) {
                u.attractedTo.add(User.Gender.CISMALE);
            }
            if (Boolean.valueOf(req.queryParams("userAttractionCisfemale"))) {
                u.attractedTo.add(User.Gender.CISFEMALE);
            }
            if (Boolean.valueOf(req.queryParams("userAttractionTransmale"))) {
                u.attractedTo.add(User.Gender.TRANSMALE);
            }
            if (Boolean.valueOf(req.queryParams("userAttractionTransfemale"))) {
                u.attractedTo.add(User.Gender.TRANSFEMALE);
            }
            if (Boolean.valueOf(req.queryParams("userAttractionOther"))) {
                u.attractedTo.add(User.Gender.OTHER);
            }
            u.minAge = Integer.parseInt(Strings.nullToEmpty(req.queryParams("userAcceptableMinAge")).trim());
            u.maxAge = Integer.parseInt(Strings.nullToEmpty(req.queryParams("userAcceptableMaxAge")).trim());
            String curMatchText = Strings.nullToEmpty(u.getCurrentMatchText(false)).trim();
            String newMatchText = Strings.nullToEmpty(req.queryParams("userMatchInfo")).trim();
            if (!newMatchText.isEmpty() && (curMatchText.isEmpty() || !Objects.equal(curMatchText, newMatchText))) {
                u.matchInfo.put(new Date(), newMatchText);
            }
            
            // Validation
            Period p = Period.between(u.dob, LocalDate.now()); //TODO Timezone differences? ...I'm inclined not to worry about it.
            if (p.getYears() < 18) {
                logger.info("User younger than 18; failing");
                res.status(400);
                res.body("ERR_LESS_THAN_18YO");
                return res.status();
            }
            
            ds.save(u);
            
            res.status(200);
            logger.info("updated user info");
        } catch (Exception e) {
            //TODO Just throw the exception?
            logger.info("error parsing data from handleUpdateContact \n");
            e.printStackTrace();
            res.status(500);
        }

        return res.status();
    }
}
