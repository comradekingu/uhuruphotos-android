/*
Copyright 2022 Savvas Dalkitsis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.savvasdalkitsis.uhuruphotos.albums.usecase

import com.savvasdalkitsis.uhuruphotos.albums.api.model.Album
import com.savvasdalkitsis.uhuruphotos.albums.api.usecase.AlbumsUseCase
import com.savvasdalkitsis.uhuruphotos.albums.repository.AlbumsRepository
import com.savvasdalkitsis.uhuruphotos.albums.worker.AlbumWorkScheduler
import com.savvasdalkitsis.uhuruphotos.db.albums.GetAlbums
import com.savvasdalkitsis.uhuruphotos.db.albums.GetPersonAlbums
import com.savvasdalkitsis.uhuruphotos.infrastructure.date.DateDisplayer
import com.savvasdalkitsis.uhuruphotos.infrastructure.extensions.Group
import com.savvasdalkitsis.uhuruphotos.infrastructure.extensions.mapValues
import com.savvasdalkitsis.uhuruphotos.infrastructure.extensions.safelyOnStartIgnoring
import com.savvasdalkitsis.uhuruphotos.photos.service.model.isVideo
import com.savvasdalkitsis.uhuruphotos.photos.api.model.Photo
import com.savvasdalkitsis.uhuruphotos.photos.usecase.PhotosUseCase
import com.savvasdalkitsis.uhuruphotos.photos.usecase.PhotosUseCase.Companion.FAVOURITES_RATING_THRESHOLD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AlbumsUseCase @Inject constructor(
    private val albumsRepository: AlbumsRepository,
    private val dateDisplayer: DateDisplayer,
    private val photosUseCase: PhotosUseCase,
    private val albumWorkScheduler: AlbumWorkScheduler,
) : AlbumsUseCase {

    override fun observePersonAlbums(personId: Int): Flow<List<Album>> = albumsRepository.observePersonAlbums(personId)
        .map {
            it.mapValues {
                getPersonAlbums -> getPersonAlbums.toDbAlbums()
            }
        }
        .mapToAlbums()

    override fun observeAlbums(): Flow<List<Album>> = albumsRepository.observeAlbumsByDate()
        .map {
            it.mapValues {
                    getAlbums -> getAlbums.toDbAlbums()
            }
        }
        .mapToAlbums()

    override suspend fun getPersonAlbums(personId: Int): List<Album> = albumsRepository.getPersonAlbums(personId)
        .mapValues {
            getAlbums -> getAlbums.toDbAlbums()
        }
        .mapToAlbums()

    override suspend fun getAlbums(): List<Album> = albumsRepository.getAlbumsByDate()
        .mapValues {
            getAlbums -> getAlbums.toDbAlbums()
        }
        .mapToAlbums()

    private fun Flow<Group<String, DbAlbums>>.mapToAlbums(): Flow<List<Album>> = map { albums ->
        albums.mapToAlbums()
    }
        .distinctUntilChanged()
        .safelyOnStartIgnoring {
            if (!albumsRepository.hasAlbums()) {
                startRefreshAlbumsWork(shallow = false)
            }
        }

    private fun Group<String, DbAlbums>.mapToAlbums(): List<Album> = items.map { (id, photos) ->
        val albumDate = photos.firstOrNull()?.albumDate
        val albumLocation = photos.firstOrNull()?.albumLocation

        Album(
            id = id,
            photoCount = photos.size,
            date = dateDisplayer.dateString(albumDate),
            location = albumLocation ?: "",
            photos = photos.mapNotNull { item ->
                item.photoId?.let { id ->
                    Photo(
                        id = id,
                        thumbnailUrl = with(photosUseCase) {
                            id.toThumbnailUrlFromId()
                        },
                        fullResUrl = with(photosUseCase) {
                            id.toFullSizeUrlFromId(item.isVideo)
                        },
                        fallbackColor = item.dominantColor,
                        isFavourite = (item.rating ?: 0) >= FAVOURITES_RATING_THRESHOLD,
                        ratio = item.aspectRatio ?: 1.0f,
                        isVideo = item.isVideo,
                    )
                }
            }
        )
    }.filter { it.photos.isNotEmpty() }

    override fun startRefreshAlbumsWork(shallow: Boolean) {
        albumWorkScheduler.scheduleAlbumsRefreshNow(shallow)
    }
}



private data class DbAlbums(
    val id: String,
    val albumDate: String?,
    val albumLocation: String?,
    val photoId: String?,
    val dominantColor: String?,
    val rating: Int?,
    val aspectRatio: Float?,
    val type: String?,
    val isVideo: Boolean,
)

private fun GetPersonAlbums.toDbAlbums() = DbAlbums(
    id = id,
    albumDate = albumDate,
    albumLocation = albumLocation,
    photoId = photoId,
    dominantColor = dominantColor,
    rating = rating,
    aspectRatio = aspectRatio,
    type = type,
    isVideo = isVideo,
)
private fun GetAlbums.toDbAlbums() = DbAlbums(
    id = id,
    albumDate = albumDate,
    albumLocation = albumLocation,
    photoId = photoId,
    dominantColor = dominantColor,
    rating = rating,
    aspectRatio = aspectRatio,
    type = type,
    isVideo = isVideo,
)