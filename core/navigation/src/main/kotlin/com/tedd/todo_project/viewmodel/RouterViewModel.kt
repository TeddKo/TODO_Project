package com.tedd.todo_project.viewmodel

import androidx.lifecycle.ViewModel
import com.tedd.todo_project.navigator.NavigationChannelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RouterViewModel @Inject constructor(
    navigator: NavigationChannelProvider
) : ViewModel() {
    val intent = navigator
        .channel
        .receiveAsFlow()
}
