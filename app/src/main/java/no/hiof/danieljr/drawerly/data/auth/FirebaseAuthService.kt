package no.hiof.danieljr.drawerly.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import no.hiof.danieljr.drawerly.data.model.User
import kotlin.coroutines.resume

import com.google.firebase.auth.userProfileChangeRequest

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

    override suspend fun register(email: String, password: String, name: String): Result<User> {
        return suspendCancellableCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        if (firebaseUser != null) {
                            val profileUpdates = userProfileChangeRequest {
                                displayName = name
                            }
                            firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        val user = User(
                                            uid = firebaseUser.uid,
                                            email = firebaseUser.email ?: "",
                                            displayName = name
                                        )
                                        cont.resume(Result.success(user)) {}
                                    } else {
                                        cont.resume(Result.failure(updateTask.exception ?: Exception("Failed to update profile"))) {}
                                    }
                                }
                        } else {
                            cont.resume(Result.failure(Exception("User is null after registration"))) {}
                        }
                    } else {
                        cont.resume(Result.failure(task.exception ?: Exception("Registration failed"))) {}
                    }
                }
        }
    }



    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return suspendCancellableCoroutine { cont ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
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
                        cont.resume(Result.failure(task.exception ?: Exception("Google sign-in failed"))) {}
                    }
                }
        }
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


