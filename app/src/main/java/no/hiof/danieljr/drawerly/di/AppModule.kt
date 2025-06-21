package no.hiof.danieljr.drawerly.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.hiof.danieljr.drawerly.data.auth.AuthService
import no.hiof.danieljr.drawerly.data.auth.FirebaseAuthService
import no.hiof.danieljr.drawerly.data.repository.AuthRepository
import no.hiof.danieljr.drawerly.data.repository.AuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FirebaseAuthService()

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository = AuthRepositoryImpl(authService)
}
