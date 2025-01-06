package com.xmichxl.walletmanapp.core.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.xmichxl.walletmanapp.R
import com.xmichxl.walletmanapp.core.utils.AppConstants
import com.xmichxl.walletmanapp.core.utils.AppIcons
import com.xmichxl.walletmanapp.core.utils.filterDecimals
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.category.data.Category
import java.util.Calendar

@Composable
fun MainTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isErrorMsg: Boolean = false,
    isEnabled: Boolean = true,
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
fun WarningFormText(title: String = "Complete this field") {
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
    isErrorMsg: Boolean = false,
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
    isErrorMsg: Boolean = false,
) {
    // Declaring a boolean value to store the expanded state of the Text Field
    var mExpanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column(
        Modifier
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)
    ) {
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
fun NameIdDropdownTextField(
    value: String,
    onValueChange: (String, Int?) -> Unit, // Add a way to return the ID
    list: List<Pair<String, Int>>, // List of names and their IDs
    label: String,
    modifier: Modifier = Modifier,
    isErrorMsg: Boolean = false,
) {
    // Declaring a boolean value to store the expanded state of the Text Field
    var mExpanded by remember { mutableStateOf(false) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column(
        Modifier
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)
    ) {
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
            list.forEach { (name, id) ->
                DropdownMenuItem(
                    text = { Text(text = name) },
                    onClick = {
                        onValueChange(name, id)
                        mExpanded = false
                    })
            }
        }
    }
}


@Composable
fun DatePickerTextField(
    label: String,
    date: String,
    onValueChange: (String) -> Unit,
    isErrorMsg: Boolean = false,
) {
    val context = LocalContext.current

    // State to control the dialogs
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Temporary state to hold the selected date and time parts
    var selectedYear by remember { mutableStateOf(0) }
    var selectedMonth by remember { mutableStateOf(0) }
    var selectedDay by remember { mutableStateOf(0) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }


    // Open the DatePickerDialog
    if (showDatePicker) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month
                selectedDay = dayOfMonth
                showDatePicker = false
                showTimePicker = true // Open time picker after date is selected
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnCancelListener { showDatePicker = false }
            show()
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
                showTimePicker = false
                // Combine date and time into ISO format
                val formattedDateTime = String.format(
                    "%04d-%02d-%02d %02d:%02d", // ISO format: yyyy-MM-dd HH:mm
                    selectedYear,
                    selectedMonth + 1, // Months are 0-based in Calendar
                    selectedDay,
                    selectedHour,
                    selectedMinute
                )
                onValueChange(formattedDateTime)
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            true // Use 24-hour format
        ).apply {
            setOnCancelListener { showTimePicker = false }
            show()
        }
    }

    // TextField to display the selected date
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 15.dp)
            .clickable { showDatePicker = true }
    ) {
        OutlinedTextField(
            value = date, // Show placeholder if empty
            onValueChange = {}, // Prevent manual input
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text("Select a date") }, // Optional
            isError = isErrorMsg,
            modifier = Modifier
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize() // Ensures the Box covers the entire TextField
                .clickable { showDatePicker = true }
        )
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
            .padding(bottom = 15.dp)
    ) {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipWithSubItems(
    chipLabel: String,
    chipItems: List<String>,
    selectedValue: String,
    onValueSelected: (String) -> Unit
) {
    var showSubList by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = showSubList,
        onExpandedChange = { showSubList = !showSubList }
    ) {
        FilterChip(
            modifier = Modifier.menuAnchor(),
            selected = selectedValue.isNotEmpty(),
            onClick = { showSubList = !showSubList },
            label = { Text(text = selectedValue.ifEmpty { chipLabel }) },
            trailingIcon = {
                Icon(
                    modifier = Modifier.rotate(if (showSubList) 180f else 0f),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "List"
                )
            }
        )
        ExposedDropdownMenu(
            expanded = showSubList,
            onDismissRequest = { showSubList = false }
        ) {
            chipItems.forEach { subListItem ->
                DropdownMenuItem(
                    text = { Text(subListItem) },
                    onClick = {
                        onValueSelected(subListItem)
                        showSubList = false
                    }
                )
            }
        }
    }
}




@Composable
fun FilterRow(
    selectedDateRange: MutableState<String>,
    selectedType: MutableState<String>,
    selectedCategory: MutableState<String>,
    selectedAccount: MutableState<String>,
    categoryList: List<Category>,
    accountList: List<Account>,
    onFilterApplied: (Map<String, String>) -> Unit
) {
    val filters = remember { mutableStateMapOf<String, String>() }
    val dateRangesMap = AppConstants.dateRangesFilter
    val transactionTypesMap = AppConstants.transactionTypesFilter
    val categoryMap = categoryList.associate { it.id to it.name } + ("All" to "All")
    val accountMap = accountList.associate { it.id to it.name } + ("All" to "All")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // LazyRow for dropdowns, but with a fixed width
        LazyRow(
            modifier = Modifier.weight(1f) // Ensures it fills available space for the dropdowns
        ) {
            item {
                FilterDropdown(
                    label = "Date Range",
                    selectedValue = selectedDateRange.value,
                    options = dateRangesMap,
                    onValueSelected = { id ->
                        selectedDateRange.value = dateRangesMap[id] ?: ""
                        filters["dateRange"] = id
                        onFilterApplied(filters)
                    },
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .width(180.dp) // Set fixed width for dropdown
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            item {
                FilterDropdown(
                    label = "Type",
                    selectedValue = selectedType.value,
                    options = transactionTypesMap,
                    onValueSelected = { id ->
                        selectedType.value = transactionTypesMap[id] ?: "All"
                        filters["type"] = id.toString()
                        onFilterApplied(filters)
                    },
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .width(180.dp) // Set fixed width for dropdown
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            item {
                FilterDropdown(
                    label = "Category",
                    selectedValue = selectedCategory.value,
                    options = categoryMap,
                    onValueSelected = { selectedId ->
                        selectedCategory.value = categoryMap[selectedId] ?: "All"
                        filters["categoryId"] = selectedId.toString()
                        onFilterApplied(filters)
                    },
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .width(180.dp) // Set fixed width for dropdown
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            item {
                FilterDropdown(
                    label = "Account",
                    selectedValue = selectedAccount.value,
                    options = accountMap,
                    onValueSelected = { selectedId ->
                        selectedAccount.value = accountMap[selectedId] ?: "All"
                        filters["accountId"] = selectedId.toString()
                        onFilterApplied(filters)
                    },
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .width(180.dp) // Set fixed width for dropdown
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Button(
            onClick = {
                selectedDateRange.value = "All"
                filters["dateRange"] = "All"
                selectedType.value = "All"
                filters["type"] = "All"
                selectedCategory.value = "All"
                filters["categoryId"] = "All"
                selectedAccount.value = "All"
                filters["accountId"] = "All"
                onFilterApplied(filters)
            },
            modifier = Modifier.padding(start = 8.dp).width(50.dp) // Set fixed width for button
        ){ Text("R") }

        // Apply button, now aligned at the right end
        Button(
            onClick = { onFilterApplied(filters) },
            modifier = Modifier.padding(start = 8.dp).width(50.dp) // Set fixed width for button
        ) {
            Text(text = "S")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K> FilterDropdown(
    label: String,
    selectedValue: String,
    options: Map<K, String>,
    onValueSelected: (K) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedValue,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            },
            readOnly = true,
            modifier = modifier
                .menuAnchor()
                .padding(end = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (key, value) ->
                DropdownMenuItem(
                    {Text(text = value) },
                    onClick = {
                        onValueSelected(key)
                        expanded = false
                    }
                )
            }
        }
    }
}






