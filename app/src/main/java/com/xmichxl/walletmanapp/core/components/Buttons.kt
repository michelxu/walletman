package com.xmichxl.walletmanapp.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xmichxl.walletmanapp.core.utils.AppIcons

@Composable
fun FloatButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = AppIcons.Main.Add,
            contentDescription = "Agregar"
        )
    }
}

@Composable
fun MainIconButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
    }
}

@Composable
fun MainIconButton(icon: Int, onClick: () -> Unit, modifier: Modifier) {
    IconButton(onClick = onClick, modifier) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = Color.White)
    }
}

@Composable
fun ButtonAtBottom(
    onClick: () -> Unit,
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .padding(bottom = 30.dp)
        //.padding(16.dp) // Optional padding around the button
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Align to the bottom
        ) {
            Text(title)
        }
    }
}

@Composable
fun CircleButton(
    icon: Painter,
    enabled: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(8.dp),
        enabled = enabled,
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        Icon(painter = icon, contentDescription = "", modifier = Modifier.size(24.dp))
    }
}