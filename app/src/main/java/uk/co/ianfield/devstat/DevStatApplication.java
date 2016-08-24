package uk.co.ianfield.devstat;

import android.app.Application;

import uk.co.ianfield.devstat.di.components.AppComponent;
import uk.co.ianfield.devstat.di.components.DaggerAppComponent;
import uk.co.ianfield.devstat.di.modules.AppModule;

/**
 * Created by Ian Field on 18/03/2016.
 */
public class DevStatApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent component() {
        return component;
    }
}
