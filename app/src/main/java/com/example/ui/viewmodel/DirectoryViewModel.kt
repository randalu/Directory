package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ServiceListing
import com.example.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortOption(val displayName: String) {
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    WHATSAPP_FIRST("WhatsApp First"),
    PHONE_FIRST("Phone First")
}

class DirectoryViewModel(
    application: Application,
    private val repository: ServiceRepository
) : AndroidViewModel(application) {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _selectedLocation = MutableStateFlow<String?>("All")
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _sortBy = MutableStateFlow(SortOption.NAME_ASC)
    val sortBy = _sortBy.asStateFlow()

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggleDarkTheme() {
        _isDarkTheme.value = !(_isDarkTheme.value ?: false)
    }

    fun setSortBy(option: SortOption) {
        _sortBy.value = option
    }

    init {
        // Run pre-population on startup in background
        viewModelScope.launch {
            repository.prepopulateIfEmpty()
        }
    }

    val filteredServices: StateFlow<List<ServiceListing>> = combine(
        repository.allServices,
        _searchQuery,
        _selectedCategory,
        _selectedLocation,
        _sortBy
    ) { array ->
        @Suppress("UNCHECKED_CAST")
        val services = array[0] as List<ServiceListing>
        val query = array[1] as String
        val category = array[2] as String?
        val location = array[3] as String?
        val sortBy = array[4] as SortOption

        val filtered = services.filter { service ->
            val matchesQuery = query.isBlank() ||
                    service.title.contains(query, ignoreCase = true) ||
                    service.description.contains(query, ignoreCase = true) ||
                    service.providerName.contains(query, ignoreCase = true) ||
                    service.category.contains(query, ignoreCase = true)

            val matchesCategory = category.isNullOrEmpty() || category == "All" ||
                    service.category.equals(category, ignoreCase = true)

            val matchesLocation = location.isNullOrEmpty() || location == "All" ||
                    service.location.equals(location, ignoreCase = true)

            matchesQuery && matchesCategory && matchesLocation
        }

        when (sortBy) {
            SortOption.NAME_ASC -> filtered.sortedBy { it.title.lowercase() }
            SortOption.NAME_DESC -> filtered.sortedByDescending { it.title.lowercase() }
            SortOption.WHATSAPP_FIRST -> {
                filtered.sortedWith(
                    compareByDescending<ServiceListing> { it.whatsappNumber.isNotBlank() }
                        .thenBy { it.title.lowercase() }
                )
            }
            SortOption.PHONE_FIRST -> {
                filtered.sortedWith(
                    compareByDescending<ServiceListing> { it.phoneNumber.isNotBlank() }
                        .thenBy { it.title.lowercase() }
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookmarkedServices: StateFlow<List<ServiceListing>> = repository.bookmarkedServices
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category ?: "All"
    }

    fun setSelectedLocation(location: String?) {
        _selectedLocation.value = location ?: "All"
    }

    fun toggleBookmark(service: ServiceListing) {
        viewModelScope.launch {
            repository.update(service.copy(isBookmarked = !service.isBookmarked))
        }
    }

    fun addService(
        title: String,
        category: String,
        description: String,
        location: String,
        providerName: String,
        phoneNumber: String,
        whatsappNumber: String
    ) {
        viewModelScope.launch {
            val formattedWhatsapp = whatsappNumber.replace(Regex("[^0-9]"), "")
            val newService = ServiceListing(
                title = title,
                category = category,
                description = description,
                location = location,
                providerName = providerName,
                phoneNumber = phoneNumber,
                whatsappNumber = formattedWhatsapp.ifBlank { phoneNumber.replace(Regex("[^0-9]"), "") },
                views = (5..30).random(),
                rating = 5.0,
                reviewCount = 0,
                isBookmarked = false,
                isCustom = true
            )
            repository.insert(newService)
        }
    }

    fun deleteService(service: ServiceListing) {
        viewModelScope.launch {
            repository.delete(service)
        }
    }

    fun getServiceById(id: Int): StateFlow<ServiceListing?> {
        val selected = MutableStateFlow<ServiceListing?>(null)
        viewModelScope.launch {
            repository.getServiceById(id).collect {
                selected.value = it
            }
        }
        return selected.asStateFlow()
    }

    fun getReviewsForService(serviceId: Int) = repository.getReviewsForService(serviceId)

    fun addReview(serviceId: Int, reviewerName: String, rating: Double, comment: String) {
        viewModelScope.launch {
            val formatter = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
            val dateStr = formatter.format(java.util.Date())
            val review = com.example.data.model.Review(
                serviceId = serviceId,
                reviewerName = reviewerName.ifBlank { "Anonymous" },
                rating = rating,
                comment = comment,
                dateAdded = dateStr
            )
            repository.addReview(review)
        }
    }

    fun incrementViews(serviceId: Int) {
        viewModelScope.launch {
            repository.incrementViews(serviceId)
        }
    }

    // Factory pattern to simplify injection
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val database = AppDatabase.getDatabase(application)
            val repository = ServiceRepository(database.serviceDao())
            return DirectoryViewModel(application, repository) as T
        }
    }
}
