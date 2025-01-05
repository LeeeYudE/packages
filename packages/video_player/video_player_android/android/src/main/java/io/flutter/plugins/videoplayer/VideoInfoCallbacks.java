// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.videoplayer;

import androidx.media3.common.Format;

import java.util.List;

/**
 * Callbacks representing events invoked by {@link VideoPlayer}.
 *
 * <p>In the actual plugin, this will always be {@link VideoPlayerEventCallbacks}, which creates the
 * expected events to send back through the plugin channel. In tests methods can be overridden in
 * order to assert results.
 *
 * <p>See {@link androidx.media3.common.Player.Listener} for details.
 */
interface VideoInfoCallbacks {
  void onTracksChanged(List<Format> _audioFormats,List<Format> _subtitleFormats);
}
