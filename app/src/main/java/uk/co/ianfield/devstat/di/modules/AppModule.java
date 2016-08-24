package uk.co.ianfield.devstat.di.modules;

import android.content.Context;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.ianfield.devstat.DevStatApplication;
import uk.co.ianfield.devstat.StatHelper;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module
public class AppModule {
    private final DevStatApplication application;

    public AppModule(DevStatApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ApplicationContext @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    StatHelper provideStatHelper() {
        return new StatHelper(application);
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplicationContext {
    }
}