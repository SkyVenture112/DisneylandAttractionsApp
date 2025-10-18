package com.example.disneylandattractions.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

// Data classes for parsing content from the Queue Times API

data class QueueTimes(
    val lands: List<Land>
)

data class Land(
    val id: Int,
    val name: String,
    val rides: List<Ride>
)

@Serializable
data class Ride(
    val id: Int,
    val name: String,
    @SerialName("is_open")
    val isOpen: Boolean,
    @SerialName("wait_time")
    val waitTime: Int,
    @SerialName("last_updated")
    val lastUpdated: String
)

