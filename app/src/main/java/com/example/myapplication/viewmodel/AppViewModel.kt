package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.myapplication.audio.AudioController
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.PokemonApi
import com.example.myapplication.repository.ChallengeRepository
import com.example.myapplication.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "pico_bottle.db"
    ).build()

    private val challengeRepository = ChallengeRepository(database.challengeDao())

    private val pokemonRepository = PokemonRepository(
        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    )

    private val audioController = AudioController()

    private val _isAudioOn = MutableStateFlow(true)
    val isAudioOn: StateFlow<Boolean> = _isAudioOn.asStateFlow()

    val challenges = challengeRepository.observeChallenges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            challengeRepository.ensureSeeded()
            _isAudioOn.value = true
            startBackgroundAudioIfNeeded()
        }
    }

    fun toggleAudio() {
        _isAudioOn.value = !_isAudioOn.value
        if (_isAudioOn.value) {
            startBackgroundAudioIfNeeded()
        } else {
            audioController.stopBackground()
        }
    }

    fun pauseAudioTemporarily() {
        audioController.pauseBackground()
    }

    fun resumeAudioIfOn() {
        if (_isAudioOn.value) {
            audioController.resumeBackground()
        }
    }

    fun playSpinSound(durationMs: Long) {
        if (!_isAudioOn.value) return
        audioController.playSpin(durationMs)
    }

    fun startBackgroundAudioIfNeeded() {
        if (_isAudioOn.value) {
            audioController.startBackground(viewModelScope)
        }
    }

    fun stopBackgroundAudio() {
        audioController.stopBackground()
    }

    fun addChallenge(description: String) {
        viewModelScope.launch {
            challengeRepository.addChallenge(description)
        }
    }

    fun updateChallenge(id: Long, description: String) {
        val challenge = challenges.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            challengeRepository.updateChallenge(challenge, description)
        }
    }

    fun deleteChallenge(id: Long) {
        val challenge = challenges.value.firstOrNull { it.id == id } ?: return
        viewModelScope.launch {
            challengeRepository.deleteChallenge(challenge)
        }
    }

    suspend fun getRandomChallengeText(): String {
        val challenge = challengeRepository.getRandomChallenge()
        return challenge?.description ?: "Sin retos disponibles."
    }

    suspend fun getRandomPokemonImage(): String? {
        return pokemonRepository.getRandomPokemon()?.img
    }

    override fun onCleared() {
        audioController.stopBackground()
        super.onCleared()
    }
}

