package no.hiof.danieljr.drawerly.data.auth

import no.hiof.danieljr.drawerly.data.model.User

interface AuthService {suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, displayName: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): User?
}