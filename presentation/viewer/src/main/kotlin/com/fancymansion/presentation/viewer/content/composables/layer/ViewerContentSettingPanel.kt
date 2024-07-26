package com.fancymansion.presentation.viewer.content.composables.layer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ViewerContentSettingPanel(
    modifier: Modifier
){

    Box(modifier = modifier){

        Row(modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().height(300.dp).background(Color.DarkGray)) {

        }
    }

}

