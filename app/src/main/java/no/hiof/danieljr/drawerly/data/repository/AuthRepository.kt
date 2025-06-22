package no.hiof.danieljr.drawerly.data.repository

import no.hiof.danieljr.drawerly.data.model.User
import no.hiof.danieljr.drawerly.data.auth.AuthService

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, password: String, displayName: String): Result<User> // 👈 Add this
    suspend fun signInWithGoogle(idToken: String): Result<User> // 👈 Add this
    suspend fun logout()
    fun getCurrentUser(): User?
}

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {
    override suspend fun login(email: String, password: String) = authService.login(email, password)
    override suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun logout() = authService.logout()
    override fun getCurrentUser() = authService.getCurrentUser()
}
