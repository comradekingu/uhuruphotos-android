package com.savvasdalkitsis.librephotos.photos.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.savvasdalkitsis.librephotos.navigation.navigationTarget
import com.savvasdalkitsis.librephotos.photos.mvflow.PhotoAction
import com.savvasdalkitsis.librephotos.photos.mvflow.PhotoAction.LoadPhoto
import com.savvasdalkitsis.librephotos.photos.mvflow.PhotoEffect
import com.savvasdalkitsis.librephotos.photos.view.Photo
import com.savvasdalkitsis.librephotos.photos.view.state.PhotoState
import com.savvasdalkitsis.librephotos.photos.viewmodel.PhotoEffectsHandler
import com.savvasdalkitsis.librephotos.photos.viewmodel.PhotoViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

class PhotoNavigationTarget @Inject constructor(
    private val effectsHandler: PhotoEffectsHandler,
) {

    @ExperimentalFoundationApi
    @FlowPreview
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    fun NavGraphBuilder.create() {
        navigationTarget<PhotoState, PhotoEffect, PhotoAction, PhotoViewModel>(
            name = name,
            effects = effectsHandler,
            enterTransition = {
                slideIn(initialOffset = { fullSize ->
                    targetState.center.offsetFrom(fullSize)
                }) +
                scaleIn(initialScale = targetState.scale) + fadeIn()
            },
            exitTransition = {
               slideOut(targetOffset = { fullSize ->
                   initialState.center.offsetFrom(fullSize)
               }) +
                scaleOut(targetScale = initialState.scale) + fadeOut()
            },
            initializer = { navBackStackEntry, actions ->
                actions(LoadPhoto(navBackStackEntry.photoId))
            },
            createModel = { hiltViewModel() }
        ) { state, actions ->
            Photo(state, actions)
        }
    }

    companion object {
        private const val name = "photo/{id}/{centerX}/{centerY}/{scale}"
        fun id(id: String) = idWithCenterAndScale(id, Offset(0.5f, 0.5f), 0.3f)
        fun idWithCenterAndScale(id: String, offset: Offset, scale: Float) = name
            .replace("{id}", id)
            .replace("{centerX}", offset.x.toString())
            .replace("{centerY}", offset.y.toString())
            .replace("{scale}", scale.toString())

        private val NavBackStackEntry.photoId : String get() = get("id")!!
        private val NavBackStackEntry.center : Offset? get() {
            val x = get("centerX")?.toFloat()
            val y = get("centerY")?.toFloat()
            return if (x != null && y != null) Offset(x, y) else null
        }
        private val NavBackStackEntry.scale : Float get() = get("scale")?.toFloat() ?: 0.3f

        private fun NavBackStackEntry.get(arg: String) = arguments!!.getString(arg)
    }

    private fun Offset?.offsetFrom(size: IntSize) = when {
        this != null -> IntOffset((x - size.width/2).toInt(), (y - size.height/2).toInt())
        else -> IntOffset.Zero
    }
}
