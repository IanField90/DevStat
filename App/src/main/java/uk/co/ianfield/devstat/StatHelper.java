package uk.co.ianfield.devstat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;
import java.util.ArrayList;

import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by IanField90 on 17/06/2014.
 */
public class StatHelper {
    private Context context;

    public StatHelper(Context context) {
        this.context = context;
    }

    public enum Hardware {
        MANUFACTURER,
        MODEL,
        MEMORY_CLASS,
        LARGE_MEMORY_CLASS,
        MAX_MEMORY,
        FREE_SPACE,
        TELEPHONY,
        DEVICE,
        BRAND,
        BOARD,
        HOST,
        PRODUCT
    }

    public enum Screen {
        WIDTH,
        HEIGHT,
        DISPLAY_DENSITY,
        DRAWABLE_DENSITY,
        SCREEN_SIZE,
    }

    public enum Software {
        ANDROID_VERSION,
        SDK_INT,
        OPEN_GL_ES,
    }


    public StatItem getStatItem(Software software) {
        StatItem stat = null;
        switch (software) {
            case ANDROID_VERSION:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.android_version));
                stat.setInfo(Build.VERSION.RELEASE);
                break;
            case SDK_INT:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.sdk_int));
                stat.setInfo(String.format("%d", Build.VERSION.SDK_INT));
                break;
            case OPEN_GL_ES:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.opengl_version));
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
                if (configurationInfo != null) {
                    stat.setInfo(configurationInfo.getGlEsVersion());
                } else {
                    stat.setInfo(context.getString(R.string.unknown));
                }
                break;
        }
        return stat;
    }

    public StatItem getStatItem(Hardware hardware) {
        StatItem stat = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        switch (hardware) {
            case MODEL:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.device_model));
                stat.setInfo(Build.MODEL);
                break;

            case DEVICE:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.device));
                stat.setInfo(Build.DEVICE);
                break;

            case BRAND:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.brand));
                stat.setInfo(Build.BRAND);
                break;

            case BOARD:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.board));
                stat.setInfo(Build.BOARD);
                break;

            case HOST:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.host));
                stat.setInfo(Build.HOST);
                break;

            case MANUFACTURER:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.manufacturer));
                stat.setInfo(Build.MANUFACTURER);
                break;

            case PRODUCT:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.product));
                stat.setInfo(Build.PRODUCT);
                break;

            case MEMORY_CLASS:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.memory_class));
                int memoryClass = am.getMemoryClass();
                stat.setInfo(String.format("%d MB", memoryClass));
                break;
            case LARGE_MEMORY_CLASS:
                if (Build.VERSION.SDK_INT >= 11) {
                    stat = new StatItem();
                    stat.setTitle(context.getString(R.string.large_memory_class));
                    int largeMemoryClass = am.getLargeMemoryClass();
                    stat.setInfo(String.format("%d MB", largeMemoryClass));
                }
                break;
            case MAX_MEMORY:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.max_memory));
                Runtime rt = Runtime.getRuntime();
                long maxMemory = rt.maxMemory();
                stat.setInfo(readableFileSize(maxMemory));
                break;
            case FREE_SPACE:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.free_space));
                long available;
                if (Build.VERSION.SDK_INT >= 9) {
                    available = Environment.getExternalStorageDirectory().getFreeSpace();
                } else {
                    StatFs filesystemstats = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
                    filesystemstats.restat(Environment.getExternalStorageDirectory().getAbsolutePath());
                    available = ((long) filesystemstats.getAvailableBlocks() * (long) filesystemstats.getBlockSize());
                }
                stat.setInfo(readableFileSize(available));
                break;
            case TELEPHONY:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.telephony));
                if (isTelephonyEnabled()) {
                    stat.setInfo(context.getString(R.string.enabled));
                } else {
                    stat.setInfo(context.getString(R.string.disabled));
                }
                break;

        }
        return stat;
    }

    public StatItem getStatItem(Screen screen) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        StatItem stat = null;
        switch (screen) {
            case WIDTH:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.screen_width));
                stat.setInfo(String.format("%d px", metrics.widthPixels));
                break;
            case HEIGHT:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.screen_height));
                stat.setInfo(String.format("%d px", metrics.heightPixels));
                break;
            case DISPLAY_DENSITY:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.display_density));
                stat.setInfo(String.format("%d dpi", metrics.densityDpi));
                break;
            case DRAWABLE_DENSITY:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.drawable_density));
                if (metrics.densityDpi == DisplayMetrics.DENSITY_LOW) {
                    stat.setInfo("ldpi (.75x)");
                } else if (metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
                    stat.setInfo("mdpi (1x)");
                } else if (metrics.densityDpi == DisplayMetrics.DENSITY_TV) {
                    stat.setInfo("tvdpi (1.33x");
                } else if (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
                    stat.setInfo("hdpi (1.5x)");
                } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                    stat.setInfo("xhdpi (2x)");
                } else if (metrics.densityDpi == DisplayMetrics.DENSITY_400) {
                    stat.setInfo("xxhdpi (System scaled down to suit)");
                } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XXHIGH) {
                    stat.setInfo("xxhdpi (3x)");
                } else if (metrics.densityDpi == DisplayMetrics.DENSITY_560) {
                    stat.setInfo("xxxhdpi (System scaled down to suit)");
                } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XXXHIGH) {
                    stat.setInfo("xxxhdpi (4x)");
                }
                break;
            case SCREEN_SIZE:
                stat = new StatItem();
                stat.setTitle(context.getString(R.string.screen_size));
                int screenSize = context.getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK;

                switch (screenSize) {
                    case Configuration.SCREENLAYOUT_SIZE_LARGE:
                        stat.setInfo(context.getString(R.string.screen_size_large));
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                        stat.setInfo(context.getString(R.string.screen_size_normal));
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_SMALL:
                        stat.setInfo(context.getString(R.string.screen_size_small));
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                        stat.setInfo(context.getString(R.string.screen_size_xlarge));
                        break;
                    default:
                        stat.setInfo(context.getString(R.string.screen_size_undefined));
                }
                break;
        }
        return stat;
    }

    public ArrayList<StatItem> getFeatureList() {
        ArrayList<StatItem> featureList = new ArrayList<>();
        for (FeatureInfo featureInfo : context.getPackageManager().getSystemAvailableFeatures()) {
            StatItem stat = new StatItem();
            if (featureInfo.name != null) {
                String[] featureParts = featureInfo.name.toLowerCase().split("[.]");
                String featureName = featureParts[featureParts.length -1].replaceAll("_", " ");
                stat.setTitle(featureName.substring(0, 1).toUpperCase() + featureName.substring(1));
                stat.setInfo(featureInfo.name);
            } else {
                stat.setTitle(context.getString(R.string.opengl_version));
                stat.setInfo(featureInfo.getGlEsVersion());
            }
            featureList.add(stat);
        }
        return featureList;
    }

    private static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private boolean isTelephonyEnabled() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY);
    }
}
