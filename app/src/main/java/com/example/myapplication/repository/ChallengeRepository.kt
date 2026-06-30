package com.example.myapplication.repository

import com.example.myapplication.data.ChallengeDao
import com.example.myapplication.data.ChallengeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChallengeRepository(
    private val dao: ChallengeDao
) {
    fun observeChallenges(): Flow<List<ChallengeEntity>> = dao.observeAll()

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        if (dao.count() == 0) {
            val defaults = listOf(
                "Baila por 10 segundos sin parar.",
                "Cuenta un chiste y haz reir al grupo.",
                "Imita a tu personaje favorito.",
                "Haz 5 sentadillas rapidas.",
                "Di un trabalenguas sin equivocarte."
            )
            defaults.forEach { description ->
                dao.insert(ChallengeEntity(description = description))
            }
        }
    }

    suspend fun addChallenge(description: String) = withContext(Dispatchers.IO) {
        dao.insert(ChallengeEntity(description = description.trim()))
    }

    suspend fun updateChallenge(challenge: ChallengeEntity, newDescription: String) =
        withContext(Dispatchers.IO) {
            dao.update(challenge.copy(description = newDescription.trim()))
        }

    suspend fun deleteChallenge(challenge: ChallengeEntity) = withContext(Dispatchers.IO) {
        dao.delete(challenge)
    }

    suspend fun getRandomChallenge(): ChallengeEntity? = withContext(Dispatchers.IO) {
        dao.getRandom()
    }
}

