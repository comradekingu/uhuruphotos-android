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
package com.savvasdalkitsis.uhuruphotos.person.viewmodel

import com.savvasdalkitsis.uhuruphotos.navigation.ControllersProvider
import com.savvasdalkitsis.uhuruphotos.person.viewmodel.PersonEffect.NavigateBack
import com.savvasdalkitsis.uhuruphotos.person.viewmodel.PersonEffect.OpenPhotoDetails
import com.savvasdalkitsis.uhuruphotos.photos.model.PhotoSequenceDataSource.PersonResults
import com.savvasdalkitsis.uhuruphotos.photos.navigation.PhotoNavigationTarget
import com.savvasdalkitsis.uhuruphotos.viewmodel.EffectHandler
import javax.inject.Inject

class PersonEffectHandler @Inject constructor(
    private val controllersProvider: ControllersProvider,
) : EffectHandler<PersonEffect> {

    override suspend fun invoke(effect: PersonEffect) {
        when (effect) {
            NavigateBack -> controllersProvider.navController!!.popBackStack()
            is OpenPhotoDetails -> controllersProvider.navController!!.navigate(with(effect) {
                PhotoNavigationTarget.name(id, center, scale, video, PersonResults(person.id))
            })
        }
    }

}
