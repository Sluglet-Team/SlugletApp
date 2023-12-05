package com.sluglet.slugletapp.common.composables

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sluglet.slugletapp.common.ext.basicRow
import com.sluglet.slugletapp.common.ext.smallSpacer
import com.sluglet.slugletapp.model.BottomNavItem
import com.sluglet.slugletapp.model.CourseData

/*
This is where composables for box-like items go
This includes things like:
Course information composables
Calendars
etc.
 */

@Composable
fun CourseBox(
    coursedata: CourseData,
    modifier: Modifier = Modifier,
    onAddClick: ((CourseData) -> Unit)?,
    onMapClick: (((String) -> Unit, CourseData) -> Boolean)?,
    openScreen: (String) -> Unit = {}
) {
    var isExpanded by remember {mutableStateOf(false)}
    val context = LocalContext.current
    // Define a row: Left side will be the course info, right side the loc and add icons
    Row(
        modifier = modifier
            .basicRow()
            .clickable(
                onClick = { isExpanded = !isExpanded }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Organizes class data in a column structure
        Column (
            modifier = Modifier
                .smallSpacer()
                .padding(start = 5.dp)
        ){
            // Course Prefix and Number
            CourseText(
                text = coursedata.course_number,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            // Only show this data if the box is clicked
            if (isExpanded) {
                CourseText(
                    text = coursedata.course_name,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                CourseText(
                    text = coursedata.prof_name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                CourseText(
                    text = coursedata.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                CourseText(
                    text = coursedata.date_time,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

        }
        // Icons
        Row (
            modifier = Modifier
                .padding(end = 15.dp),
            Arrangement.SpaceEvenly
        ){
            Icon(
                Icons.Rounded.LocationOn,
                "map",
                modifier = Modifier
                    .clickable {
                       if (onMapClick != null) {
                           if(!onMapClick(openScreen, coursedata)) {
                               Toast.makeText(
                                   context,
                                   "This course does not have a physical location",
                                   Toast.LENGTH_LONG
                               ).show()
                           }
                       }
                    },
                tint = Color.Black
            )
            Icon(
                Icons.Rounded.Add,
                "add",
                modifier = Modifier
                    .clickable {
                        if(onAddClick != null) {
                            onAddClick(coursedata)
                        }
                    },
                tint = Color.Black
            )
        }
    }
}

@Composable
fun SearchBox (
    onSearchChange: (String) -> Unit,
    userSearch: String
) {
    Box (
        modifier = Modifier.padding(top = 15.dp, bottom = 20.dp)
    ) {
        RoundedTextField(
            icon = Icons.Rounded.Search,
            text = "Search",
            userSearch = userSearch,
            onSearchChange = onSearchChange
        )
    }
}

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar (
        modifier = Modifier
            .smallSpacer()
            .clip(shape = RoundedCornerShape(10.dp)),
        contentColor = Color.White,
        tonalElevation = 10.dp,
    ){
        items.forEach {item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Column ( horizontalAlignment = CenterHorizontally ){
                        if (selected) {
                            Icon(
                                item.selectedIcon,
                                contentDescription = item.name
                            )
                            Text(
                                text = item.name
                            )
                        }
                        else {
                            Icon(
                                item.unselectedIcon,
                                contentDescription = item.name
                            )
                        }
                    }
                }
            )
        }

    }
}

// Using "Split" at the top right of Android studio shows the preview of composables
@Preview (showBackground = true)
@Composable
fun CourseBoxPreview (
) {
    val test = CourseData(
        course_number = "CSE 115A",
        course_name = "Introduction to Software Engineering",
        prof_name = "Richard Julig",
        date_time = "MWF 8:00am - 9:05am",
        location = "Baskin Auditorium 1"
    )
    CourseBox(coursedata = test, modifier = Modifier, onAddClick = null, onMapClick = null)
}

@Preview(showBackground = true)
@Composable
fun SearchBoxPreview (

) {
    SearchBox(
        userSearch = "",
        onSearchChange = { }
    )
}