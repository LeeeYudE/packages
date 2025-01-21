// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.videoplayer;

import static androidx.media3.common.Player.REPEAT_MODE_ALL;
import static androidx.media3.common.Player.REPEAT_MODE_OFF;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.okhttp.OkHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.TrackSelector;

import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.flutter.view.TextureRegistry;
import io.github.anilbeesetti.nextlib.media3ext.ffdecoder.NextRenderersFactory;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@UnstableApi
final class VideoPlayer implements TextureRegistry.SurfaceProducer.Callback {
    @NonNull
    private final ExoPlayerProvider exoPlayerProvider;
    @NonNull
    private final MediaItem mediaItem;
    @NonNull
    private final TextureRegistry.SurfaceProducer surfaceProducer;
    @NonNull
    private final VideoPlayerCallbacks videoPlayerEvents;
    @NonNull
    private final VideoPlayerOptions options;
    @NonNull
    private ExoPlayer exoPlayer;
    @Nullable
    private ExoPlayerState savedStateDuring;
    private DefaultTrackSelector trackSelector;
    private static OkHttpDns okHttpDns = new OkHttpDns();
    private static OkHttpClient client;
    // 改进后的正则表达式
    static final String regex = "(http|https)://(.*?):(.*?)@(.*?)(?::(\\d+))?(/.*)";

    /**
     * Creates a video player.
     *
     * @param context         application context.
     * @param events          event callbacks.
     * @param surfaceProducer produces a texture to render to.
     * @param asset           asset to play.
     * @param options         options for playback.
     * @return a video player instance.
     */
    @OptIn(markerClass = UnstableApi.class)
    @NonNull
    static VideoPlayer create(
            @NonNull Context context,
            @NonNull VideoPlayerCallbacks events,
            @NonNull TextureRegistry.SurfaceProducer surfaceProducer,
            @NonNull VideoAsset asset,
            @NonNull VideoPlayerOptions options) {
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(context);
        return new VideoPlayer(
                context,
                () -> {
                    // 创建 RenderersFactory 并启用 FFmpeg 扩展
                    RenderersFactory renderersFactory = new NextRenderersFactory(context).setEnableDecoderFallback(true).setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);
                    // RenderersFactory renderersFactory = new DefaultRenderersFactory(context)
                    //         .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);

                    ExoPlayer.Builder builder =
                            new ExoPlayer.Builder(context)
                                    .setRenderersFactory(renderersFactory);

                    // 获取当前的 TrackSelector
                    trackSelector.setParameters(
                            trackSelector.buildUponParameters()
                                    .setPreferredTextLanguage("en") // 选择字幕语言
                                    .setRendererDisabled(C.TRACK_TYPE_TEXT, false)); // 启用字幕渲染器

                    builder.setTrackSelector(trackSelector);

                    // trackSelector.setParameters(
                    //         trackSelector.getParameters().buildUpon()
                    //                 .setPreferredTextLanguage(preferredTextLanguage) //这个方法就是字幕轨道
                    //                 .setPreferredAudioLanguage(preferredAudioLanguage)); //这个方法是音轨
                    String DNS = null;
                    HttpVideoAsset httpVideoAsset = (HttpVideoAsset) asset;
                    Map<String, String> httpHeaders = httpVideoAsset.getHttpHeaders();
                    String userAgent = null;
                    if (httpHeaders != null && httpHeaders.containsKey("user-agent")) {
                        userAgent = httpHeaders.get("user-agent");
                    }
                    if (httpHeaders != null && httpHeaders.containsKey("DNS")) {
                        DNS = httpHeaders.get("DNS");
                        okHttpDns.setDNS(DNS);
                    } else {
                        okHttpDns.setDNS(null);
                    }
                    if (asset.assetUrl.startsWith("http") && userAgent != null) {
                        Log.d("EXO", "HttpVideoAsset");

                        builder.setMediaSourceFactory(new DefaultMediaSourceFactory(
                                new DataSource.Factory() {
                                    @Override
                                    public DataSource createDataSource() {
                                        return new OkHttpDataSource.Factory(
                                                new Call.Factory() {
                                                    @NotNull
                                                    @Override
                                                    public Call newCall(@NotNull Request request) {

                                                        // Log.d("EXO", "lookup:" + request.url() + " dns:" + finalDNS);
                                                        //
                                                        // Pattern pattern = Pattern.compile(regex);
                                                        // Matcher matcher = pattern.matcher(request.url().toString());
                                                        //
                                                        // if (matcher.find()) {
                                                        //     String protocol = matcher.group(1); // 协议
                                                        //     String username = matcher.group(2); // 用户名
                                                        //     String password = matcher.group(3); // 密码
                                                        //     String host = matcher.group(4);     // 主机名
                                                        //     String port = matcher.group(5);     // 端口号（可选）
                                                        //     String path = matcher.group(6);     // 路径
                                                        //
                                                        //     // 构建 Basic Authentication Header
                                                        //     String credentials = username + ":" + password;
                                                        //     String basicAuth;
                                                        //     if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        //         basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
                                                        //     } else {
                                                        //         basicAuth = "Basic " + android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
                                                        //     }
                                                        //
                                                        //     // 重新构造 URL（移除用户名和密码）
                                                        //     String cleanUrl = protocol + "://" + host;
                                                        //     if (port != null) {
                                                        //         cleanUrl += ":" + port; // 添加端口号
                                                        //     }
                                                        //     cleanUrl += path;
                                                        //
                                                        //     // 构建请求（保留原始域名作为 Host 头）
                                                        //     Request.Builder builder = new Request.Builder()
                                                        //             .url(cleanUrl)
                                                        //             .addHeader("Authorization", basicAuth);
                                                        //     if (finalDNS != null) {
                                                        //         builder.addHeader("Host", host);// 设置 Host 头
                                                        //     }
                                                        //
                                                        //     request = builder.build();
                                                        // } else {
                                                        //     if (finalDNS != null) {
                                                        //         request = request.newBuilder().addHeader("Host", request.url().host()).build();
                                                        //     }
                                                        // }
                                                        return client.newCall(request);
                                                    }
                                                }
                                        ).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36").createDataSource();

                                    }
                                }
                        ));
                    } else {
                        Log.d("EXO", "getMediaSourceFactory");
                        builder.setMediaSourceFactory(asset.getMediaSourceFactory(context));
                    }

                    return builder.build();
                },
                events,
                surfaceProducer,
                asset.getMediaItem(),
                options, trackSelector);
    }


    /**
     * A closure-compatible signature since {@link java.util.function.Supplier} is API level 24.
     */
    interface ExoPlayerProvider {
        /**
         * Returns a new {@link ExoPlayer}.
         *
         * @return new instance.
         */
        ExoPlayer get();
    }

    @OptIn(markerClass = UnstableApi.class)
    @VisibleForTesting
    VideoPlayer(
            @NonNull Context context,
            @NonNull ExoPlayerProvider exoPlayerProvider,
            @NonNull VideoPlayerCallbacks events,
            @NonNull TextureRegistry.SurfaceProducer surfaceProducer,
            @NonNull MediaItem mediaItem,
            @NonNull VideoPlayerOptions options,
            @NonNull DefaultTrackSelector trackSelector) {
        this.exoPlayerProvider = exoPlayerProvider;
        this.videoPlayerEvents = events;
        this.surfaceProducer = surfaceProducer;
        this.mediaItem = mediaItem;
        this.options = options;
        this.trackSelector = trackSelector;
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .dns(okHttpDns)
                    .build();
        }
        this.exoPlayer = createVideoPlayer(context);
        surfaceProducer.setCallback(this);
    }


    public void onSurfaceCreated() {

    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void onSurfaceDestroyed() {
        // Intentionally do not call pause/stop here, because the surface has already been released
        // at this point (see https://github.com/flutter/flutter/issues/156451).
        savedStateDuring = ExoPlayerState.save(exoPlayer);
        exoPlayer.release();
    }

    ExoPlayerEventListener exoPlayerEventListener;

    private ExoPlayer createVideoPlayer(Context context) {
        ExoPlayer exoPlayer = exoPlayerProvider.get();
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setVideoSurface(surfaceProducer.getSurface());

        boolean wasInitialized = savedStateDuring != null;
        exoPlayerEventListener = new ExoPlayerEventListener(exoPlayer, videoPlayerEvents, wasInitialized);
        exoPlayer.addListener(exoPlayerEventListener);
        setAudioAttributes(exoPlayer, options.mixWithOthers);

        return exoPlayer;
    }

    void sendBufferingUpdate() {
        videoPlayerEvents.onBufferingUpdate(exoPlayer.getBufferedPosition());
    }

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

    void seekTo(int location) {
        exoPlayer.seekTo(location);
    }

    @OptIn(markerClass = UnstableApi.class)
    void setAudioTrack(String id) {
        TrackSelector trackSelector = exoPlayer.getTrackSelector();
        List<Format> _audioFormats = exoPlayerEventListener._audioFormats;
        Log.d("EXO", "setSubtitleTrack" + trackSelector + " " + _audioFormats);
        for (int i = 0; i < _audioFormats.size(); i++) {
            Format format = _audioFormats.get(i);
            if (format.id != null && format.id.equals(id)) {
                trackSelector.setParameters(
                        trackSelector.getParameters().buildUpon()
                                .setPreferredAudioLanguage(format.language).build());
                Log.d("EXO", "setAudioTrack format" + format);
                break;
            }
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    void setSubtitleTrack(String id) {
        List<Format> _subtitleFormats = exoPlayerEventListener._subtitleFormats;
        Log.d("EXO", "setSubtitleTrack" + trackSelector + " " + _subtitleFormats);
        for (int i = 0; i < _subtitleFormats.size(); i++) {
            Format format = _subtitleFormats.get(i);
            if (format.id != null && format.id.equals(id)) {
                trackSelector.setParameters(
                        trackSelector.getParameters().buildUpon()
                                .setPreferredTextLanguage(format.language).build());
                Log.d("EXO", "setSubtitleTrack format" + format);
                break;
            }
        }
    }

    long getPosition() {
        return exoPlayer.getCurrentPosition();
    }

    void dispose() {
        trackSelector = null;
        exoPlayer.release();
        surfaceProducer.release();
        // TODO(matanlurey): Remove when embedder no longer calls-back once released.
        // https://github.com/flutter/flutter/issues/156434.
        surfaceProducer.setCallback(null);
    }
}
