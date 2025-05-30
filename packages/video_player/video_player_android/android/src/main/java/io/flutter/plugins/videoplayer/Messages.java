// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
// Autogenerated from Pigeon (v22.5.0), do not edit directly.
// See also: https://pub.dev/packages/pigeon

package io.flutter.plugins.videoplayer;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.common.StandardMessageCodec;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Generated class from Pigeon.
 */
@SuppressWarnings({"unused", "unchecked", "CodeBlock2Expr", "RedundantSuppression", "serial"})
public class Messages {

    /**
     * Error class for passing custom error details to Flutter via a thrown PlatformException.
     */
    public static class FlutterError extends RuntimeException {

        /**
         * The error code.
         */
        public final String code;

        /**
         * The error details. Must be a datatype supported by the api codec.
         */
        public final Object details;

        public FlutterError(@NonNull String code, @Nullable String message, @Nullable Object details) {
            super(message);
            this.code = code;
            this.details = details;
        }
    }

    @NonNull
    protected static ArrayList<Object> wrapError(@NonNull Throwable exception) {
        ArrayList<Object> errorList = new ArrayList<>(3);
        if (exception instanceof FlutterError) {
            FlutterError error = (FlutterError) exception;
            errorList.add(error.code);
            errorList.add(error.getMessage());
            errorList.add(error.details);
        } else {
            errorList.add(exception.toString());
            errorList.add(exception.getClass().getSimpleName());
            errorList.add(
                    "Cause: " + exception.getCause() + ", Stacktrace: " + Log.getStackTraceString(exception));
        }
        return errorList;
    }

    @Target(METHOD)
    @Retention(CLASS)
    @interface CanIgnoreReturnValue {
    }

    /**
     * Generated class from Pigeon that represents data sent in messages.
     */
    public static final class CreateMessage {
        private @Nullable String asset;

        public @Nullable String getAsset() {
            return asset;
        }

        public void setAsset(@Nullable String setterArg) {
            this.asset = setterArg;
        }

        private @Nullable String uri;

        public @Nullable String getUri() {
            return uri;
        }

        public void setUri(@Nullable String setterArg) {
            this.uri = setterArg;
        }

        private @Nullable String packageName;

        public @Nullable String getPackageName() {
            return packageName;
        }

        public void setPackageName(@Nullable String setterArg) {
            this.packageName = setterArg;
        }

        private @Nullable String formatHint;

        public @Nullable String getFormatHint() {
            return formatHint;
        }

        public void setFormatHint(@Nullable String setterArg) {
            this.formatHint = setterArg;
        }

        private @NonNull Map<String, String> httpHeaders;

        public @NonNull Map<String, String> getHttpHeaders() {
            return httpHeaders;
        }

        public void setHttpHeaders(@NonNull Map<String, String> setterArg) {
            if (setterArg == null) {
                throw new IllegalStateException("Nonnull field \"httpHeaders\" is null.");
            }
            this.httpHeaders = setterArg;
        }

        /**
         * Constructor is non-public to enforce null safety; use Builder.
         */
        CreateMessage() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CreateMessage that = (CreateMessage) o;
            return Objects.equals(asset, that.asset)
                    && Objects.equals(uri, that.uri)
                    && Objects.equals(packageName, that.packageName)
                    && Objects.equals(formatHint, that.formatHint)
                    && httpHeaders.equals(that.httpHeaders);
        }

        @Override
        public int hashCode() {
            return Objects.hash(asset, uri, packageName, formatHint, httpHeaders);
        }

        public static final class Builder {

            private @Nullable String asset;

            @CanIgnoreReturnValue
            public @NonNull Builder setAsset(@Nullable String setterArg) {
                this.asset = setterArg;
                return this;
            }

            private @Nullable String uri;

            @CanIgnoreReturnValue
            public @NonNull Builder setUri(@Nullable String setterArg) {
                this.uri = setterArg;
                return this;
            }

            private @Nullable String packageName;

            @CanIgnoreReturnValue
            public @NonNull Builder setPackageName(@Nullable String setterArg) {
                this.packageName = setterArg;
                return this;
            }

            private @Nullable String formatHint;

            @CanIgnoreReturnValue
            public @NonNull Builder setFormatHint(@Nullable String setterArg) {
                this.formatHint = setterArg;
                return this;
            }

            private @Nullable Map<String, String> httpHeaders;

            @CanIgnoreReturnValue
            public @NonNull Builder setHttpHeaders(@NonNull Map<String, String> setterArg) {
                this.httpHeaders = setterArg;
                return this;
            }

            public @NonNull CreateMessage build() {
                CreateMessage pigeonReturn = new CreateMessage();
                pigeonReturn.setAsset(asset);
                pigeonReturn.setUri(uri);
                pigeonReturn.setPackageName(packageName);
                pigeonReturn.setFormatHint(formatHint);
                pigeonReturn.setHttpHeaders(httpHeaders);
                return pigeonReturn;
            }
        }

        @NonNull
        ArrayList<Object> toList() {
            ArrayList<Object> toListResult = new ArrayList<>(5);
            toListResult.add(asset);
            toListResult.add(uri);
            toListResult.add(packageName);
            toListResult.add(formatHint);
            toListResult.add(httpHeaders);
            return toListResult;
        }

        static @NonNull CreateMessage fromList(@NonNull ArrayList<Object> pigeonVar_list) {
            CreateMessage pigeonResult = new CreateMessage();
            Object asset = pigeonVar_list.get(0);
            pigeonResult.setAsset((String) asset);
            Object uri = pigeonVar_list.get(1);
            pigeonResult.setUri((String) uri);
            Object packageName = pigeonVar_list.get(2);
            pigeonResult.setPackageName((String) packageName);
            Object formatHint = pigeonVar_list.get(3);
            pigeonResult.setFormatHint((String) formatHint);
            Object httpHeaders = pigeonVar_list.get(4);
            pigeonResult.setHttpHeaders((Map<String, String>) httpHeaders);
            return pigeonResult;
        }
    }

    private static class PigeonCodec extends StandardMessageCodec {
        public static final PigeonCodec INSTANCE = new PigeonCodec();

        private PigeonCodec() {
        }

        @Override
        protected Object readValueOfType(byte type, @NonNull ByteBuffer buffer) {
            switch (type) {
                case (byte) 129:
                    return CreateMessage.fromList((ArrayList<Object>) readValue(buffer));
                default:
                    return super.readValueOfType(type, buffer);
            }
        }

        @Override
        protected void writeValue(@NonNull ByteArrayOutputStream stream, Object value) {
            if (value instanceof CreateMessage) {
                stream.write(129);
                writeValue(stream, ((CreateMessage) value).toList());
            } else {
                super.writeValue(stream, value);
            }
        }
    }

    /**
     * Generated interface from Pigeon that represents a handler of messages from Flutter.
     */
    public interface AndroidVideoPlayerApi {

        void initialize();

        @NonNull
        Long create(@NonNull CreateMessage msg);

        void dispose(@NonNull Long textureId);

        void setLooping(@NonNull Long textureId, @NonNull Boolean looping);

        void setVolume(@NonNull Long textureId, @NonNull Double volume);

        void setPlaybackSpeed(@NonNull Long textureId, @NonNull Double speed);

        void play(@NonNull Long textureId);

        @NonNull
        Long position(@NonNull Long textureId);

        void seekTo(@NonNull Long textureId, @NonNull Long position);

        void pause(@NonNull Long textureId);

        void setAudioTrack(@NonNull Long textureId, String id);

        void setSubtitleTrack(@NonNull Long textureId, String id);

        void setMixWithOthers(@NonNull Boolean mixWithOthers);

        void setMaxBufferMs(Long ms);

        void setMaxBufferBytes(Long bytes);

        /**
         * The codec used by AndroidVideoPlayerApi.
         */
        static @NonNull MessageCodec<Object> getCodec() {
            return PigeonCodec.INSTANCE;
        }

        /**
         * Sets up an instance of `AndroidVideoPlayerApi` to handle messages through the
         * `binaryMessenger`.
         */
        static void setUp(
                @NonNull BinaryMessenger binaryMessenger, @Nullable AndroidVideoPlayerApi api) {
            setUp(binaryMessenger, "", api);
        }

        static void setUp(
                @NonNull BinaryMessenger binaryMessenger,
                @NonNull String messageChannelSuffix,
                @Nullable AndroidVideoPlayerApi api) {
            messageChannelSuffix = messageChannelSuffix.isEmpty() ? "" : "." + messageChannelSuffix;
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.initialize"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                try {
                                    api.initialize();
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.create"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                CreateMessage msgArg = (CreateMessage) args.get(0);
                                try {
                                    Long output = api.create(msgArg);
                                    wrapped.add(0, output);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.dispose"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                try {
                                    api.dispose(textureIdArg);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.setLooping"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                Boolean loopingArg = (Boolean) args.get(1);
                                try {
                                    api.setLooping(textureIdArg, loopingArg);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.setVolume"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                Double volumeArg = (Double) args.get(1);
                                try {
                                    api.setVolume(textureIdArg, volumeArg);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.setPlaybackSpeed"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                Double speedArg = (Double) args.get(1);
                                try {
                                    api.setPlaybackSpeed(textureIdArg, speedArg);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.play"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                try {
                                    api.play(textureIdArg);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.position"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                try {
                                    Long output = api.position(textureIdArg);
                                    wrapped.add(0, output);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.seekTo"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                Long positionArg = (Long) args.get(1);
                                try {
                                    api.seekTo(textureIdArg, positionArg);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.pause"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                try {
                                    api.pause(textureIdArg);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.setMixWithOthers"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Boolean mixWithOthersArg = (Boolean) args.get(0);
                                try {
                                    api.setMixWithOthers(mixWithOthersArg);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.setMaxBufferMs"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long ms = (Long) args.get(0);
                                try {
                                    api.setMaxBufferMs(ms);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.setMaxBufferBytes"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long bytes = (Long) args.get(0);
                                try {
                                    api.setMaxBufferBytes(bytes);
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.setAudioTrack"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                Log.d("Exo", "setAudioTrack");
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                try {
                                    api.setAudioTrack(textureIdArg, (String) args.get(1));
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
            {
                BasicMessageChannel<Object> channel =
                        new BasicMessageChannel<>(
                                binaryMessenger,
                                "dev.flutter.pigeon.video_player_android.AndroidVideoPlayerApi.setSubtitleTrack"
                                        + messageChannelSuffix,
                                getCodec());
                if (api != null) {
                    channel.setMessageHandler(
                            (message, reply) -> {
                                Log.d("Exo", "setSubtitleTrack");
                                ArrayList<Object> wrapped = new ArrayList<>();
                                ArrayList<Object> args = (ArrayList<Object>) message;
                                Long textureIdArg = (Long) args.get(0);
                                try {
                                    api.setSubtitleTrack(textureIdArg, (String) args.get(1));
                                    wrapped.add(0, null);
                                } catch (Throwable exception) {
                                    wrapped = wrapError(exception);
                                }
                                reply.reply(wrapped);
                            });
                } else {
                    channel.setMessageHandler(null);
                }
            }
        }
    }
}
