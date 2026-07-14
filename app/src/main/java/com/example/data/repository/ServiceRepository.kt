package com.example.data.repository

import com.example.data.local.ServiceDao
import com.example.data.model.ServiceListing
import kotlinx.coroutines.flow.Flow

class ServiceRepository(private val serviceDao: ServiceDao) {
    val allServices: Flow<List<ServiceListing>> = serviceDao.getAllServices()
    val bookmarkedServices: Flow<List<ServiceListing>> = serviceDao.getBookmarkedServices()

    fun getServiceById(id: Int): Flow<ServiceListing?> = serviceDao.getServiceById(id)

    suspend fun insert(service: ServiceListing): Long = serviceDao.insertService(service)

    suspend fun update(service: ServiceListing) = serviceDao.updateService(service)

    suspend fun delete(service: ServiceListing) = serviceDao.deleteService(service)

    suspend fun prepopulateIfEmpty() {
        if (serviceDao.getServiceCount() == 0) {
            val sampleListings = listOf(
                ServiceListing(
                    title = "Expert Plumbing Repairs",
                    category = "Plumbing",
                    description = "Fast, high-quality, and reliable plumbing repairs. We fix all types of leaks, blocked drains, pipeline bursts, bathroom fittings, and kitchen installations. Experienced technicians available 24/7 in Raddoluwa.",
                    location = "Raddoluwa",
                    providerName = "John Doe",
                    phoneNumber = "+94771234567",
                    whatsappNumber = "94771234567",
                    views = 157,
                    rating = 5.0,
                    reviewCount = 1,
                    dateAdded = "Jul 08, 2026",
                    isBookmarked = false,
                    isCustom = false
                ),
                ServiceListing(
                    title = "Home Electrical Wiring",
                    category = "Electrical",
                    description = "Professional electrical troubleshooting, wiring modifications, fan installation, lighting fixture setups, and fuse box maintenance. Certified electrician with 10+ years of experience in Seeduwa area.",
                    location = "Seeduwa",
                    providerName = "John Doe",
                    phoneNumber = "+94771234567",
                    whatsappNumber = "94771234567",
                    views = 210,
                    rating = 4.8,
                    reviewCount = 3,
                    dateAdded = "Jul 08, 2026",
                    isBookmarked = false,
                    isCustom = false
                ),
                ServiceListing(
                    title = "Garden Landscaping & Design",
                    category = "Gardening",
                    description = "Beautify your outdoor spaces with our complete landscaping services! We offer Malaysian grass laying, paving, custom flower borders, trimming, pruning, and weekly lawn maintenance. Free site inspection.",
                    location = "Raddoluwa",
                    providerName = "John Doe",
                    phoneNumber = "+94771234567",
                    whatsappNumber = "94771234567",
                    views = 184,
                    rating = 5.0,
                    reviewCount = 2,
                    dateAdded = "Jul 08, 2026",
                    isBookmarked = false,
                    isCustom = false
                ),
                ServiceListing(
                    title = "Professional AC Repair & Cleaning",
                    category = "AC Repair",
                    description = "Keep cool this summer! Prompt and affordable split unit servicing, cooling diagnostics, gas topping, and replacement of old copper tubes. Fast door-to-door service in Katunayake.",
                    location = "Katunayake",
                    providerName = "Kumara Perera",
                    phoneNumber = "+94711223344",
                    whatsappNumber = "94711223344",
                    views = 98,
                    rating = 4.9,
                    reviewCount = 4,
                    dateAdded = "Jul 10, 2026",
                    isBookmarked = false,
                    isCustom = false
                ),
                ServiceListing(
                    title = "Glamour Bridal Beauty Salon",
                    category = "Beauty",
                    description = "Enhance your natural beauty! Specializing in bridal dressing, premium facials, haircuts, custom hair styling, and high-definition makeups for corporate events and family functions. Custom packages available.",
                    location = "Ja-Ela",
                    providerName = "Nisha Silva",
                    phoneNumber = "+94722334455",
                    whatsappNumber = "94722334455",
                    views = 135,
                    rating = 4.7,
                    reviewCount = 5,
                    dateAdded = "Jul 11, 2026",
                    isBookmarked = false,
                    isCustom = false
                ),
                ServiceListing(
                    title = "Custom Carpentry & Woodworks",
                    category = "Carpentry",
                    description = "We fabricate premium teak and mahogany furniture! From custom pantry cupboard systems, wooden doors, and window frames, to high-end furniture repairs. Satisfaction guaranteed.",
                    location = "Kandana",
                    providerName = "Sunil Fernando",
                    phoneNumber = "+94755443322",
                    whatsappNumber = "94755443322",
                    views = 112,
                    rating = 4.6,
                    reviewCount = 2,
                    dateAdded = "Jul 09, 2026",
                    isBookmarked = false,
                    isCustom = false
                ),
                ServiceListing(
                    title = "Spicy Bites Catering Services",
                    category = "Catering",
                    description = "Premium traditional Sri Lankan buffets, Western dishes, finger food Platters, and custom juice bars for weddings, corporate gatherings, and household celebrations. Clean and professional culinary team.",
                    location = "Negombo",
                    providerName = "Nimal Peiris",
                    phoneNumber = "+94766554433",
                    whatsappNumber = "94766554433",
                    views = 240,
                    rating = 5.0,
                    reviewCount = 7,
                    dateAdded = "Jul 12, 2026",
                    isBookmarked = false,
                    isCustom = false
                ),
                ServiceListing(
                    title = "Deep House Cleaning Solutions",
                    category = "Cleaning",
                    description = "Squeaky clean houses! We do complete deep cleaning of kitchens, bedrooms, bathrooms, high pressure wash for yards, carpet shampooing, and post-construction commercial cleanups.",
                    location = "Seeduwa",
                    providerName = "Deep Cleaning Co.",
                    phoneNumber = "+94777112233",
                    whatsappNumber = "94777112233",
                    views = 87,
                    rating = 4.5,
                    reviewCount = 3,
                    dateAdded = "Jul 13, 2026",
                    isBookmarked = false,
                    isCustom = false
                ),
                ServiceListing(
                    title = "Sashini Photography & Studio",
                    category = "Photography",
                    description = "Cinematic wedding photography, professional outdoor family shoots, newborn shoots, and fast commercial product photography. Edited by international prize-winning digital artists.",
                    location = "Negombo",
                    providerName = "Sashini Perera",
                    phoneNumber = "+94788998899",
                    whatsappNumber = "94788998899",
                    views = 195,
                    rating = 4.9,
                    reviewCount = 8,
                    dateAdded = "Jul 07, 2026",
                    isBookmarked = false,
                    isCustom = false
                )
            )
            serviceDao.insertAll(sampleListings)

            // Prepopulate some initial reviews
            val sampleReviews = listOf(
                com.example.data.model.Review(serviceId = 1, reviewerName = "Amara Senanayake", rating = 5.0, comment = "Excellent plumbing service! Resolved a long-standing leak in our kitchen very quickly.", dateAdded = "Jul 09, 2026"),
                com.example.data.model.Review(serviceId = 2, reviewerName = "Rohan Perera", rating = 5.0, comment = "Very reliable electrician. Fixed our fuse box and fans cleanly.", dateAdded = "Jul 10, 2026"),
                com.example.data.model.Review(serviceId = 2, reviewerName = "Sanduni Jayasekara", rating = 4.0, comment = "Friendly and professional. High quality wiring job.", dateAdded = "Jul 11, 2026"),
                com.example.data.model.Review(serviceId = 4, reviewerName = "Nisansala Silva", rating = 5.0, comment = "They did an amazing job cleaning our split unit AC. Highly recommend Kumara!", dateAdded = "Jul 11, 2026"),
                com.example.data.model.Review(serviceId = 5, reviewerName = "Dilhani Cooray", rating = 5.0, comment = "Best bridal beauty care in the Ja-Ela area. Nisha is a master of makeup!", dateAdded = "Jul 12, 2026")
            )
            for (r in sampleReviews) {
                serviceDao.insertReview(r)
            }
        }
    }

    fun getReviewsForService(serviceId: Int): Flow<List<com.example.data.model.Review>> =
        serviceDao.getReviewsForService(serviceId)

    suspend fun addReview(review: com.example.data.model.Review) {
        serviceDao.insertReview(review)
        
        // Recalculate average rating and review count
        val reviews = serviceDao.getReviewsListForService(review.serviceId)
        val count = reviews.size
        val avgRating = if (count > 0) {
            reviews.map { it.rating }.average()
        } else {
            0.0
        }
        
        val service = serviceDao.getServiceByIdSync(review.serviceId)
        if (service != null) {
            val roundedRating = Math.round(avgRating * 10.0) / 10.0
            serviceDao.updateService(
                service.copy(
                    rating = roundedRating,
                    reviewCount = count
                )
            )
        }
    }

    suspend fun incrementViews(serviceId: Int) {
        val service = serviceDao.getServiceByIdSync(serviceId)
        if (service != null) {
            serviceDao.updateService(service.copy(views = service.views + 1))
        }
    }
}
