package com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel

import androidx.lifecycle.LiveData
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero

interface HeroDetailViewModel {
    val heroDetails: LiveData<AppInternalData<SuperHero>>

    fun queryHeroDetails(name: String)
}