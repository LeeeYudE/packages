package io.flutter.plugins.videoplayer;

import android.app.ActivityManager;
import android.content.Context;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.LoadControl;

import android.util.Log;

/**
 * Created 2025/5/8 22:23
 * Author:charcolee
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------
 */
public class ExoPlayerUtils {

    /**
     * 根据设备内存大小创建动态 LoadControl
     *
     * @param context 应用上下文，用于获取内存信息
     * @return 配置好的 LoadControl
     */
    @OptIn(markerClass = UnstableApi.class)
    public static LoadControl createDynamicLoadControl(Context context, VideoPlayerOptions options) {
        // 获取设备总内存（MB）
        long totalMemoryMb = getTotalMemory(context);
        Log.d("EXO", " createDynamicLoadControl: " + totalMemoryMb + " " + options.maxBufferMs + " " + options.maxBufferBytes);
        // 根据内存大小设置 LoadControl 参数
        DefaultLoadControl.Builder builder = new DefaultLoadControl.Builder();

        if (options.maxBufferMs == 0) {
            if (totalMemoryMb < 2048) {
                // 低内存设备（<2GB，如 Amlogic 低端 TV 盒子）
                builder.setBufferDurationsMs(
                        500,    // minBufferMs: 0.5 秒
                        5000,   // maxBufferMs: 2 秒
                        250,    // bufferForPlaybackMs: 0.25 秒
                        500     // bufferForPlaybackAfterRebufferMs: 0.5 秒
                );
                builder.setTargetBufferBytes(10 * 1024 * 1024); // 1MB
            } else if (totalMemoryMb < 4096) {
                // 中等内存设备（2-4GB）
                builder.setBufferDurationsMs(
                        1000,   // minBufferMs: 1 秒
                        10000,   // maxBufferMs: 5 秒
                        500,    // bufferForPlaybackMs: 0.5 秒
                        1000    // bufferForPlaybackAfterRebufferMs: 1 秒
                );
                builder.setTargetBufferBytes(20 * 1024 * 1024); // 2MB
            } else {
                // 高内存设备（>4GB）
                builder.setBufferDurationsMs(
                        2000,   // minBufferMs: 2 秒
                        30000,  // maxBufferMs: 10 秒
                        1000,   // bufferForPlaybackMs: 1 秒
                        2000    // bufferForPlaybackAfterRebufferMs: 2 秒
                );
                builder.setTargetBufferBytes(40 * 1024 * 1024); // 5MB
            }
        } else {
            builder.setBufferDurationsMs(
                    2000,    // minBufferMs: 2 秒
                    (int) options.maxBufferMs,   // maxBufferMs: 用户设置的值
                    1000,    // bufferForPlaybackMs: 1 秒
                    2000     // bufferForPlaybackAfterRebufferMs: 2 秒
            );
        }

        if (options.maxBufferBytes == 0) {
            if (totalMemoryMb < 2048) {
                builder.setTargetBufferBytes(10 * 1024 * 1024); // 1MB
            } else if (totalMemoryMb < 4096) {
                builder.setTargetBufferBytes(20 * 1024 * 1024); // 2MB
            } else {
                builder.setTargetBufferBytes(40 * 1024 * 1024); // 5MB
            }
        } else {
            builder.setTargetBufferBytes((int) options.maxBufferBytes);
        }
        builder.setPrioritizeTimeOverSizeThresholds(true);

        return builder.build();
    }

    /**
     * 获取设备总内存（MB）
     *
     * @param context 应用上下文
     * @return 内存大小（MB）
     */
    private static long getTotalMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        // totalMem 是 API 16+ 可用，返回字节数，转换为 MB
        return memoryInfo.totalMem / (1024 * 1024);
    }

}
