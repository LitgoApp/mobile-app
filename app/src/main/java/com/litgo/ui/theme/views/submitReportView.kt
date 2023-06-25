package com.litgo.ui.theme.views

import android.widget.ImageView
import com.litgo.ui.theme.Typography

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Switch
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SubmitReportView() {

    var description by remember { mutableStateOf<String>("") }
    var toggleDanger by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Report Litter",
            style = Typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(modifier = Modifier.padding(bottom = 16.dp)) {
            Row(modifier = Modifier.padding(16.dp)) {
            }
        }

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            singleLine = false,
            maxLines = 10,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Health Hazard",
                style = Typography.labelLarge
            )

            Switch(
                checked = toggleDanger,
                onCheckedChange = { toggleDanger = it }
            )

        }

    }
}
