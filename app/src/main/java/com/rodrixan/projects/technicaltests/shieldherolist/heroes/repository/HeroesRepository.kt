package com.rodrixan.projects.technicaltests.shieldherolist.heroes.repository

import androidx.lifecycle.LiveData
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero

interface HeroesRepository {
    val heroesList: LiveData<AppInternalData<List<SuperHero>>>
    val heroDetail: LiveData<AppInternalData<SuperHero>>

    suspend fun getSuperHeroesList()
    suspend fun getHeroDetail(name: String)
    suspend fun searchForHeroes(query: String): LiveData<AppInternalData<List<SuperHero>>>
}