package com.techawarenessma.app.certification

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Keeps certification progress on the device, like the website's `taa-cert-platform-v1`
 * localStorage entry. Signing out keeps checkpoint progress, exactly as the site does.
 */
class CertificationStore(private val prefs: SharedPreferences) {

    constructor(context: Context) : this(context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE))

    private val _state = MutableStateFlow(load())
    val state: StateFlow<CertificationState> = _state.asStateFlow()

    /** Returns false (and changes nothing) when the code isn't in TAA-XXXX form. */
    fun signIn(code: String): Boolean {
        if (!isValidAccessCode(code)) return false
        update { it.copy(authed = true) }
        return true
    }

    fun signOut() = update { it.copy(authed = false) }

    fun openSection(section: CertSection) = update { it.copy(lastSection = section) }

    fun setModuleDone(section: CertSection, done: Boolean) = update {
        it.copy(doneModules = if (done) it.doneModules + section.id else it.doneModules - section.id)
    }

    fun setCertItemReady(itemId: String, ready: Boolean) = update {
        it.copy(certItems = if (ready) it.certItems + itemId else it.certItems - itemId)
    }

    private fun update(transform: (CertificationState) -> CertificationState) {
        _state.update(transform)
        save(_state.value)
    }

    private fun load() = CertificationState(
        authed = prefs.getBoolean(KEY_AUTHED, false),
        lastSection = CertSection.byId(prefs.getString(KEY_SECTION, null)) ?: CertSection.HowToUse,
        doneModules = prefs.getStringSet(KEY_DONE, null).orEmpty().toSet(),
        certItems = prefs.getStringSet(KEY_CERT, null).orEmpty().toSet(),
    )

    private fun save(state: CertificationState) = prefs.edit {
        putBoolean(KEY_AUTHED, state.authed)
        putString(KEY_SECTION, state.lastSection.id)
        // Copies: SharedPreferences must never be handed a set it will later see mutated.
        putStringSet(KEY_DONE, HashSet(state.doneModules))
        putStringSet(KEY_CERT, HashSet(state.certItems))
    }

    private companion object {
        const val PREFS_NAME = "taa-cert-platform-v1"
        const val KEY_AUTHED = "authed"
        const val KEY_SECTION = "screen"
        const val KEY_DONE = "done"
        const val KEY_CERT = "cert"
    }
}
