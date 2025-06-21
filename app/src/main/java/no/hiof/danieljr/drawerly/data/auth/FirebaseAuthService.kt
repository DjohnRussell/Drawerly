package no.hiof.danieljr.drawerly.data.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import no.hiof.danieljr.drawerly.data.model.User
import kotlin.coroutines.resume

class FirebaseAuthService(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : AuthService {

    override suspend fun login(email: String, password: String): Result<User> {
        return suspendCancellableCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        if (firebaseUser != null) {
                            val user = User(
                                uid = firebaseUser.uid,
                                email = firebaseUser.email ?: "",
                                displayName = firebaseUser.displayName ?: ""
                            )
                            cont.resume(Result.success(user)) {}
                        } else {
                            cont.resume(Result.failure(Exception("User is null"))) {}
                        }
                    } else {
                        cont.resume(Result.failure(task.exception ?: Exception("Login failed"))) {}
                    }
                }
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: ""
        )
    }
}
