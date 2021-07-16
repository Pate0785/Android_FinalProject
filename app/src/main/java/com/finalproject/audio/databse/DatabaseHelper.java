package com.finalproject.audio.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.finalproject.audio.audio.ArtistModel;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

  public final static String DATABASE_NAME = "FinalProject";

  public final static String TABLE_NAME_ARTIST = "Artist_Data";
  public final static String COL_ALBUM = "album";
  public final static String COL_ARTIST = "artist";
  public final static String COL_TRACK = "track";
  public final static String COL_TRACK_ID = "track_id";
  public final static String COL_TOTAL_LISTENERS = "totalListeners";
  public final static String COL_TOTAL_PLAY = "totalPlay";
  public static final String COL_ID = "id";

  public final static int VERSION_NUMBER = 1;

  public final static String[] ALL_COLS_ARTIST =
      new String[] {COL_ID, COL_ALBUM, COL_TRACK, COL_ARTIST, COL_TOTAL_LISTENERS, COL_TOTAL_PLAY,
          COL_TRACK_ID};

  private static final String CREATE_TABLE_ARTIST = "CREATE TABLE IF NOT EXISTS "
      + TABLE_NAME_ARTIST
      + "("
      + COL_ID
      + " INTEGER primary key autoincrement, "
      + COL_TRACK
      + " text, "
      + COL_ALBUM
      + " text, "
      + COL_TOTAL_LISTENERS
      + " text, "
      + COL_TOTAL_PLAY
      + " text, "
      + COL_TRACK_ID
      + " text, "
      + COL_ARTIST
      + " text )";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION_NUMBER);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    //Create table query
    db.execSQL(CREATE_TABLE_ARTIST);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //Delete old table
    db.execSQL("drop table if exists " + TABLE_NAME_ARTIST);

    //Create new table
    onCreate(db);
  }

  // SQLiteDatabase db;

  public int checkIfArtistDataExist(ArtistModel artistModel) {

    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor =
        db.rawQuery("select * from "
                + TABLE_NAME_ARTIST
                + " where "
                + COL_TRACK_ID
                + " = '"
                + artistModel.getTrackId()
                + "';",
            null);

    int count = cursor.getCount();
    cursor.close();
    db.close();
    return count;
  }

  //method for storing data
  public long insertArtistData(String strTrack, String strAlbum, String strArtist,
      String totalListeners, String totalPlays, String trackId) {

    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(COL_TRACK, strTrack);
    cv.put(COL_ALBUM, strAlbum);
    cv.put(COL_ARTIST, strArtist);
    cv.put(COL_TOTAL_LISTENERS, totalListeners);
    cv.put(COL_TOTAL_PLAY, totalPlays);
    cv.put(COL_TRACK_ID, trackId);
    return db.insert(TABLE_NAME_ARTIST, null, cv);//return id
  }

  public void deleteArtistEntry(long id) {
    SQLiteDatabase db = getWritableDatabase();
    db.delete(TABLE_NAME_ARTIST, COL_ID + "=" + id, null);
    db.close();
  }

  public List<ArtistModel> getAllArtistData() {//Get data from database

    SQLiteDatabase db = getWritableDatabase();
    List<ArtistModel> list = new ArrayList<>();

    //get all the results from the databse
    Cursor cursor = db.query(TABLE_NAME_ARTIST, ALL_COLS_ARTIST, null, null, null, null, null);

    //Loop for getting nxt items
    while (cursor.moveToNext()) {

      //Find id,artist,lyrics in column
      final long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
      final String album = cursor.getString(cursor.getColumnIndex(COL_ALBUM));
      final String track = cursor.getString(cursor.getColumnIndex(COL_TRACK));
      final String trackId = cursor.getString(cursor.getColumnIndex(COL_TRACK_ID));
      final String artist = cursor.getString(cursor.getColumnIndex(COL_ARTIST));
      final String totalListeners = cursor.getString(cursor.getColumnIndex(COL_TOTAL_LISTENERS));
      final String totalPlay = cursor.getString(cursor.getColumnIndex(COL_TOTAL_PLAY));

      //add new object to list
      list.add(new ArtistModel(id, trackId, album, artist, track, totalListeners, totalPlay));
    }
    cursor.close();
    db.close();
    return list;//Return list
  }

  public Cursor getArtistData() {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor c = db.rawQuery("select * from " + TABLE_NAME_ARTIST + ";", null);
    Log.d("Cursor Object", DatabaseUtils.dumpCursorToString(c));

    return c;
  }
}