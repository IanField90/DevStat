package uk.co.ianfield.devstat;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import uk.co.ianfield.devstat.di.components.AppComponent;
import uk.co.ianfield.devstat.di.components.DaggerAppComponent;
import uk.co.ianfield.devstat.di.modules.AppModule;

/**
 * Created by Ian Field on 18/03/2016.
 */
public class DevStatApplication extends Application {
    private static final String TAG = "DevStatApplication";

    private AppComponent component;

    public static String getJSONFromBundle(Bundle bundle) {
        if (bundle == null || Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return null;
        }
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    json.put(key, JSONObject.wrap(bundle.get(key)));
                }
            } catch (JSONException e) {
                //Handle exception here
            }
        }
        return json.toString();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle bundle) {
                        String extras = getJSONFromBundle(activity.getIntent().getExtras());
                        if (extras != null) {
                            Log.d(TAG, activity.getLocalClassName() + " created. Extras: " + extras);
                        }
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {
                    }

                    @Override
                    public void onActivityResumed(Activity activity) {
                    }

                    @Override
                    public void onActivityPaused(Activity activity) {
                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                    }
                });
            }
        }
    }

    public AppComponent component() {
        return component;
    }
}
