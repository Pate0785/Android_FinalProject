package com.finalproject.audio.audio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.finalproject.audio.R;

import java.util.List;

public class ArtistAlbumListAdapter extends BaseAdapter {

  private final List<ArtistModel> dataList;
  private final Context context;
  private final ArtistAlbumClickListener artistClickListener;
  private final String type;

  public ArtistAlbumListAdapter(List<ArtistModel> dataList, Context context,
      ArtistAlbumClickListener clickListener, String type) {
    this.dataList = dataList;
    this.context = context;
    artistClickListener = clickListener;
    this.type = type;
  }

  public interface ArtistAlbumClickListener {
    void OnClick(int position);
  }

  @Override
  public int getCount() {
    return dataList.size();
  }

  @Override
  public Object getItem(int position) {
    return dataList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // inflate the layout for each list row
    if (convertView == null) {
      convertView = LayoutInflater.from(context).
          inflate(R.layout.artist_album_list_item, parent, false);
    }

    // get current item to be displayed
    ArtistModel artistModel = (ArtistModel) getItem(position);

    // get the TextView for item name and item description
    LinearLayout llData = convertView.findViewById(R.id.ll_data);
    TextView tvArtistName = convertView.findViewById(R.id.tv_artist_name);
    TextView tvTrackName = convertView.findViewById(R.id.tv_track_name);
    TextView tvTotalListeners = convertView.findViewById(R.id.tv_total_listeners);
    TextView tvTotalPlay = convertView.findViewById(R.id.tv_total_play);
    Button btnSave = convertView.findViewById(R.id.btn_save);

    if (type.equals("delete")) {
      btnSave.setText("Delete");
    } else {
      btnSave.setText("Save");
    }

    //sets the text for item name and item description from the current item object
    tvArtistName.setText(String.format("Artist Name: %s", artistModel.getStrAlbum()));
    tvTrackName.setText(String.format("Track Name: %s", artistModel.getStrTrack()));
    tvTotalListeners.setText(String.format("Total Listeners: %s", artistModel.getTotalListeners()));
    tvTotalPlay.setText(String.format("Total Play: %s", artistModel.getTotalPlays()));

    llData.setTag(position);

    llData.setOnClickListener(v -> {

      int pos = (int) v.getTag();

      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse("http://www.google.com/search?q=" + dataList.get(pos).getStrArtist()));
      context.startActivity(i);
    });

    btnSave.setTag(position);
    btnSave.setOnClickListener(v -> {

      int pos = (int) v.getTag();
      artistClickListener.OnClick(pos);
    });

    // returns the view for the current row
    return convertView;
  }
}


