package com.finalproject.audio.audio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.audio.R;
import com.finalproject.audio.databse.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class ArtistActivity extends AppCompatActivity implements
    ArtistListAdapter.ArtistClickListener {

  Toolbar myToolbar;
  RelativeLayout toolLayout;
  private EditText edtSearch;
  private ListView lView;
  private ProgressBar pBar;

  private final ArrayList<ArtistModel> allList = new ArrayList<>();

  SharedPreferences sp;
  String titleWord = "";

  //String[] lyricsArray;
  //String lyrics = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_audio);

    //Get the fields from the screen:
    toolLayout = findViewById(R.id.main);
    edtSearch = findViewById(R.id.edt_search);
    Button sButton = findViewById(R.id.search);
    lView = findViewById(R.id.lv_audio_data);
    pBar = findViewById(R.id.progressBarLyrics);

    new DatabaseHelper(this);

    edtSearch.setText("");

    sButton.setOnClickListener(c -> {
      pBar.setVisibility(View.VISIBLE);

      SharedPreferences.Editor editor = sp.edit();//
      titleWord = edtSearch.getText().toString();
      editor.putString("title", titleWord);
      editor.apply();

      String titleText = null;
      try {
        titleText = URLEncoder.encode(titleWord, "utf-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }

      new GetDataFromAPI().execute(
          "https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s=" + titleText);
    });

    sp = getSharedPreferences("artistLastInput", Context.MODE_PRIVATE);
    String savedTitle = sp.getString("title", "");
    edtSearch.setText(savedTitle);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater mInflater = getMenuInflater();
    mInflater.inflate(R.menu.menu_common, menu);
    return true;
  }

  //Menu List
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int itemId = item.getItemId();

    //if (itemId == R.id.about) {
    //  alertMessage();
    //  makeText(this, "info", LENGTH_SHORT).show();
    //}

    if (itemId == R.id.saved) {

      Toast.makeText(this, "Saved Artist Data", LENGTH_SHORT).show();

      Intent intent = new Intent(this, AlbumSavedActivity.class);
      startActivity(intent);
    }

    if (itemId == R.id.exit) {
      showSnackbar(myToolbar);
    }

    return super.onOptionsItemSelected(item);
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

        JSONArray result = jObject.optJSONArray("album");

        allList.clear();

        for (int i = 0; i < result.length(); i++) {

          JSONObject data = result.optJSONObject(i);
          allList.add(new ArtistModel(data.optString("idAlbum"), data.optString("strAlbum"),
              data.optString("strArtist")));
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

    ArtistListAdapter artistListAdapter = new ArtistListAdapter(allList, ArtistActivity.this, this);
    lView.setAdapter(artistListAdapter);
  }

  @Override public void OnClick(int position) {

    Intent intent = new Intent(ArtistActivity.this, AlbumDetailsActivity.class);
    intent.putExtra("albumId", allList.get(position).getAlbumId());
    startActivity(intent);
  }

  public void showSnackbar(Toolbar view) {
    //Showing snackbar
    final Snackbar sb = Snackbar.make(lView, "Want to exit?", Snackbar.LENGTH_LONG);
    sb.setAction("Exit", e -> finish());
    sb.show();
  }

  //public void alertMessage() {
  //
  //  View middle = getLayoutInflater().inflate(R.layout.lyrics_activity_info, null);
  //
  //  AlertDialog.Builder builder = new AlertDialog.Builder(this);
  //
  //  builder.setTitle(R.string.d_help_tittle);
  //  builder.setMessage(R.string.lyrics_info_text);
  //
  //  builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
  //    public void onClick(DialogInterface dialog, int id) {
  //    }
  //  });
  //
  //  builder.create().show();
  //}
}