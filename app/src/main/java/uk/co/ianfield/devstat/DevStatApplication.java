package uk.co.ianfield.devstat;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import uk.co.ianfield.devstat.common.base.AndroidModule;

/**
 * Created by Ian Field on 18/03/2016.
 */
public class DevStatApplication extends Application {

    @Singleton
    @Component(modules = AndroidModule.class)
    public interface ApplicationComponent {
        void inject(MainActivity mainActivity);
    }

    @Inject StatHelper statHelper;

    private ApplicationComponent component;

    @Override public void onCreate() {
        super.onCreate();
        component = DaggerDevStatApplication_ApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
    }

    public ApplicationComponent component() {
        return component;
    }
}
