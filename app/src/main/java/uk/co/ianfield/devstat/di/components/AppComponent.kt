package uk.co.ianfield.devstat.di.components

import dagger.Component
import uk.co.ianfield.devstat.MainActivity
import uk.co.ianfield.devstat.di.modules.AppModule
import javax.inject.Singleton

/**
 * Created by Ian Field on 21/03/2016.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(activity: MainActivity)
}