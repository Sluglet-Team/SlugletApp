package com.sluglet.slugletapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.impl.ClassNotification
import com.sluglet.slugletapp.screens.search.SearchScreenContent
import com.sluglet.slugletapp.ui.theme.DarkMode
import com.sluglet.slugletapp.ui.theme.LightMode
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SlugletActivity : ComponentActivity() {
    @Inject
    lateinit var classNotification: ClassNotification
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SlugletApp() }
        classNotification.createNotificationChannel()

    }
}