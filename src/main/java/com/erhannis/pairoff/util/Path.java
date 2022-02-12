/**
 *
 */
package com.erhannis.pairoff.util;

/**
 * @author Seun Matt Date 13 Oct 2016 Year 2016 (c) SMATT Corporation
 *
 * This is a class that will contain static strings for our paths
 */
public class Path {

    /**
     * Constructor cloud contacts
     */
    public Path() {
    }

    public static class Web {
//        public static String UPDATE_PWD = "/s/user/:id"; //uses put method

        public static String ATTR_USER_ID = "userId";
        public static String ATTR_EMAIL = "email";
        public static String ATTR_NAME = "name";

        public static String OK_PATTERN = "[^a-zA-Z0-9:\",{}@_.\\- ]";
        public static int SESSION_TIMEOUT = 60 * 30; //30 mins
        public static String JSON_TYPE = "application/json";
        public static String AUTH_STATUS = "AUTH_STATUS";
    }

    public static class Database {

        public static String LOCAL_DBNAME = "contacts_db";
        public static String HOST = "127.0.0.1";
        public static int PORT = 27017;

        //the db uri is from the heroku platform
        public static String HEROKU_DB_URI = "mongodb://heroku_n35m7bx6:vf99qjg9otp744biaqjtepvurd@ds011725.mlab.com:11725/heroku_n35m7bx6";
        public static String HEROKU_DB_NAME = "heroku_n35m7bx6"; //this is the last part of the HEROKU_DB_URI

    }

    public static class Reply {

        public static int OK = 200;
        public static String OK_MSG = "Hurray! Operation Successful";
        public static int CONTACT_NOT_FOUND = 601;
        public static String CONTACT_NOT_FOUND_MSG = "Ooops! The resource is not found on the server";
    }

}
