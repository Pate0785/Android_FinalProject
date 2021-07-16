package com.finalproject.audio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.finalproject.audio.audio.ArtistActivity;

public class HomeActivity extends AppCompatActivity {

  Button btnAudio;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    btnAudio = findViewById(R.id.btn_audio);

    btnAudio.setOnClickListener(n -> {
      Intent intent = new Intent(HomeActivity.this, ArtistActivity.class);
      startActivity(intent);
    });
  }
}
