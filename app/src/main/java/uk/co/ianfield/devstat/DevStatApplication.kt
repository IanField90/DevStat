package uk.co.ianfield.devstat

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import org.json.JSONException
import org.json.JSONObject
/**
 * Created by Ian Field on 18/03/2016.
 */
@HiltAndroidApp
class DevStatApplication : Application() {

    companion object {
        const val TAG = "DevStatApplication"
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                    val extras = getJSONFromBundle(activity.intent.extras)
                    if (extras != null) {
                        Log.d(TAG, activity.localClassName + " created. Extras: " + extras)
                    }
                }

                override fun onActivityStarted(activity: Activity) {}

                override fun onActivityResumed(activity: Activity) {}

                override fun onActivityPaused(activity: Activity) {}

                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

                override fun onActivityDestroyed(activity: Activity) {}
            })
        }
    }

    fun getJSONFromBundle(bundle: Bundle?): String? {
        if (bundle == null) {
            return null
        }
        val json = JSONObject()
        val keys = bundle.keySet()
        keys.forEach { key ->
            try {
                json.put(key, JSONObject.wrap(bundle.getString(key)))
            } catch (e: JSONException) {
                //Handle exception here
            }

        }
        return json.toString()
    }

}
