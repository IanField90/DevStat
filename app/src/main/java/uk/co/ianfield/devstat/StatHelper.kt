@file:Suppress("DEPRECATION")

package uk.co.ianfield.devstat

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import uk.co.ianfield.devstat.model.StatItem
import java.security.Security
import java.text.DecimalFormat
import java.util.*

/**
 * Created by IanField90 on 17/06/2014.
 */
class StatHelper(private val context: Context) {

    private fun readableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    val softwareList: ArrayList<StatItem>
        get() {
            val softwareStats = ArrayList<StatItem>()
            softwareStats.add(getStatItem(StatHelper.Software.ANDROID_VERSION))
            softwareStats.add(getStatItem(StatHelper.Software.SDK_INT))
            softwareStats.add(getStatItem(StatHelper.Software.OPEN_GL_ES))
            softwareStats.add(getStatItem(StatHelper.Software.GOOGLE_PLAY_SERVICES_VERSION))
            return softwareStats
        }

    fun getStatItem(software: Software): StatItem {
        val stat = StatItem()
        when (software) {
            StatHelper.Software.ANDROID_VERSION -> {
                stat.title = context.getString(R.string.android_version)
                stat.info = Build.VERSION.RELEASE
            }
            StatHelper.Software.SDK_INT -> {
                stat.title = context.getString(R.string.sdk_int)
                stat.info = String.format(Locale.getDefault(), "%d", Build.VERSION.SDK_INT)
            }
            StatHelper.Software.OPEN_GL_ES -> {
                stat.title = context.getString(R.string.opengl_version)
                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val configurationInfo = activityManager.deviceConfigurationInfo
                if (configurationInfo != null) {
                    stat.info = configurationInfo.glEsVersion
                } else {
                    stat.info = context.getString(R.string.unknown)
                }
            }
            StatHelper.Software.GOOGLE_PLAY_SERVICES_VERSION -> {
                stat.title = context.getString(R.string.google_play_services_version)
                try {
                    val info = context.packageManager.getPackageInfo("com.google.android.gms", 0)
                    stat.info = String.format("%s [%s]", info.versionName, info.versionCode)
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e(StatHelper::class.java.simpleName, "Unable to find google play services", e)
                    stat.info = context.getString(R.string.unavailable)
                }

            }
        }
        return stat
    }

    // Hardware
    // This is also checked for within
    val hardwareList: ArrayList<StatItem>
        get() {
            val hardwareStats = ArrayList<StatItem>()
            hardwareStats.add(getStatItem(StatHelper.Hardware.MANUFACTURER)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.MODEL)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.DEVICE)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.BRAND)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.BOARD)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.HOST)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.PRODUCT)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.MEMORY_CLASS)!!)
            if (Build.VERSION.SDK_INT >= 11) {
                hardwareStats.add(getStatItem(StatHelper.Hardware.LARGE_MEMORY_CLASS)!!)
            }
            hardwareStats.add(getStatItem(StatHelper.Hardware.MAX_MEMORY)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.FREE_SPACE)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.TELEPHONY)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.SD_CARD)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.ARCHITECTURE)!!)
            hardwareStats.add(getStatItem(StatHelper.Hardware.PROCESSORS)!!)
            return hardwareStats
        }

    fun getStatItem(hardware: Hardware): StatItem? {
        var stat: StatItem? = StatItem()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        when (hardware) {
            StatHelper.Hardware.MODEL -> {
                stat!!.title = context.getString(R.string.device_model)
                stat.info = Build.MODEL
            }

            StatHelper.Hardware.DEVICE -> {
                stat!!.title = context.getString(R.string.device)
                stat.info = Build.DEVICE
            }

            StatHelper.Hardware.BRAND -> {
                stat!!.title = context.getString(R.string.brand)
                stat.info = Build.BRAND
            }

            StatHelper.Hardware.BOARD -> {
                stat!!.title = context.getString(R.string.board)
                stat.info = Build.BOARD
            }

            StatHelper.Hardware.HOST -> {
                stat!!.title = context.getString(R.string.host)
                stat.info = Build.HOST
            }

            StatHelper.Hardware.MANUFACTURER -> {
                stat!!.title = context.getString(R.string.manufacturer)
                stat.info = Build.MANUFACTURER
            }

            StatHelper.Hardware.PRODUCT -> {
                stat!!.title = context.getString(R.string.product)
                stat.info = Build.PRODUCT
            }

            StatHelper.Hardware.MEMORY_CLASS -> {
                stat!!.title = context.getString(R.string.memory_class)
                val memoryClass = am.memoryClass
                stat.info = String.format(Locale.getDefault(), "%d MB", memoryClass)
            }
            StatHelper.Hardware.LARGE_MEMORY_CLASS -> if (Build.VERSION.SDK_INT >= 11) {
                stat!!.title = context.getString(R.string.large_memory_class)
                val largeMemoryClass = am.largeMemoryClass
                stat.info = String.format(Locale.getDefault(), "%d MB", largeMemoryClass)
            } else {
                stat = null
            }
            StatHelper.Hardware.MAX_MEMORY -> {
                stat = StatItem()
                stat.title = context.getString(R.string.max_memory)
                val rt = Runtime.getRuntime()
                val maxMemory = rt.maxMemory()
                stat.info = readableFileSize(maxMemory)
            }
            StatHelper.Hardware.FREE_SPACE -> {
                stat!!.title = context.getString(R.string.free_space)
                val available: Long
                if (Build.VERSION.SDK_INT >= 9) {
                    available = Environment.getExternalStorageDirectory().freeSpace
                } else {
                    val filesystemstats = StatFs(Environment.getExternalStorageDirectory().absolutePath)
                    filesystemstats.restat(Environment.getExternalStorageDirectory().absolutePath)
                    available = filesystemstats.availableBlocks.toLong() * filesystemstats.blockSize.toLong()
                }
                stat.info = readableFileSize(available)
            }
            StatHelper.Hardware.TELEPHONY -> {
                stat!!.title = context.getString(R.string.telephony)
                if (isTelephonyEnabled) {
                    stat.info = context.getString(R.string.enabled)
                } else {
                    stat.info = context.getString(R.string.disabled)
                }
            }

            StatHelper.Hardware.SD_CARD -> {
                stat!!.title = context.getString(R.string.sd_card)
                val sdPresence = android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    stat.info = context.getString(R.string.sd_presence, sdPresence.toString())
                } else {
                    val emulated = android.os.Environment.isExternalStorageEmulated()
                    stat.info = context.getString(R.string.sd_presence_emulated, sdPresence.toString(), emulated.toString())
                }
            }
            StatHelper.Hardware.ARCHITECTURE -> {
                stat!!.title = context.getString(R.string.architecture)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    stat.info = TextUtils.join(", ", Build.SUPPORTED_ABIS)
                } else {
                    stat.info = TextUtils.join(", ", Arrays.asList(Build.CPU_ABI, Build.CPU_ABI2))
                }
            }
            StatHelper.Hardware.PROCESSORS -> {
                stat!!.title = context.getString(R.string.processors)
                stat.info = "" + Runtime.getRuntime().availableProcessors()
            }
        }
        return stat
    }

    val screenList: ArrayList<StatItem>
        get() {
            val screenStats = ArrayList<StatItem>()
            screenStats.add(getStatItem(StatHelper.Screen.WIDTH))
            screenStats.add(getStatItem(StatHelper.Screen.HEIGHT))
            screenStats.add(getStatItem(StatHelper.Screen.DISPLAY_DENSITY))
            screenStats.add(getStatItem(StatHelper.Screen.DRAWABLE_DENSITY))
            screenStats.add(getStatItem(StatHelper.Screen.SCREEN_SIZE))
            return screenStats
        }

    fun getStatItem(screen: Screen): StatItem {
        val metrics = context.resources.displayMetrics

        val stat = StatItem()
        when (screen) {
            StatHelper.Screen.WIDTH -> {
                stat.title = context.getString(R.string.screen_width)
                stat.info = String.format(Locale.getDefault(), "%d px", metrics.widthPixels)
            }
            StatHelper.Screen.HEIGHT -> {
                stat.title = context.getString(R.string.screen_height)
                stat.info = String.format(Locale.getDefault(), "%d px", metrics.heightPixels)
            }
            StatHelper.Screen.DISPLAY_DENSITY -> {
                stat.title = context.getString(R.string.display_density)
                stat.info = String.format(Locale.getDefault(), "%d dpi", metrics.densityDpi)
            }
            StatHelper.Screen.DRAWABLE_DENSITY -> {
                stat.title = context.getString(R.string.drawable_density)
                stat.info = getDensityInfo(metrics)
            }
            StatHelper.Screen.SCREEN_SIZE -> {
                stat.title = context.getString(R.string.screen_size)
                val screenSize = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

                when (screenSize) {
                    Configuration.SCREENLAYOUT_SIZE_LARGE -> stat.info = context.getString(R.string.screen_size_large)
                    Configuration.SCREENLAYOUT_SIZE_NORMAL -> stat.info = context.getString(R.string.screen_size_normal)
                    Configuration.SCREENLAYOUT_SIZE_SMALL -> stat.info = context.getString(R.string.screen_size_small)
                    Configuration.SCREENLAYOUT_SIZE_XLARGE -> stat.info = context.getString(R.string.screen_size_xlarge)
                    else -> stat.info = context.getString(R.string.screen_size_undefined)
                }
            }
        }
        return stat
    }

    private fun getDensityInfo(metrics: DisplayMetrics): String {
        when (metrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> return "ldpi (.75x)"
            DisplayMetrics.DENSITY_MEDIUM // === DENSITY_DEFAULT
            -> return "mdpi (1x)"
            DisplayMetrics.DENSITY_TV -> return "tvdpi (1.33x)"
            DisplayMetrics.DENSITY_HIGH -> return "hdpi (1.5x)"
            DisplayMetrics.DENSITY_XHIGH -> return "xhdpi (2x)"
            DisplayMetrics.DENSITY_280 -> return "xhdpi (System scaled down to suit)"
            DisplayMetrics.DENSITY_360 -> return "xxhdpi (System scaled down to suit)"
            DisplayMetrics.DENSITY_400, DisplayMetrics.DENSITY_420 -> return "xxhdpi (System scaled down to suit)"
            DisplayMetrics.DENSITY_XXHIGH -> return "xxhdpi (3x)"
            DisplayMetrics.DENSITY_560 -> return "xxxhdpi (System scaled down to suit)"
            DisplayMetrics.DENSITY_XXXHIGH -> return "xxxhdpi (4x)"
        }
        return "Unknown DPI: " + metrics.densityDpi
    }

    val featureList: ArrayList<StatItem>
        get() {
            val featureList = ArrayList<StatItem>()
            for (featureInfo in context.packageManager.systemAvailableFeatures) {
                val stat = StatItem()
                if (featureInfo.name != null) {
                    val featureParts = featureInfo.name.toLowerCase().split("[.]".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    val featureName = featureParts[featureParts.size - 1].replace("_".toRegex(), " ")
                    stat.title = featureName.substring(0, 1).toUpperCase() + featureName.substring(1)
                    stat.info = featureInfo.name
                } else {
                    stat.title = context.getString(R.string.opengl_version)
                    stat.info = featureInfo.glEsVersion
                }
                featureList.add(stat)
            }
            return featureList
        }

    private val isTelephonyEnabled: Boolean
        get() {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.simState == TelephonyManager.SIM_STATE_READY
        }

    internal val cryptoList: ArrayList<StatItem>
        get() {
            val cryptoList = ArrayList<StatItem>()

            for (provider in Security.getProviders()) {
                val item = StatItem()
                item.title = provider.name
                var info = ""

                val services = provider.services
                for (service in services) {
                    info += service.algorithm + "\n"
                }
                info = info.substring(0, info.length - 2)
                item.info = info
                cryptoList.add(item)
            }

            return cryptoList
        }

    enum class Hardware {
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

    enum class Screen {
        WIDTH,
        HEIGHT,
        DISPLAY_DENSITY,
        DRAWABLE_DENSITY,
        SCREEN_SIZE
    }

    enum class Software {
        ANDROID_VERSION,
        SDK_INT,
        OPEN_GL_ES,
        GOOGLE_PLAY_SERVICES_VERSION
    }

}
