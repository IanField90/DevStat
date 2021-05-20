package uk.co.ianfield.devstat.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.co.ianfield.devstat.DevStatApplication
import uk.co.ianfield.devstat.StatHelper
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideStatHelper(@ApplicationContext context: Context): StatHelper {
        return StatHelper(context)
    }

}