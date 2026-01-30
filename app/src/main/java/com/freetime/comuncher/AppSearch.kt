package com.freetime.comuncher

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppSearch(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val packageManager = context.packageManager

    val apps = remember {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        packageManager.queryIntentActivities(intent, 0)
    }

    val filteredApps = remember(searchQuery, apps) {
        if (searchQuery.isEmpty()) {
            apps
        } else {
            apps.filter {
                it.loadLabel(packageManager).toString().contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Apps") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            items(filteredApps) { app ->
                AppRow(app = app)
            }
        }
    }
}

@Composable
fun AppRow(app: ResolveInfo) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val appName = app.loadLabel(packageManager).toString()
    val appIcon = app.loadIcon(packageManager)
    val launchIntent = packageManager.getLaunchIntentForPackage(app.activityInfo.packageName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (launchIntent != null) context.startActivity(launchIntent) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = appIcon),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = appName,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
