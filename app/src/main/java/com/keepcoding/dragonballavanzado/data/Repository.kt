package com.keepcoding.dragonballavanzado.data

import com.keepcoding.dragonballavanzado.data.local.LocalDataSource
import com.keepcoding.dragonballavanzado.data.remote.RemoteDataSource
import com.keepcoding.dragonballavanzado.models.HeroLocal
import com.keepcoding.dragonballavanzado.models.HeroRemote
import com.keepcoding.dragonballavanzado.models.HeroUI
import com.keepcoding.dragonballavanzado.models.LocationRemote
import com.keepcoding.dragonballavanzado.models.LocationUI
import com.keepcoding.dragonballavanzado.models.mapToLocal
import com.keepcoding.dragonballavanzado.models.mapToUI
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {
    
    suspend fun login(username: String, password: String): Response<String> {
        return remoteDataSource.login(username, password)
    }
    
    suspend fun getHeros(): List<HeroUI> {
        val token = getToken()
        
        // TODO: Mirar si hay datos nuevos
        
        val localHeros: List<HeroLocal> = localDataSource.getHeros()
        
        return if (localHeros.isNotEmpty()) {
            
            localHeros.map { it.mapToUI() }
        } else {
            
            // Get from network
            var remoteHeros: List<HeroRemote> = emptyList()
            token?.let { 
                remoteHeros = remoteDataSource.getHeros(it)
            }
            
            // Save in local
            localDataSource.insertHeros(remoteHeros.map { it.mapToLocal() })

            // Get from local
            val updateLocalHeros: List<HeroLocal> = localDataSource.getHeros()
            updateLocalHeros.map { it.mapToUI() }
        }
    }
    
    suspend fun getLocations(heroID: String): List<LocationUI> {
        val token = getToken()
        
        var locations: List<LocationRemote> = emptyList()
        token?.let {
            locations = remoteDataSource.getLocations(token, heroID)
        }
        
        return locations.map { it.mapToUI() }
    }

    suspend fun getHeroDetail(id: String): HeroUI {
        val heroLocal = localDataSource.getHeroDetail(id)

        return heroLocal.mapToUI()
    }
    
    fun getToken(): String? {
        return localDataSource.getToken()
    }

    fun setToken(value: String) {
        localDataSource.setToken(value)
    }
    
}