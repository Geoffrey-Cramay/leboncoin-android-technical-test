package fr.leboncoin.data.repository.mapper

import fr.leboncoin.data.local.track.entity.TrackEntity
import fr.leboncoin.data.remote.track.dto.TrackDto

internal fun TrackDto.toEntity() = TrackEntity(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)
