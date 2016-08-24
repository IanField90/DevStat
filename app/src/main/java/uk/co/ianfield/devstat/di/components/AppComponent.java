package uk.co.ianfield.devstat.di.components;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.ianfield.devstat.MainActivity;
import uk.co.ianfield.devstat.di.modules.AppModule;

/**
 * Created by Ian Field on 21/03/2016.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
}