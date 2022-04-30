package com.savvasdalkitsis.uhuruphotos.person.api.navigation

import androidx.navigation.NavBackStackEntry

object PersonNavigationTarget {
    const val registrationName = "person/{personId}"
    fun name(id: Int) = registrationName.replace("{personId}", id.toString())
    val NavBackStackEntry.personId : Int get() =
        arguments!!.getString("personId")!!.toInt()
}