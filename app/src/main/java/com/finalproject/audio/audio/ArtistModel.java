package com.finalproject.audio.audio;

public class ArtistModel {

  public String strAlbum;
  public String strArtist;
  public String strTrack;
  public String trackId;
  public String totalListeners;
  public String totalPlays;
  public String albumId;

  public String getStrTrack() {
    return strTrack;
  }

  public void setStrTrack(String strTrack) {
    this.strTrack = strTrack;
  }

  public String getTrackId() {
    return trackId;
  }

  public void setTrackId(String trackId) {
    this.trackId = trackId;
  }

  public String getTotalListeners() {
    return totalListeners;
  }

  public void setTotalListeners(String totalListeners) {
    this.totalListeners = totalListeners;
  }

  public String getTotalPlays() {
    return totalPlays;
  }

  public void setTotalPlays(String totalPlays) {
    this.totalPlays = totalPlays;
  }

  public String getAlbumId() {
    return albumId;
  }

  public void setAlbumId(String albumId) {
    this.albumId = albumId;
  }

  public long id;

  // id, strAlbum, strArtist, ingredients

  /**
   * Constructor:
   */

  public ArtistModel(String albumId, String strAlbum, String strArtist) {
    this.albumId = albumId;
    this.strAlbum = strAlbum;
    this.strArtist = strArtist;
  }

  public ArtistModel(long id, String trackId, String strAlbum, String strArtist, String strTrack,
      String totalListeners,
      String totalPlays) {
    this.id = id;
    this.strAlbum = strAlbum;
    this.trackId = trackId;
    this.strArtist = strArtist;
    this.strTrack = strTrack;
    this.totalListeners = totalListeners;
    this.totalPlays = totalPlays;
  }

  public String getStrAlbum() {
    return strAlbum;
  }

  public void setStrAlbum(String strAlbum) {
    this.strAlbum = strAlbum;
  }

  public String getStrArtist() {
    return strArtist;
  }

  public void setStrArtist(String strArtist) {
    this.strArtist = strArtist;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}