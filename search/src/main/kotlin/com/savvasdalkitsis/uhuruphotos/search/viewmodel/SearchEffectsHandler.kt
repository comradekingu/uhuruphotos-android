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
package com.savvasdalkitsis.uhuruphotos.search.viewmodel

import com.savvasdalkitsis.uhuruphotos.heatmap.navigation.HeatMapNavigationTarget
import com.savvasdalkitsis.uhuruphotos.home.navigation.HomeNavigationTarget
import com.savvasdalkitsis.uhuruphotos.navigation.ControllersProvider
import com.savvasdalkitsis.uhuruphotos.people.api.navigation.PeopleNavigationTarget
import com.savvasdalkitsis.uhuruphotos.person.api.navigation.PersonNavigationTarget
import com.savvasdalkitsis.uhuruphotos.photos.model.PhotoSequenceDataSource.SearchResults
import com.savvasdalkitsis.uhuruphotos.photos.navigation.PhotoNavigationTarget
import com.savvasdalkitsis.uhuruphotos.search.mvflow.SearchEffect
import com.savvasdalkitsis.uhuruphotos.search.mvflow.SearchEffect.*
import com.savvasdalkitsis.uhuruphotos.server.navigation.ServerNavigationTarget
import com.savvasdalkitsis.uhuruphotos.settings.navigation.SettingsNavigationTarget
import com.savvasdalkitsis.uhuruphotos.strings.R
import com.savvasdalkitsis.uhuruphotos.toaster.Toaster
import com.savvasdalkitsis.uhuruphotos.viewmodel.EffectHandler
import javax.inject.Inject

class SearchEffectsHandler @Inject constructor(
    private val controllersProvider: ControllersProvider,
    private val toaster: Toaster,
) : EffectHandler<SearchEffect> {

    override suspend fun invoke(
        effect: SearchEffect,
    ) = when (effect) {
        HideKeyboard -> controllersProvider.keyboardController!!.hide()
        ReloadApp -> {
            with(controllersProvider.navController!!) {
                backQueue.clear()
                navigate(HomeNavigationTarget.name)
            }
        }
        NavigateToEditServer -> navigateTo(
            ServerNavigationTarget.name(auto = false)
        )
        NavigateToSettings -> navigateTo(SettingsNavigationTarget.name)
        is OpenPhotoDetails -> navigateTo(
            with(effect) {
                PhotoNavigationTarget.name(id, center, scale, isVideo, SearchResults(currentQuery))
            }
        )
        ErrorSearching -> toaster.show(R.string.error_searching)
        NavigateToAllPeople -> navigateTo(PeopleNavigationTarget.name)
        ErrorRefreshingPeople -> toaster.show(R.string.error_refreshing_people)
        is NavigateToPerson -> navigateTo(PersonNavigationTarget.name(effect.personId))
        NavigateToHeatMap -> navigateTo(HeatMapNavigationTarget.name)
    }

    private fun navigateTo(target: String) {
        controllersProvider.navController!!.navigate(target)
    }
}
