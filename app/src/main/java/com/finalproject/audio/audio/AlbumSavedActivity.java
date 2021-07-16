package com.finalproject.audio.audio;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.audio.R;
import com.finalproject.audio.databse.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AlbumSavedActivity extends AppCompatActivity implements
    ArtistAlbumListAdapter.ArtistAlbumClickListener {

  private ListView lView;

  private List<ArtistModel> allList = new ArrayList<>();
  private ArtistAlbumListAdapter artistAlbumListAdapter;
  private DatabaseHelper databaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_album_details);

    databaseHelper = new DatabaseHelper(this);

    lView = findViewById(R.id.lv_album_data);

    allList = databaseHelper.getAllArtistData();
    setAdapter();
  }

  private void setAdapter() {

    artistAlbumListAdapter =
        new ArtistAlbumListAdapter(allList, AlbumSavedActivity.this, this, "delete");
    lView.setAdapter(artistAlbumListAdapter);
  }

  @Override public void OnClick(int position) {
    deleteData(allList.get(position), position);
  }

  public void deleteData(ArtistModel artistModel, int position) {
    databaseHelper.deleteArtistEntry(artistModel.getId());//Delete id from Database
    allList.remove(position);//Remove from list
    artistAlbumListAdapter.notifyDataSetChanged();//Update Adapter view

    if (allList.size() == 0) {
      Toast.makeText(this, "Favorite list is empty!!!", Toast.LENGTH_SHORT).show();
    }
  }
}