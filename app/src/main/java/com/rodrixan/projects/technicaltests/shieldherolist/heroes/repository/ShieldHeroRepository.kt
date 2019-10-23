package com.rodrixan.projects.technicaltests.shieldherolist.heroes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Either
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.service.SuperHeroApiService
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.service.toSuccessOrError
import kotlinx.coroutines.delay
import timber.log.Timber

class ShieldHeroRepository : HeroesRepository {
    override val heroesList: MutableLiveData<AppInternalData<List<SuperHero>>> = MutableLiveData()
    override val heroDetail: MutableLiveData<AppInternalData<SuperHero>> = MutableLiveData()

    //This could be a room database or similar. Used a Hashmap for simplicity
    private var heroCache: Map<String, SuperHero> = HashMap()
    private var lastListRefreshTimeMs: Long = -1

    override suspend fun getSuperHeroesList() {
        //only make a call if it's not already loading
        if (heroesList.value is AppInternalData.Loading) {
            return
        }

        Timber.d("Get superhero list")
        heroesList.postValue(AppInternalData.Loading)

        if (hasCacheExpired()) {
            Timber.d("Getting heroes from service...")

            val response = getHeroesFromService()

            Timber.d("Received heroes: $response")

            if (response is AppInternalData.Success) {
                heroCache = response.data.map { (it.name ?: UNKNOWN_DATA) to it }.toMap()
            }

            heroesList.postValue(response)

        } else {
            //Get the values from the hashmap
            heroesList.postValue(AppInternalData.Success(heroCache.toList().map { it.second }).also {
                Timber.d("Getting heroes from cache: ${it.data}")
            })
        }
    }

    private fun hasCacheExpired() = heroCache.isEmpty() || System.currentTimeMillis() - lastListRefreshTimeMs >= CACHE_EXPIRATION_TIME_MS

    private suspend fun getHeroesFromService(): AppInternalData<List<SuperHero>> {
        lastListRefreshTimeMs = System.currentTimeMillis()

        //let's simulate some delay, as the server responds almost immediately
        delay(2000)

        val response = SuperHeroApiService.serviceApi.getHeroesList().toSuccessOrError()
        return when (response) {
            is Either.Left -> {
                AppInternalData.Success(response.a.superheroes)
            }
            is Either.Right -> {
                response.b.toAppInternalData()
            }
        }
    }

    override suspend fun getHeroDetail(name: String) {
        Timber.d("[getHeroDetail] get details for $name")
        //we don't mind about cache being expired for details
        heroDetail.postValue(AppInternalData.Loading)

        val hero = heroCache[name]
        hero?.run {
            heroDetail.postValue(AppInternalData.Success(this))
        } ?: heroDetail.postValue(AppInternalData.Error())
    }

    override suspend fun searchForHeroes(query: String): LiveData<AppInternalData<List<SuperHero>>> {
        Timber.d("[searchForHeroes] query heroes: $query")

        /*search in cache for quicker response
            1. Name
            2. Real Name
         */
        if (query.isEmpty() || query.isBlank()) {
            return MutableLiveData(AppInternalData.Success(heroCache.toList().map { it.second }))
        }

        //No loading, show data live
        val result = heroCache.toList().map { it.second }.filter {
            it.name?.contains(query, true) == true || it.realName?.contains(query, true) == true
        }

        return MutableLiveData(AppInternalData.Success(result))
    }

    companion object {
        private const val UNKNOWN_DATA = "UNKNOWN"
        private const val CACHE_EXPIRATION_TIME_MS = 60000 //60 seconds for cache to expire
    }
}