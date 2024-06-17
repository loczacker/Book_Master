package com.zacker.bookmaster.ui.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zacker.bookmaster.R
import com.zacker.bookmaster.util.Const
import com.zacker.bookmaster.util.Event
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val resultData = MutableLiveData(false)
    private val prefs: SharedPreferences = application.getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)

    private val _startRegister = MutableLiveData<Event<Boolean>>()
    val startRegister: LiveData<Event<Boolean>>
        get() = _startRegister

    private val _showToast = MutableLiveData<Event<String>>()
    val showToast: LiveData<Event<String>>
        get() = _showToast

    private val _googleSignInIntent = MutableLiveData<Event<IntentSenderRequest>>()
    val googleSignInIntent: LiveData<Event<IntentSenderRequest>>
        get() = _googleSignInIntent

    private var oneTapClient: SignInClient? = null
    private lateinit var signInRequest: BeginSignInRequest

    init {
        val context = getApplication<Application>().applicationContext
        oneTapClient = Identity.getSignInClient(context)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.your_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            try {
                val result = oneTapClient?.beginSignIn(signInRequest)?.await()
                val intentSenderRequest = IntentSenderRequest.Builder(result!!.pendingIntent).build()
                _googleSignInIntent.postValue(Event(intentSenderRequest))
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Google Sign-In failed: ${e.message}")
            }
        }
    }

    fun handleGoogleSignInResult(data: Intent?) {
        try {
            val credential = oneTapClient!!.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val auth: FirebaseAuth = Firebase.auth
                auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserEmail(auth.currentUser?.email ?: "")
                        resultData.postValue(true)
                        showToast("Logged in successfully.")
                    } else {
                        resultData.postValue(false)
                        showToast("Google Sign-In failed.")
                    }
                }
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            showToast("Google Sign-In failed: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        _showToast.value = Event(message)
    }

    private fun saveUserEmail(email: String) {
        prefs.edit().apply {
            putBoolean(Const.KEY_LOGIN, true)
            putString(Const.KEY_EMAIL_USER, email)
            apply()
        }
        Log.d("LoginViewModel", "Saved email: $email")
    }

    fun login(email: String, pass: String) {
        val auth: FirebaseAuth = Firebase.auth
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserEmail(email)
                    resultData.postValue(true)
                    showToast("Logged in successfully.")
                } else {
                    resultData.postValue(false)
                    showToast("Login failed. Please try again.")
                }
            }
    }

    fun showRegister() {
        _startRegister.value = Event(true)
    }
}
