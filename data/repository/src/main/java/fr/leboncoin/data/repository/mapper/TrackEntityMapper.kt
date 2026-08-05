package fr.leboncoin.data.repository.mapper

import fr.leboncoin.data.local.track.entity.TrackEntity
import fr.leboncoin.domain.model.Track

internal fun TrackEntity.toDomain() = Track(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
    isFavorite = isFavorite,
)
