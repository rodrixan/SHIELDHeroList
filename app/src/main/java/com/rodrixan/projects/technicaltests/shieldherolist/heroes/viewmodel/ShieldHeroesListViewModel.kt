package com.rodrixan.projects.technicaltests.shieldherolist.heroes.viewmodel

import androidx.lifecycle.*
import com.rodrixan.projects.technicaltests.shieldherolist.common.data.AppInternalData
import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHero
import com.rodrixan.projects.technicaltests.shieldherolist.heroes.repository.HeroesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShieldHeroesListViewModel(private val repository: HeroesRepository) : ViewModel(), HeroesListViewModel {

    private val heroQuery = MutableLiveData<String>()

    private val repositoryHeroList = repository.heroesList

    private val repositoryHeroesQueryList = Transformations.switchMap(heroQuery,
            ::searchHeroesInRepository)

    override val heroesList: MediatorLiveData<AppInternalData<List<SuperHero>>> = MediatorLiveData()

    init {
        heroesList.addSource(repositoryHeroList) { heroes ->
            heroesList.value = heroes
        }
        heroesList.addSource(repositoryHeroesQueryList) { queriedHeroes ->
            heroesList.value = queriedHeroes
        }
    }

    override fun refreshSuperHeroesList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSuperHeroesList()
        }
    }

    override fun queryHeroes(query: String) {
        heroQuery.postValue(query)
    }

    private fun searchHeroesInRepository(query: String): LiveData<AppInternalData<List<SuperHero>>> {
        return runBlocking {
            viewModelScope.async(Dispatchers.IO) {
                repository.searchForHeroes(query)
            }.await()
        }
    }
}