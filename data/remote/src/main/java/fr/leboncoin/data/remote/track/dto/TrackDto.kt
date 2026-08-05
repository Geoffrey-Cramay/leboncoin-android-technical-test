package fr.leboncoin.data.remote.track.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
