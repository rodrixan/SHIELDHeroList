package com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model

import com.google.gson.annotations.SerializedName
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData

data class SuperHeroList(@SerializedName("superheroes")
                         val superheroes: List<SuperHero>)

class SuperHero(@SerializedName("name")
                val name: String?,
                @SerializedName("photo")
                val photo: String?,
                @SerializedName("realName")
                val realName: String?,
                @SerializedName("height")
                val height: String?,
                @SerializedName("power")
                val power: String?,
                @SerializedName("abilities")
                val abilities: String?,
                @SerializedName("groups")
                val groups: String?) {

    val groupsFormatted: String
        get() {
            return formatGroups()
        }

    private fun formatGroups(): String {
        return groups?.run {
            if (isBlank() || isEmpty()) {
                this
            } else {
                val sb = StringBuilder(GROUP_FIRST_BULLET_SEPARATOR)
                sb.append(replace(Regex(SPLIT_GROUP_REGEXP), GROUP_BULLET_SEPARATOR))
                sb.toString()
            }
        } ?: ""
    }

    companion object {
        private const val SPLIT_GROUP_REGEXP = "[,;]"
        private const val GROUP_FIRST_BULLET_SEPARATOR = "- "
        private const val GROUP_BULLET_SEPARATOR = "\n-"
    }

}

data class ErrorResponse(@SerializedName("status")
                         val code: Int? = -1,
                         @SerializedName("message")
                         val message: String?) {
    fun toAppInternalData(): AppInternalData.Error {
        return if (message == null) {
            AppInternalData.Error(code = code ?: -1)
        } else {
            AppInternalData.Error(message, code ?: -1)
        }
    }
}