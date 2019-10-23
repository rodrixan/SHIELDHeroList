package com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel

import androidx.lifecycle.LiveData
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero

interface HeroesListViewModel {
    val heroesList: LiveData<AppInternalData<List<SuperHero>>>

    fun refreshSuperHeroesList()

    fun queryHeroes(query: String)
}