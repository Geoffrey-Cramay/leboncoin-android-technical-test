package fr.leboncoin.data.repository.mapper

import fr.leboncoin.data.local.album.entity.AlbumEntity
import fr.leboncoin.domain.model.Album

internal fun AlbumEntity.toDomain() = Album(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)
