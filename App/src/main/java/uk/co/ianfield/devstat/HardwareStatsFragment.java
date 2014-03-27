package uk.co.ianfield.devstat;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.ianfield.devstat.model.StatItem;
import uk.co.ianfield.devstat.widget.StatItemArrayAdapter;

/**
 * Created by ianfield on 20/02/2014.
 */
@EFragment (R.layout.stats_list)
public class HardwareStatsFragment extends Fragment {
    @ViewById
    ListView statsList;

    ArrayList<StatItem> stats = new ArrayList<StatItem>();

    @AfterViews
    void initHardwareStats() {
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        StatItem stat;

        stat = new StatItem();
        stat.setTitle(getString(R.string.manufacturer));
        stat.setInfo(Build.MANUFACTURER);
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.device_model));
        stat.setInfo(Build.MODEL);
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.memory_class));
        int memoryClass = am.getMemoryClass();
        stat.setInfo(String.format("%d mb", memoryClass));
        stats.add(stat);

        if(Build.VERSION.SDK_INT >= 11) {
            stat = new StatItem();
            stat.setTitle(getString(R.string.large_memory_class));
            int largeMemoryClass = am.getLargeMemoryClass();
            stat.setInfo(String.format("%d mb", largeMemoryClass));
            stats.add(stat);
        }

        stat = new StatItem();
        stat.setTitle(getString(R.string.max_memory));
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        stat.setInfo(String.format("%d bytes", maxMemory));
        stats.add(stat);


        stat = new StatItem();
        stat.setTitle(getString(R.string.free_space));
        long available;
        if (Build.VERSION.SDK_INT >= 9) {
            available = Environment.getExternalStorageDirectory().getFreeSpace();
        }
        else {
            StatFs filesystemstats = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            filesystemstats.restat(Environment.getExternalStorageDirectory().getAbsolutePath());
            available = ((long) filesystemstats.getAvailableBlocks() * (long) filesystemstats.getBlockSize());
        }
        stat.setInfo(String.format("%d bytes", available));
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.vibrator));
        if(Build.VERSION.SDK_INT >= 11) {
            if(((Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE)).hasVibrator()) {
                stat.setInfo(getString(R.string.exists));
            }
            else {
                stat.setInfo(getString(R.string.none));
            }
        }
        else {
            stat.setInfo(getString(R.string.unknown));
        }
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.telephony));
        if (isTelephonyEnabled(getActivity())) {
            stat.setInfo(getString(R.string.enabled));
        }
        else {
            stat.setInfo(getString(R.string.disabled));
        }
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.auto_focus));
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)){
            stat.setInfo(getString(R.string.available));
        }
        else {
            stat.setInfo(getString(R.string.unavailable));
        }
        stats.add(stat);

        statsList.setAdapter(new StatItemArrayAdapter(getActivity(), R.layout.stat_item, stats));
    }


    public static boolean isTelephonyEnabled(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY);
    }
}
