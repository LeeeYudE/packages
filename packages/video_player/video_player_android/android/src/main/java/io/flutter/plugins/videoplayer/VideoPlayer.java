// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.videoplayer;

import static androidx.media3.common.Player.REPEAT_MODE_ALL;
import static androidx.media3.common.Player.REPEAT_MODE_OFF;

import android.content.Context;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.annotation.VisibleForTesting;
import androidx.annotation.Nullable;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.okhttp.OkHttpDataSource;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelector;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

import io.flutter.view.TextureRegistry;
// import io.github.anilbeesetti.nextlib.media3ext.ffdecoder.NextRenderersFactory;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import io.flutter.view.TextureRegistry.SurfaceProducer;


/**
 * A class responsible for managing video playback using {@link ExoPlayer}.
 *
 * <p>It provides methods to control playback, adjust volume, and handle seeking.
 */
public abstract class VideoPlayer {

    @NonNull
    protected final VideoPlayerCallbacks videoPlayerEvents;
    @NonNull
    protected ExoPlayer exoPlayer;

    @Nullable protected final SurfaceProducer surfaceProducer;
    protected ExoPlayerEventListener exoPlayerEventListener;

    /**
     * A closure-compatible signature since {@link java.util.function.Supplier} is API level 24.
     */
    public interface ExoPlayerProvider {
        /**
         * Returns a new {@link ExoPlayer}.
         *
         * @return new instance.
         */
        @NonNull
        ExoPlayer get();
    }

    public VideoPlayer(
            @NonNull VideoPlayerCallbacks events,
            @NonNull MediaItem mediaItem,
            @NonNull VideoPlayerOptions options,
            @Nullable SurfaceProducer surfaceProducer,
            @NonNull ExoPlayerProvider exoPlayerProvider) {
        this.videoPlayerEvents = events;
        this.surfaceProducer = surfaceProducer;
        exoPlayer = exoPlayerProvider.get();
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayerEventListener = createExoPlayerEventListener(exoPlayer, surfaceProducer);
        exoPlayer.addListener(exoPlayerEventListener);
        setAudioAttributes(exoPlayer, options.mixWithOthers);
    }

    void sendBufferingUpdate() {
        videoPlayerEvents.onBufferingUpdate(exoPlayer.getBufferedPosition());
    }

    @OptIn(markerClass = UnstableApi.class)
    void setAudioTrack(String id) {
        TrackSelector trackSelector = exoPlayer.getTrackSelector();
        List<Format> _audioFormats = exoPlayerEventListener._audioFormats;
        // Log.d("EXO", "setSubtitleTrack" + trackSelector + " " + _audioFormats);
        for (int i = 0; i < _audioFormats.size(); i++) {
            Format format = _audioFormats.get(i);
            if (format.id != null && format.id.equals(id)) {
                trackSelector.setParameters(
                        trackSelector.getParameters().buildUpon()
                                .setPreferredAudioLanguage(format.language).build());
                // Log.d("EXO", "setAudioTrack format" + format);
                break;
            }
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    void setSubtitleTrack(String id) {
        TrackSelector trackSelector = exoPlayer.getTrackSelector();
        List<Format> _subtitleFormats = exoPlayerEventListener._subtitleFormats;
        Log.d("EXO", "setSubtitleTrack id: " + id + ", available tracks: " + _subtitleFormats.size());
        
        if (_subtitleFormats == null || _subtitleFormats.isEmpty()) {
            Log.w("EXO", "No subtitle tracks available");
            return;
        }
        
        for (int i = 0; i < _subtitleFormats.size(); i++) {
            Format format = _subtitleFormats.get(i);
            Log.d("EXO", "Checking subtitle format: " + format);
            if (format.id != null && format.id.equals(id)) {
                String language = format.language != null ? format.language : "und";
                trackSelector.setParameters(
                        trackSelector.getParameters().buildUpon()
                                .setPreferredTextLanguage(language)
                                .setSelectUndeterminedTextLanguage(true)
                                .build());
                Log.d("EXO", "setSubtitleTrack format selected: " + format + ", language: " + language);
                return;
            }
        }
        Log.w("EXO", "Subtitle track with id '" + id + "' not found");
    }

    @OptIn(markerClass = UnstableApi.class)
    void enableFirstAvailableSubtitle() {
        TrackSelector trackSelector = exoPlayer.getTrackSelector();
        List<Format> _subtitleFormats = exoPlayerEventListener._subtitleFormats;
        
        if (_subtitleFormats != null && !_subtitleFormats.isEmpty()) {
            Format firstSubtitle = _subtitleFormats.get(0);
            String language = firstSubtitle.language != null ? firstSubtitle.language : "und";
            // 启用第一个可用字幕，无论什么语言
            trackSelector.setParameters(
                    trackSelector.getParameters().buildUpon()
                            .setPreferredTextLanguage(language)
                            .setSelectUndeterminedTextLanguage(true)
                            .build());
            Log.d("EXO", "Auto-enabled first subtitle track: " + firstSubtitle + ", language: " + language);
        }
    }

    /**
     * 获取当前选中的音轨
     * @return 当前选中的音轨Format，如果没有选中则返回null
     */
    @OptIn(markerClass = UnstableApi.class)
    public Format getCurrentSelectedAudioTrack() {
        if (exoPlayerEventListener != null) {
            return exoPlayerEventListener.getCurrentSelectedAudioTrack();
        }
        return null;
    }

    /**
     * 获取当前选中的字幕轨道
     * @return 当前选中的字幕轨道Format，如果没有选中则返回null
     */
    @OptIn(markerClass = UnstableApi.class)
    public Format getCurrentSelectedSubtitleTrack() {
        if (exoPlayerEventListener != null) {
            return exoPlayerEventListener.getCurrentSelectedSubtitleTrack();
        }
        return null;
    }

    /**
     * 获取当前选中轨道的详细信息
     * @return 包含当前选中音轨和字幕轨道信息的字符串
     */
    @OptIn(markerClass = UnstableApi.class)
    public String getCurrentSelectedTracksInfo() {
        if (exoPlayerEventListener != null) {
            return exoPlayerEventListener.getCurrentSelectedTracksInfo();
        }
        return "No track information available";
    }

    void seekTo(int location) {
        if (exoPlayer != null) {
            Player.Commands availableCommands = exoPlayer.getAvailableCommands();
            if (!availableCommands.contains(Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM)) {
                androidx.media3.common.util.Log.d("EXO Player", "seekTo = " + location + " not supported");
                return;
            }
            exoPlayer.seekTo(location);
        }
    }

    long getPosition() {
        return exoPlayer.getCurrentPosition();
    }

    public void dispose() {
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }



  @NonNull
  protected abstract ExoPlayerEventListener createExoPlayerEventListener(
      @NonNull ExoPlayer exoPlayer, @Nullable SurfaceProducer surfaceProducer);


  private static void setAudioAttributes(ExoPlayer exoPlayer, boolean isMixMode) {
    exoPlayer.setAudioAttributes(
        new AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MOVIE).build(),
        !isMixMode);
  }

  void play() {
    exoPlayer.play();
  }

  void pause() {
    exoPlayer.pause();
  }

  void setLooping(boolean value) {
    exoPlayer.setRepeatMode(value ? REPEAT_MODE_ALL : REPEAT_MODE_OFF);
  }

  void setVolume(double value) {
    float bracketedValue = (float) Math.max(0.0, Math.min(1.0, value));
    exoPlayer.setVolume(bracketedValue);
  }

  void setPlaybackSpeed(double value) {
    // We do not need to consider pitch and skipSilence for now as we do not handle them and
    // therefore never diverge from the default values.
    final PlaybackParameters playbackParameters = new PlaybackParameters(((float) value));

    exoPlayer.setPlaybackParameters(playbackParameters);
  }


  @NonNull
  public ExoPlayer getExoPlayer() {
    return exoPlayer;
  }


}
