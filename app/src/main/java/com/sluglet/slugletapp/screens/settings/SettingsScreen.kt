package com.sluglet.slugletapp.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource
import com.sluglet.slugletapp.HOME_SCREEN
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.common.composables.CourseBox

/*
A composable that renders the Search Screen
Uses a CourseBox composable along with a SearchTextField
 */
@Composable
fun SettingsScreen (
    openScreen: (String) -> Unit,
     viewModel: SettingsViewModel = hiltViewModel() //FIXME(CAMDEN): This line breaks the app
) {

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        // modifier = modifier.padding(8.dp)
    ) {
        items(100) {
            SwitchWithIconExample()
            SwitchWithCustomColors()
            Text(
                text = "setting "
            )
        }

    }
    SettingsScreenContent(
        onReturnClick = viewModel::onReturnClick,
        openScreen = openScreen

    )
}
@Composable
fun SettingsScreenContent (
    modifier: Modifier = Modifier,
    onReturnClick: (((String) -> Unit) -> Unit)?,
    openScreen: (String) -> Unit = {}
    // FIXME: the following two take the wrong arguments
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ReturnButton(
            modifier = Modifier.align(Alignment.TopStart),
            onReturnClick = onReturnClick,
            openScreen = openScreen
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(8.dp)
        ) {
            Text(
                text = "happy birthday"
            )
            Text(
                text = "from emma"
            )
        }

    }
}
@Preview
@Composable
fun SettingPreview (
) {
    val testList = mutableListOf<CourseData>()
    val testCourse = CourseData (
        courseNum = "CSE 115A",
        courseName = "Intro to Software Engineering",
        location = "Basking Auditorium 1",
        dateTime = "MWF 8:00am-9:00am",
        profName = "Julig"
    )
    for (i in 1..10) {
        testList.add(testCourse)
    }
    CourseBox(coursedata = testCourse, onAddClick = null)
    SlugletAppTheme {
        SettingsScreenContent(
            courses = testList,
            userSearch = ""
        )
    }
}
@Composable
fun SwitchWithIconExample() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        thumbContent = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        }
    )
}
@Composable
fun SwitchWithCustomColors() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.inverseOnSurface,
            checkedTrackColor = MaterialTheme.colorScheme.secondary,
            uncheckedThumbColor = Color.Gray,
            uncheckedTrackColor = MaterialTheme.colorScheme.inverseOnSurface,
        )
    )
}



@Composable
fun ThemeSwitchSetting(
    themeSwitchState: (Boolean) -> Unit,
    isDarkTheme: Boolean
) {
    val switchState = remember { mutableStateOf(isDarkTheme) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.theme_switch_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Switch(
            checked = switchState.value,
            onCheckedChange = { checked ->
                switchState.value = checked
                themeSwitchState(checked)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun ReturnButton(
    modifier: Modifier = Modifier,
    onReturnClick: (((String) -> Unit) -> Unit)?,
    openScreen: (String) -> Unit = {}
) {
    Icon(
        Icons.Filled.ArrowBack,
        contentDescription = "home",
        modifier = Modifier
            .clickable {
                if (onReturnClick != null) {
                    onReturnClick(openScreen)
                }
            }
    )
}
