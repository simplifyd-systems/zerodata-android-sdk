package com.simplifydvpn.android.data.model

import java.text.SimpleDateFormat
import java.util.*

data class DashboardData(
    val statusDistribution: HashMap<String, Int>?,
    val timeSeriesDistributionPerHour: List<HashMap<String, List<Distribution>>>?,
    val timeSeriesDistributionPerMinute: List<HashMap<String, List<Distribution>>>?,
    val timeSeriesPerHour: List<Distribution>?,
    val timeSeriesPerMinute: List<Distribution>?,
    val topContentCategories: HashMap<String, Int>?,
    val topCustomersByRequests: HashMap<String, Int>?,
    val topDomains: HashMap<String, Int>?,
    val topMaliciousCustomers: HashMap<String, Int>?,
    val topSecurityCategories: HashMap<String, Int>?
)

data class Distribution(
    val sum_value: Int,
    val time: String
) {
    private val dateTime: Date
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(time)
            ?: Date()

    val timeInMillis: Long
        get() = System.currentTimeMillis() - dateTime.time
}

