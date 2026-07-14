package com.example.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.DirectoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    viewModel: DirectoryViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Plumbing") }
    var description by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("Raddoluwa") }
    var providerName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var locationExpanded by remember { mutableStateOf(false) }

    // Validation errors state
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var providerError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    val categories = listOf(
        "Plumbing", "Electrical", "Gardening", "AC Repair", "Beauty",
        "Carpentry", "Catering", "Cleaning", "IT Support", "Medical",
        "Moving", "Painting", "Photography", "Transport", "Tutoring"
    )

    val locations = listOf(
        "Raddoluwa", "Seeduwa", "Kandana", "Katunayake", "Negombo", "Ja-Ela"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List Your Service", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("back_button")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Service Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            titleError = false
                        },
                        label = { Text("Service Title (e.g. Expert Plumbing)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_title"),
                        isError = titleError,
                        supportingText = {
                            if (titleError) Text("Title is required", color = MaterialTheme.colorScheme.error)
                        },
                        singleLine = true
                    )

                    // Category Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        ExposedDropdownMenuBox(
                            expanded = categoryExpanded,
                            onExpandedChange = { categoryExpanded = !categoryExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Category") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("input_category")
                            )
                            ExposedDropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            selectedCategory = cat
                                            categoryExpanded = false
                                        },
                                        modifier = Modifier.testTag("category_item_$cat")
                                    )
                                }
                            }
                        }
                    }

                    // Location Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        ExposedDropdownMenuBox(
                            expanded = locationExpanded,
                            onExpandedChange = { locationExpanded = !locationExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedLocation,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Location Area") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("input_location")
                            )
                            ExposedDropdownMenu(
                                expanded = locationExpanded,
                                onDismissRequest = { locationExpanded = false }
                            ) {
                                locations.forEach { loc ->
                                    DropdownMenuItem(
                                        text = { Text(loc) },
                                        onClick = {
                                            selectedLocation = loc
                                            locationExpanded = false
                                        },
                                        modifier = Modifier.testTag("location_item_$loc")
                                    )
                                }
                            }
                        }
                    }

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            descriptionError = false
                        },
                        label = { Text("Full Service Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .testTag("input_description"),
                        isError = descriptionError,
                        supportingText = {
                            if (descriptionError) Text("Description is required", color = MaterialTheme.colorScheme.error)
                        },
                        maxLines = 5
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Contact Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Provider Name
                    OutlinedTextField(
                        value = providerName,
                        onValueChange = {
                            providerName = it
                            providerError = false
                        },
                        label = { Text("Provider Name (e.g. Sunil Perera)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_provider"),
                        isError = providerError,
                        supportingText = {
                            if (providerError) Text("Provider name is required", color = MaterialTheme.colorScheme.error)
                        },
                        singleLine = true
                    )

                    // Phone Number
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            phoneError = false
                        },
                        label = { Text("Phone Number (e.g. +94771234567)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_phone"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError,
                        supportingText = {
                            if (phoneError) Text("Valid phone number is required", color = MaterialTheme.colorScheme.error)
                        },
                        singleLine = true
                    )

                    // WhatsApp Number
                    OutlinedTextField(
                        value = whatsappNumber,
                        onValueChange = { whatsappNumber = it },
                        label = { Text("WhatsApp Number (Optional)") },
                        placeholder = { Text("e.g. 94771234567") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_whatsapp"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        supportingText = {
                            Text("If empty, phone number will be used for WhatsApp.")
                        },
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("cancel_button")
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        // Validate inputs
                        if (title.isBlank()) titleError = true
                        if (description.isBlank()) descriptionError = true
                        if (providerName.isBlank()) providerError = true
                        if (phoneNumber.isBlank() || phoneNumber.length < 8) phoneError = true

                        if (!titleError && !descriptionError && !providerError && !phoneError) {
                            viewModel.addService(
                                title = title,
                                category = selectedCategory,
                                description = description,
                                location = selectedLocation,
                                providerName = providerName,
                                phoneNumber = phoneNumber,
                                whatsappNumber = whatsappNumber.ifBlank { phoneNumber }
                            )
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("submit_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Submit Listing", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
