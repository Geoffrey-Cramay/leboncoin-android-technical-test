package fr.leboncoin.data.remote.album.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
