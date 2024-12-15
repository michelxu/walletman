package com.xmichxl.walletmanapp.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.xmichxl.walletmanapp.core.utils.filterDecimals

@Composable
fun MainTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isErrorMsg: Boolean = false,
    isEnabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = isErrorMsg,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)

    )
}

@Composable
fun WarningFormText(title: String = "Complete this field"){
    Row(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically // Align icon and text vertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Warning",
            tint = Color.Red,
            modifier = Modifier.size(16.dp) // Adjust the size of the icon
        )
        Spacer(modifier = Modifier.width(4.dp)) // Add spacing between icon and text
        Text(
            text = title,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
fun NumericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    allowDecimals: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    isErrorMsg: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            val sanitized = if (allowDecimals) {
                filterDecimals(it)
            } else {
                it.filter { char -> char.isDigit() } // Only digits
            }

            // Remove leading zeros unless the input is just "0"
            val withoutLeadingZeros = if (sanitized.startsWith("0") && sanitized.length > 1) {
                sanitized.trimStart('0')
            } else {
                sanitized
            }

            // Apply maxLength restriction
            if (withoutLeadingZeros.length <= maxLength) {
                onValueChange(withoutLeadingZeros)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = if (allowDecimals) KeyboardType.Number else KeyboardType.NumberPassword),
        singleLine = true,
        isError = isErrorMsg,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)
    )
}


@Composable
fun DropdownTextField(
    value: String,
    onValueChange: (String) -> Unit,
    list: List<String>,
    label: String,
    modifier: Modifier = Modifier,
    isErrorMsg: Boolean = false
) {
    // Declaring a boolean value to store the expanded state of the Text Field
    var mExpanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column(
        Modifier
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)) {
        // Overlay a clickable layer on the TextField
        Box {
            // Create an Outlined Text Field with icon and not expanded
            OutlinedTextField(
                value = value,
                onValueChange = {}, // Disable direct input editing
                label = { Text(label) },
                readOnly = true, // Ensure the field is not editable directly
                isError = isErrorMsg,
                trailingIcon = {
                    Icon(
                        icon,
                        "Toggle Dropdown",
                        Modifier.clickable { mExpanded = !mExpanded }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to the DropDown the same width
                        mTextFieldSize = coordinates.size.toSize()
                    }
            )
            // Make the entire Box clickable
            Box(
                Modifier
                    .matchParentSize() // Covers the entire OutlinedTextField
                    .clickable { mExpanded = !mExpanded }
            )
        }

        // Create a drop-down menu
        // when clicked, set the Text Field text as the item selected
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onValueChange(item)
                        mExpanded = false
                    })
            }
        }
    }
}


@Composable
fun ColorPickerDropdown(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    colors: List<Pair<String, Color>>,
    label: String,
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column(
        Modifier
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)) {
        // Overlay a clickable layer on the TextField
        Box {
            // Outlined TextField for the dropdown trigger
            OutlinedTextField(
                value = selectedColor,
                onValueChange = {},
                label = { Text(label) },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    }
                    .clickable { expanded = !expanded }
            )
            // Make the entire Box clickable
            Box(
                Modifier
                    .matchParentSize() // Covers the entire OutlinedTextField
                    .clickable { expanded = !expanded }
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            colors.forEach { (name, color) ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Box to display the color
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(color)
                                //.border(1.dp, Color.Black) // Optional: Border for better visibility
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Space between box and text
                            Text(text = name)
                        }
                    },
                    onClick = {
                        onColorSelected(name)
                        expanded = false
                    }
                )
            }
        }
    }
}