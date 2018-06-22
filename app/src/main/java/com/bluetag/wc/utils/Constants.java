package com.bluetag.wc.utils;

/**
 * Created by Jeffy on 4/2/2017.
 */

public class Constants {

    public static final int ITEMS_PER_AD = 5;

    public static class BundleKeys {
        public static final String BUNDLE_KEY_IMAGE_URL = "bundle_key_stadium_image_url";
        public static final String BUNDLE_KEY_STADIUM_MODEL = "bundle_key_stadium_model";
        public static final String BUNDLE_KEY_DETAIL_URL = "bundle_key_stadium_detail_url";
        public static final String BUNDLE_KEY_LIVE_SCORE = "bundle_key_live_score";
        public static final String BUNDLE_KEY_NAME_PRONOUNCE = "bundle_key_stadium_name";

        public static final String BUNDLE_TRANSITION_KEY_STADIUM_IMAGE = "bundle_transition_key_stadium_image";

        //Pastwinners
        public static final String BUNDLE_KEY_PAST_WINNER_MODEL = "bundle_key_past_winner_model";

        //Groups
        public static final String BUNDLE_KEY_GROUP_NAME = "bundle_key_group_name";

        public static final String BUNDLE_TRANSITION_KEY_PAST_WINNER_IMAGE = "bundle_transition_key_past_winner_image";

        //Rss Feed
        public static final String BUNDLE_KEY_RSS_MODEL = "bundle_key_rss_model";

        //Official Fifa World Cup
        public static final String FIFA_SITE = "http://www.fifa.com";

        public static final String KEY_FEED_URl = "FEED";

        //notification
        public static final String BUNDLE_KEY_NEWS_NOTIFICATION = "bundle_key_news_notification";

        public class RssItems{
            public static final int RSS_FOR_NEWS = 0;
            public static final int RSS_FOR_PHOTOS = 1;
            public static final int RSS_FOR_VIDEOS = 2;
        }
    }


    public class DB{
        public static final String DB_NAME = "russiaworldcup.db";
        public static final int DB_VERSION = 2;

        // MATCHES TABLE CONSTANTS
        public static final String MATCHES_TABLE = "matches";

        public static final String M_ID = "mid";
        public static final String DATE = "date";
        public static final String ROUND = "round";
        public static final String TEAM1 = "team1";
        public static final String TEAM2 = "team2";
        public static final String SCORE = "score";
        public static final String DETAILS = "details";
        public static final String MATCH_STATUS = "status";

        public static final String CREATE_MATCHES_TABLE = "CREATE TABLE "+ MATCHES_TABLE +
                "( " +
                M_ID +" TEXT PRIMARY KEY, " +
                DATE +" TEXT, " +
                ROUND +" TEXT, " +
                TEAM1 + " TEXT, " +
                TEAM2 + " TEXT, " +
                SCORE + " TEXT, " +
                DETAILS +" TEXT, " +
                MATCH_STATUS +" TEXT " +
                ");";

        public static final String DROP_MATCHES_TABLE = "DROP TABLE IF EXISTS " + MATCHES_TABLE;


        // POINT TABLE CONSTANTS
        public static final String POINT_TABLE = "points";

        public static final String P_ID = "pid";
        public static final String GROUP_NO = "group_no";
        public static final String TEAM_NO = "team_no";
        public static final String TEAM_NAME = "team_name";
        public static final String STATUS = "status";

        public static final String CREATE_POINTS_TABLE = "CREATE TABLE " + POINT_TABLE +
                "( " +
                P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GROUP_NO + " TEXT, " +
                TEAM_NO + " TEXT, " +
                TEAM_NAME + " TEXT, " +
                STATUS + " TEXT);";

        public static final String DROP_POINTS_TABLE = "DROP TABLE IF EXISTS " + POINT_TABLE;


        // GOALS TABLE CONSTANTS
        public static final String GOAL_TABLE = "goals";

        public static final String G_ID = "gid";
        public static final String NAME = "name";
        public static final String GOALS = "goal";
        public static final String TAG = "tag";

        public static final String CREATE_GOAL_TABLE = "CREATE TABLE " + GOAL_TABLE +
                "( " +
                G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                GOALS + " TEXT, " +
                TAG + " TEXT);";

        public static final String DROP_GOAL_TABLE = "DROP TABLE IF EXISTS " + GOAL_TABLE;

        // MY TEAMS TABLE CONSTANTS
        public static final String MY_TEAMS_TABLE = "my_teams";
        public static final String MY_TEAM_NAME = "my_team_name";

        public static final String CREATE_MY_TEAMS_TABLE = "CREATE TABLE " + MY_TEAMS_TABLE +
                "( " + MY_TEAM_NAME + " TEXT PRIMARY KEY);";

        public static final String DROP_MY_TEAMS_TABLE = "DROP TABLE IF EXISTS " + MY_TEAMS_TABLE;
    }
}
