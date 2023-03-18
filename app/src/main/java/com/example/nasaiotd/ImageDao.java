package com.example.nasaiotd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;


public class ImageDao {

    public static final String DATABASE_NAME = "NasaIotdDatabase";

    private static final String TABLE_NAME = "IMAGE";

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_DATE = "date";

    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_EXPLANATION = "explanation";

    private static final String COLUMN_URL = "url";

    private static final String COLUMN_HDURL = "hdUrl";

    private static final String[] COLUMNS = {
            COLUMN_ID, COLUMN_DATE, COLUMN_TITLE, COLUMN_EXPLANATION,
            COLUMN_URL, COLUMN_HDURL
    };


    private final Context context;

    private final DatabaseOpener dbOpener;

    public ImageDao(Context context) {
        this.context = context;
        dbOpener = new DatabaseOpener(context);
    }


    public ImageData get(long id) {
        return find("id = ?", String.valueOf(id));
    }


    public ImageData find(String whereClause, String... whereArgs) {
        final SQLiteDatabase db = dbOpener.getWritableDatabase();

        Cursor result = db.query(false, TABLE_NAME, COLUMNS,
                whereClause, whereArgs, null, null, null, null);

        ImageData image = null;

        if (result.getCount() > 0) {
            final int idIndex = result.getColumnIndex(COLUMN_ID);
            final int dateIndex = result.getColumnIndex(COLUMN_DATE);
            final int titleIndex = result.getColumnIndex(COLUMN_TITLE);
            final int explanationIndex = result.getColumnIndex(COLUMN_EXPLANATION);
            final int urlIndex = result.getColumnIndex(COLUMN_URL);
            final int hdUrlIndex = result.getColumnIndex(COLUMN_HDURL);

            result.moveToFirst();

            long identifier = result.getLong(idIndex);
            String date = result.getString(dateIndex);
            String title = result.getString(titleIndex);
            String explanation = result.getString(explanationIndex);
            String url = result.getString(urlIndex);
            String hdUrl = result.getString(hdUrlIndex);

            image = new ImageData(identifier, date, title, explanation, url, hdUrl);

            String fileName = image.getFileName();

            Bitmap bitmap = ImageUtils.getImageFromLocal(context, fileName);
            image.setImage(bitmap);
        }

        result.close();
        db.close();

        return image;
    }

    /**
     * Loads all ImageData instances from the database.
     * @return List of ImageData instances.
     */
    public List<ImageData> load() {
        final SQLiteDatabase db = dbOpener.getWritableDatabase();

        Cursor result = db.query(false, TABLE_NAME, COLUMNS,
                null, null, null, null, null, null);

        List<ImageData> imageList = new ArrayList<ImageData>();

        if (result.getCount() > 0) {
            final int idIndex = result.getColumnIndex(COLUMN_ID);
            final int dateIndex = result.getColumnIndex(COLUMN_DATE);
            final int titleIndex = result.getColumnIndex(COLUMN_TITLE);
            final int explanationIndex = result.getColumnIndex(COLUMN_EXPLANATION);
            final int urlIndex = result.getColumnIndex(COLUMN_URL);
            final int hdUrlIndex = result.getColumnIndex(COLUMN_HDURL);

            while (result.moveToNext()) {
                long identifier = result.getLong(idIndex);
                String date = result.getString(dateIndex);
                String title = result.getString(titleIndex);
                String explanation = result.getString(explanationIndex);
                String url = result.getString(urlIndex);
                String hdUrl = result.getString(hdUrlIndex);

                ImageData image = new ImageData(identifier, date, title, explanation, url, hdUrl);
                imageList.add(image);

                String fileName = image.getFileName();

                Bitmap bitmap = ImageUtils.getImageFromLocal(context, fileName);
                image.setImage(bitmap);
            }
        }

        result.close();
        db.close();

        return imageList;
    }


    public long save(ImageData image) {
        final SQLiteDatabase db = dbOpener.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, image.getDate());
        contentValues.put(COLUMN_TITLE, image.getTitle());
        contentValues.put(COLUMN_EXPLANATION, image.getExplanation());
        contentValues.put(COLUMN_URL, image.getUrl());
        contentValues.put(COLUMN_HDURL, image.getHdUrl());

        long id = image.getId();

        if (id == 0) {
            id = db.insert(TABLE_NAME, null, contentValues);
        }
        else {
            contentValues.put(COLUMN_ID, image.getId());
            db.insert(TABLE_NAME, null, contentValues);
        }

        db.close();

        return id;
    }


    public void delete(ImageData image) {
        final SQLiteDatabase db = dbOpener.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(image.getId()) });
        db.close();
    }

    private class DatabaseOpener extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;

        public DatabaseOpener(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String sql = String.format("CREATE TABLE %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL);",
                    TABLE_NAME, COLUMN_ID, COLUMN_DATE, COLUMN_TITLE, COLUMN_EXPLANATION,
                    COLUMN_URL, COLUMN_HDURL);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String sql = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
            db.execSQL(sql);

            onCreate(db);
        }
    }
}
