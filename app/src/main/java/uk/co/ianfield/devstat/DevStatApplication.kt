package uk.co.ianfield.devstat

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import uk.co.ianfield.devstat.di.components.AppComponent
import uk.co.ianfield.devstat.di.components.DaggerAppComponent
import uk.co.ianfield.devstat.di.modules.AppModule

/**
 * Created by Ian Field on 18/03/2016.
 */
class DevStatApplication : Application() {

    companion object {
        const val TAG = "DevStatApplication"
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
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

                    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {}

                    override fun onActivityDestroyed(activity: Activity) {}
                })
            }
        }
    }

    fun component(): AppComponent? {
        return graph
    }

    fun getJSONFromBundle(bundle: Bundle?): String? {
        if (bundle == null || Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return null
        }
        val json = JSONObject()
        val keys = bundle.keySet()
        keys.forEach { key ->
            try {
                // json.put(key, bundle.get(key)); see edit below
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    json.put(key, JSONObject.wrap(bundle.get(key)))
                }
            } catch (e: JSONException) {
                //Handle exception here
            }

        }
        return json.toString()
    }

}
