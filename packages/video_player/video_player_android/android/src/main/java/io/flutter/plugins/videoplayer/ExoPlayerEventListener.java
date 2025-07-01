// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.videoplayer;

import static android.media.metrics.TrackChangeEvent.TRACK_TYPE_TEXT;
import static androidx.media3.common.C.TRACK_TYPE_AUDIO;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.common.Tracks;
import androidx.media3.common.VideoSize;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.TrackSelectionArray;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class ExoPlayerEventListener implements Player.Listener {
  private boolean isBuffering = false;
  private boolean isInitialized = false;
  protected final ExoPlayer exoPlayer;
  protected final VideoPlayerCallbacks events;
  private final VideoInfoCallbacks videoInfoCallbacks;

  protected enum RotationDegrees {
    ROTATE_0(0),
    ROTATE_90(90),
    ROTATE_180(180),
    ROTATE_270(270);

    private final int degrees;

    RotationDegrees(int degrees) {
      this.degrees = degrees;
    }

    public static RotationDegrees fromDegrees(int degrees) {
      for (RotationDegrees rotationDegrees : RotationDegrees.values()) {
        if (rotationDegrees.degrees == degrees) {
          return rotationDegrees;
        }
      }
      throw new IllegalArgumentException("Invalid rotation degrees specified: " + degrees);
    }

    public int getDegrees() {
      return this.degrees;
    }
  }

  public ExoPlayerEventListener(ExoPlayer exoPlayer, VideoPlayerCallbacks events) {
    this(exoPlayer, events, false);
  }

  public ExoPlayerEventListener(ExoPlayer exoPlayer, VideoPlayerCallbacks events, boolean initialized) {
    this(exoPlayer, events, initialized, null);
  }

  public ExoPlayerEventListener(@NonNull ExoPlayer exoPlayer,@NonNull VideoPlayerCallbacks events, boolean initialized, VideoInfoCallbacks videoInfoCallbacks) {
    this.exoPlayer = exoPlayer;
    this.events = events;
    this.isInitialized = initialized;
    this.videoInfoCallbacks = videoInfoCallbacks;
  }

  private void setBuffering(boolean buffering) {
    if (isBuffering == buffering) {
      return;
    }
    isBuffering = buffering;
    if (buffering) {
      events.onBufferingStart();
    } else {
      events.onBufferingEnd();
    }
  }

  protected abstract void sendInitialized();


  List<Format> _audioFormats;
  List<Format> _subtitleFormats;

  @OptIn(markerClass = UnstableApi.class) @Override
  public void onTracksChanged(Tracks tracks) {
    if(_audioFormats == null){
      _audioFormats = new ArrayList<>();
      _subtitleFormats = new ArrayList<>();
      ImmutableList<Tracks.Group> groups = tracks.getGroups();

      for (Tracks.Group group : groups) {

        int length = group.length;
        for(int i = 0; i < length; i++) {
          Format format = group.getTrackFormat(i);
          if(format.sampleMimeType != null && format.sampleMimeType.contains("audio")) {
            Log.d("Exo", "onTracksChanged audio "+format);
            _audioFormats.add(format);
          }else if(format.sampleMimeType != null && (format.sampleMimeType.contains("cues") 
                  || format.sampleMimeType.contains("text") 
                  || format.sampleMimeType.contains("tx3g") 
                  || format.sampleMimeType.contains("quicktime")
                  || format.sampleMimeType.contains("subtitle"))) {
            Log.d("Exo", "onTracksChanged subtitle "+format);
            _subtitleFormats.add(format);
          }
        }
      }

      events.onTracksChanged(_audioFormats, _subtitleFormats);
      
      // 自动启用第一个字幕轨道
      if (!_subtitleFormats.isEmpty()) {
          Format firstSubtitle = _subtitleFormats.get(0);
          String language = firstSubtitle.language != null ? firstSubtitle.language : "und";
          
          androidx.media3.exoplayer.trackselection.TrackSelector trackSelector = exoPlayer.getTrackSelector();
          if (trackSelector instanceof androidx.media3.exoplayer.trackselection.DefaultTrackSelector) {
              androidx.media3.exoplayer.trackselection.DefaultTrackSelector defaultTrackSelector = 
                  (androidx.media3.exoplayer.trackselection.DefaultTrackSelector) trackSelector;
              
              defaultTrackSelector.setParameters(
                  defaultTrackSelector.getParameters().buildUpon()
                      .setPreferredTextLanguage(language)
                      .setSelectUndeterminedTextLanguage(true)
                      .build());
              
              Log.d("Exo", "Auto-enabled first subtitle track: " + firstSubtitle + ", language: " + language);
          }
      }
    }

  }

  @Override
  public void onCues(CueGroup cueGroup) {
    int size = cueGroup.cues.size();
    if(size == 0){
      events.onCues("");
    }else{
      for (int i = 0; i < size; i++) {
        // Log.d("EXO", "onCues"+cueGroup.cues.get(i).text);
        events.onCues(cueGroup.cues.get(i).text);
      }
    }

  }

  @Override
  public void onPlaybackStateChanged(final int playbackState) {
    switch (playbackState) {
      case Player.STATE_BUFFERING:
        setBuffering(true);
        events.onBufferingUpdate(exoPlayer.getBufferedPosition());
        break;
      case Player.STATE_READY:
        if (!isInitialized) {
          isInitialized = true;
          sendInitialized();
        }
        break;
      case Player.STATE_ENDED:
        events.onCompleted();
        break;
      case Player.STATE_IDLE:
        break;
    }
    if (playbackState != Player.STATE_BUFFERING) {
      setBuffering(false);
    }
  }

  @Override
  public void onPlayerError(@NonNull final PlaybackException error) {
    setBuffering(false);
    if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
      // See
      // https://exoplayer.dev/live-streaming.html#behindlivewindowexception-and-error_code_behind_live_window
      exoPlayer.seekToDefaultPosition();
      exoPlayer.prepare();
    } else {
      events.onError("VideoError", "Video player had error " + error, null);
    }
  }

  @Override
  public void onIsPlayingChanged(boolean isPlaying) {
    events.onIsPlayingStateUpdate(isPlaying);
  }
}
