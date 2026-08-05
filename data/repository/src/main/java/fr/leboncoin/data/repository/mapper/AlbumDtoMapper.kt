package fr.leboncoin.data.repository.mapper

import fr.leboncoin.data.remote.album.dto.AlbumDto
import fr.leboncoin.domain.model.Album

internal fun AlbumDto.toDomain() = Album(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)
