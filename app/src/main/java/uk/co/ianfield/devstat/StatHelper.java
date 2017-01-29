package uk.co.ianfield.devstat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.security.Provider;
import java.security.Security;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by IanField90 on 17/06/2014.
 */
public class StatHelper {
    private final Context context;

    public StatHelper(Context context) {
        this.context = context;
    }

    private static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public ArrayList<StatItem> getSoftwareList() {
        ArrayList<StatItem> softwareStats = new ArrayList<>();
        softwareStats.add(getStatItem(StatHelper.Software.ANDROID_VERSION));
        softwareStats.add(getStatItem(StatHelper.Software.SDK_INT));
        softwareStats.add(getStatItem(StatHelper.Software.OPEN_GL_ES));
        softwareStats.add(getStatItem(StatHelper.Software.GOOGLE_PLAY_SERVICES_VERSION));
        return softwareStats;
    }

    public StatItem getStatItem(Software software) {
        StatItem stat = new StatItem();
        switch (software) {
            case ANDROID_VERSION:
                stat.setTitle(context.getString(R.string.android_version));
                stat.setInfo(Build.VERSION.RELEASE);
                break;
            case SDK_INT:
                stat.setTitle(context.getString(R.string.sdk_int));
                stat.setInfo(String.format(Locale.getDefault(), "%d", Build.VERSION.SDK_INT));
                break;
            case OPEN_GL_ES:
                stat.setTitle(context.getString(R.string.opengl_version));
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
                if (configurationInfo != null) {
                    stat.setInfo(configurationInfo.getGlEsVersion());
                } else {
                    stat.setInfo(context.getString(R.string.unknown));
                }
                break;
            case GOOGLE_PLAY_SERVICES_VERSION:
                stat.setTitle(context.getString(R.string.google_play_services_version));
                try {
                    PackageInfo info = context.getPackageManager().getPackageInfo("com.google.android.gms", 0);
                    stat.setInfo(String.format("%s [%s]", info.versionName, info.versionCode));
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(StatHelper.class.getSimpleName(), "Unable to find google play services", e);
                    stat.setInfo(context.getString(R.string.unavailable));
                }
                break;

        }
        return stat;
    }

    public ArrayList<StatItem> getHardwareList() {
        ArrayList<StatItem> hardwareStats = new ArrayList<>();
        // Hardware
        hardwareStats.add(getStatItem(StatHelper.Hardware.MANUFACTURER));
        hardwareStats.add(getStatItem(StatHelper.Hardware.MODEL));
        hardwareStats.add(getStatItem(StatHelper.Hardware.DEVICE));
        hardwareStats.add(getStatItem(StatHelper.Hardware.BRAND));
        hardwareStats.add(getStatItem(StatHelper.Hardware.BOARD));
        hardwareStats.add(getStatItem(StatHelper.Hardware.HOST));
        hardwareStats.add(getStatItem(StatHelper.Hardware.PRODUCT));
        hardwareStats.add(getStatItem(StatHelper.Hardware.MEMORY_CLASS));
        if (Build.VERSION.SDK_INT >= 11) { // This is also checked for within
            hardwareStats.add(getStatItem(StatHelper.Hardware.LARGE_MEMORY_CLASS));
        }
        hardwareStats.add(getStatItem(StatHelper.Hardware.MAX_MEMORY));
        hardwareStats.add(getStatItem(StatHelper.Hardware.FREE_SPACE));
        hardwareStats.add(getStatItem(StatHelper.Hardware.TELEPHONY));
        hardwareStats.add(getStatItem(StatHelper.Hardware.SD_CARD));
        hardwareStats.add(getStatItem(StatHelper.Hardware.ARCHITECTURE));
        hardwareStats.add(getStatItem(StatHelper.Hardware.PROCESSORS));
        return hardwareStats;
    }

    public StatItem getStatItem(Hardware hardware) {
        StatItem stat = new StatItem();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        switch (hardware) {
            case MODEL:
                stat.setTitle(context.getString(R.string.device_model));
                stat.setInfo(Build.MODEL);
                break;

            case DEVICE:
                stat.setTitle(context.getString(R.string.device));
                stat.setInfo(Build.DEVICE);
                break;

            case BRAND:
                stat.setTitle(context.getString(R.string.brand));
                stat.setInfo(Build.BRAND);
                break;

            case BOARD:
                stat.setTitle(context.getString(R.string.board));
                stat.setInfo(Build.BOARD);
                break;

            case HOST:
                stat.setTitle(context.getString(R.string.host));
                stat.setInfo(Build.HOST);
                break;

            case MANUFACTURER:
                stat.setTitle(context.getString(R.string.manufacturer));
                stat.setInfo(Build.MANUFACTURER);
                break;

            case PRODUCT:
                stat.setTitle(context.getString(R.string.product));
                stat.setInfo(Build.PRODUCT);
                break;

            case MEMORY_CLASS:
                stat.setTitle(context.getString(R.string.memory_class));
                int memoryClass = am.getMemoryClass();
                stat.setInfo(String.format(Locale.getDefault(), "%d MB", memoryClass));
                break;
            case LARGE_MEMORY_CLASS:
                if (Build.VERSION.SDK_INT >= 11) {
                    stat.setTitle(context.getString(R.string.large_memory_class));
                    int largeMemoryClass = am.getLargeMemoryClass();
                    stat.setInfo(String.format(Locale.getDefault(), "%d MB", largeMemoryClass));
                } else {
                    stat = null;
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
                stat.setTitle(context.getString(R.string.telephony));
                if (isTelephonyEnabled()) {
                    stat.setInfo(context.getString(R.string.enabled));
                } else {
                    stat.setInfo(context.getString(R.string.disabled));
                }
                break;

            case SD_CARD:
                stat.setTitle(context.getString(R.string.sd_card));
                Boolean sdPresence = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    stat.setInfo(context.getString(R.string.sd_presence, String.valueOf(sdPresence)));
                } else {
                    Boolean emulated = android.os.Environment.isExternalStorageEmulated();
                    stat.setInfo(context.getString(R.string.sd_presence_emulated, String.valueOf(sdPresence), String.valueOf(emulated)));
                }
                break;
            case ARCHITECTURE:
                stat.setTitle(context.getString(R.string.architecture));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    stat.setInfo(TextUtils.join(", ", Build.SUPPORTED_ABIS));
                } else {
                    stat.setInfo(TextUtils.join(", ", Arrays.asList(Build.CPU_ABI, Build.CPU_ABI2)));
                }
                break;
            case PROCESSORS:
                stat.setTitle(context.getString(R.string.processors));
                stat.setInfo("" + Runtime.getRuntime().availableProcessors());
                break;
        }
        return stat;
    }

    public ArrayList<StatItem> getScreenList() {
        ArrayList<StatItem> screenStats = new ArrayList<>();
        screenStats.add(getStatItem(StatHelper.Screen.WIDTH));
        screenStats.add(getStatItem(StatHelper.Screen.HEIGHT));
        screenStats.add(getStatItem(StatHelper.Screen.DISPLAY_DENSITY));
        screenStats.add(getStatItem(StatHelper.Screen.DRAWABLE_DENSITY));
        screenStats.add(getStatItem(StatHelper.Screen.SCREEN_SIZE));
        return screenStats;
    }

    public StatItem getStatItem(Screen screen) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        StatItem stat = new StatItem();
        switch (screen) {
            case WIDTH:
                stat.setTitle(context.getString(R.string.screen_width));
                stat.setInfo(String.format(Locale.getDefault(), "%d px", metrics.widthPixels));
                break;
            case HEIGHT:
                stat.setTitle(context.getString(R.string.screen_height));
                stat.setInfo(String.format(Locale.getDefault(), "%d px", metrics.heightPixels));
                break;
            case DISPLAY_DENSITY:
                stat.setTitle(context.getString(R.string.display_density));
                stat.setInfo(String.format(Locale.getDefault(), "%d dpi", metrics.densityDpi));
                break;
            case DRAWABLE_DENSITY:
                stat.setTitle(context.getString(R.string.drawable_density));
                stat.setInfo(getDensityInfo(metrics));
                break;
            case SCREEN_SIZE:
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

    private String getDensityInfo(DisplayMetrics metrics) {
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi (.75x)";
            case DisplayMetrics.DENSITY_MEDIUM: // === DENSITY_DEFAULT
                return "mdpi (1x)";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi (1.33x)";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi (1.5x)";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi (2x)";
            case DisplayMetrics.DENSITY_280:
                return "xhdpi (System scaled down to suit)";
            case DisplayMetrics.DENSITY_360:
                return "xxhdpi (System scaled down to suit)";
            case DisplayMetrics.DENSITY_400:
            case DisplayMetrics.DENSITY_420:
                return "xxhdpi (System scaled down to suit)";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi (3x)";
            case DisplayMetrics.DENSITY_560:
                return "xxxhdpi (System scaled down to suit)";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi (4x)";
        }
        return "Unknown DPI: " + metrics.densityDpi;
    }

    public ArrayList<StatItem> getFeatureList() {
        ArrayList<StatItem> featureList = new ArrayList<>();
        for (FeatureInfo featureInfo : context.getPackageManager().getSystemAvailableFeatures()) {
            StatItem stat = new StatItem();
            if (featureInfo.name != null) {
                String[] featureParts = featureInfo.name.toLowerCase().split("[.]");
                String featureName = featureParts[featureParts.length - 1].replaceAll("_", " ");
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

    private boolean isTelephonyEnabled() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY);
    }

    ArrayList<StatItem> getCryptoList() {
        ArrayList<StatItem> cryptoList = new ArrayList<>();

        for (Provider provider : Security.getProviders()) {
            StatItem item = new StatItem();
            item.setTitle(provider.getName());
            String info = "";

            Set<Provider.Service> services = provider.getServices();
            for (Provider.Service service : services) {
                info += service.getAlgorithm() + "\n";
            }
            info = info.substring(0, info.length() - 2);
            item.setInfo(info);
            cryptoList.add(item);
        }

        return cryptoList;
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
        PRODUCT,
        SD_CARD,
        ARCHITECTURE,
        PROCESSORS
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
        GOOGLE_PLAY_SERVICES_VERSION
    }

}
