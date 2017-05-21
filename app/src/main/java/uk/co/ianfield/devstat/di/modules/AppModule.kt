package uk.co.ianfield.devstat.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import uk.co.ianfield.devstat.DevStatApplication
import uk.co.ianfield.devstat.StatHelper
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * A module for Android-specific dependencies which require a [Context] or
 * [android.app.Application] to create.
 */
@Module
class AppModule(private val application: DevStatApplication) {

    /**
     * Allow the application context to be injected but require that it be annotated with
     * [@Annotation][ApplicationContext] to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideStatHelper(): StatHelper {
        return StatHelper(application)
    }

    @Qualifier
    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ApplicationContext
}