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
package com.savvasdalkitsis.uhuruphotos.feedpage.usecase

import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.savvasdalkitsis.uhuruphotos.feed.view.state.FeedDisplays
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedPageUseCase @Inject constructor(
    preferences: FlowSharedPreferences,
) {
    private val preference = preferences.getEnum("feedDisplay", defaultValue = FeedDisplays.default)

    fun getFeedDisplay(): Flow<FeedDisplays> = preference.asFlow()

    suspend fun setFeedDisplay(feedDisplay: FeedDisplays) {
        preference.setAndCommit(feedDisplay)
    }
}