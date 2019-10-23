package com.example.rodri.projectlist.common.rest.api

import com.rodrixan.projects.technicaltests.shieldherolist.common.rest.model.SuperHeroList
import retrofit2.Response
import retrofit2.http.GET

interface SuperHeroApi {
    @GET("bins/bvyob/")
    suspend fun getHeroesList(): Response<SuperHeroList>
}