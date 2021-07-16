package com.finalproject.audio.audio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.finalproject.audio.R;

import java.util.List;

public class ArtistListAdapter extends BaseAdapter {

  private final List<ArtistModel> dataList;
  private final Context context;
  private final ArtistClickListener artistClickListener;

  public ArtistListAdapter(List<ArtistModel> dataList, Context context,
      ArtistClickListener clickListener) {
    this.dataList = dataList;
    this.context = context;
    artistClickListener = clickListener;
  }

  public interface ArtistClickListener {
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
          inflate(R.layout.artist_list_item, parent, false);
    }

    // get current item to be displayed
    ArtistModel artistModel = (ArtistModel) getItem(position);

    // get the TextView for item name and item description
    TextView tvRecipeTitle = convertView.findViewById(R.id.tv_recipe_title);

    //sets the text for item name and item description from the current item object
    tvRecipeTitle.setText(artistModel.getStrAlbum());

    tvRecipeTitle.setTag(position);
    tvRecipeTitle.setOnClickListener(v -> {

      int pos = (int) v.getTag();
      artistClickListener.OnClick(pos);
    });

    // returns the view for the current row
    return convertView;
  }
}


