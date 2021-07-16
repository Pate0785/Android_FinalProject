package com.finalproject.audio.audio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.audio.R;
import com.finalproject.audio.databse.DatabaseHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class AlbumDetailsActivity extends AppCompatActivity implements
    ArtistAlbumListAdapter.ArtistAlbumClickListener {

  private ListView lView;
  private ProgressBar pBar;

  private final ArrayList<ArtistModel> allList = new ArrayList<>();

  private DatabaseHelper databaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_album_details);

    databaseHelper = new DatabaseHelper(this);

    lView = findViewById(R.id.lv_album_data);
    pBar = findViewById(R.id.progressBarLyrics);

    String albumId = getIntent().getStringExtra("albumId");

    new GetDataFromAPI().execute(
        "https://theaudiodb.com/api/v1/json/1/track.php?m=" + albumId);
  }

  private class GetDataFromAPI extends AsyncTask<String, Integer, String> {

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      pBar.setProgress(values[0]);
    }

    @Override
    protected String doInBackground(String... strings) {

      HttpURLConnection urlConnection = null;
      String line = null;
      String urlString = strings[0];
      publishProgress(25);

      try {
        URL wordURL = new URL(urlString);
        urlConnection = (HttpURLConnection) wordURL.openConnection();
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);
        urlConnection.connect();

        BufferedReader reader = null;
        publishProgress(25);
        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
            "UTF-8"), 8);
        line = reader.readLine();
        publishProgress(50);
        JSONObject jObject = new JSONObject(line);

        JSONArray result = jObject.optJSONArray("track");

        allList.clear();

        for (int i = 0; i < result.length(); i++) {

          JSONObject data = result.optJSONObject(i);
          allList.add(new ArtistModel(1, data.optString("idTrack"), data.optString("strAlbum"),
              data.optString("strArtist"), data.optString("strTrack"),
              data.optString("intTotalListeners"),
              data.optString("intTotalPlays")));
        }
        publishProgress(75);
      } catch (Exception e) {
        e.printStackTrace();
      }

      return "Finished";
    }

    @Override
    protected void onPostExecute(String s) {

      pBar.setProgress(100);
      pBar.setVisibility(View.INVISIBLE);

      setAdapter();
    }
  }

  private void setAdapter() {

    ArtistAlbumListAdapter artistListAdapter =
        new ArtistAlbumListAdapter(allList, AlbumDetailsActivity.this, this, "save");
    lView.setAdapter(artistListAdapter);
  }

  @Override public void OnClick(int position) {
    addData(allList.get(position));
  }

  public void addData(ArtistModel artistModel) {//add data to database methode

    if (databaseHelper.checkIfArtistDataExist(artistModel) > 0) {
      Toast.makeText(AlbumDetailsActivity.this, "Already Saved", Toast.LENGTH_SHORT).show();
    } else {

      //cv.put(COL_TRACK, artistModel.getStrTrack());
      //cv.put(COL_ALBUM, artistModel.getStrAlbum());
      //cv.put(COL_ARTIST, artistModel.getStrArtist());
      //cv.put(COL_TOTAL_LISTENERS, artistModel.getTotalListeners());
      //cv.put(COL_TOTAL_PLAY, artistModel.getTotalPlays());
      //cv.put(COL_TRACK_ID, artistModel.getTrackId());

      long id =
          databaseHelper.insertArtistData(artistModel.getStrTrack(), artistModel.getStrAlbum(),
              artistModel.getStrArtist(), artistModel.getTotalListeners(),
              artistModel.getTotalPlays(),artistModel.getTrackId());
      artistModel.setId(id);

      Toast.makeText(AlbumDetailsActivity.this, "Save Successfully", Toast.LENGTH_SHORT).show();
    }
  }
}