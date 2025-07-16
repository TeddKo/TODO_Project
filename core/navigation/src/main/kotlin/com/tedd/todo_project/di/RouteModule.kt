package com.tedd.todo_project.di

import com.tedd.todo_project.navigator.NavigationChannelProvider
import com.tedd.todo_project.navigator.NavigatorImpl
import com.tedd.todo_project.navigator.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class RouteModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun provideNavigator(
        navigator: NavigatorImpl
    ): Navigator

    @Binds
    @ActivityRetainedScoped
    abstract fun provideChannelNavigator(
        navigator: NavigatorImpl
    ): NavigationChannelProvider
}