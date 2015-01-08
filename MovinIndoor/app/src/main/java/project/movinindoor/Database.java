package project.movinindoor;


import android.provider.BaseColumns;

/**
 * Created by Ian on 8-12-2014.
 */
public final class Database {

    public Database(){}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "defects";
        public static final String COLUMN_NAME_DEFECT_ID = "defectid";
        public static final String COLUMN_NAME_SHORTDESCRIPTION = "shortdescription";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_BUILDING = "building";
        public static final String COLUMN_NAME_FLOOR = "floor";
        public static final String COLUMN_NAME_CLONG = "clong";
        public static final String COLUMN_NAME_CLAT = "clat";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_COMMENTS = "comments";

    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_NAME_DEFECT_ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_SHORTDESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_BUILDING + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_FLOOR + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CLONG + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CLAT + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_PRIORITY + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_COMMENTS + TEXT_TYPE + COMMA_SEP +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
