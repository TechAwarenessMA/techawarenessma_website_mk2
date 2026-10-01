package com.techawarenessma.app.certification

import android.content.SharedPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CertificationStoreTest {

    @Test
    fun rejectsMalformedCodesWithoutSigningIn() {
        val store = CertificationStore(InMemoryPreferences())
        assertFalse(store.signIn("hello"))
        assertFalse(store.state.value.authed)
    }

    @Test
    fun progressPersistsAcrossInstances() {
        val prefs = InMemoryPreferences()
        CertificationStore(prefs).apply {
            assertTrue(signIn("TAA-2026"))
            setModuleDone(CertSection.M1, true)
            setModuleDone(CertSection.M4, true)
            setCertItemReady("repair", true)
            openSection(CertSection.M4)
        }

        val restored = CertificationStore(prefs).state.value
        assertTrue(restored.authed)
        assertEquals(setOf("m1", "m4"), restored.doneModules)
        assertEquals(setOf("repair"), restored.certItems)
        assertEquals(CertSection.M4, restored.lastSection)
    }

    @Test
    fun undoingACheckpointRemovesIt() {
        val store = CertificationStore(InMemoryPreferences())
        store.setModuleDone(CertSection.M2, true)
        store.setModuleDone(CertSection.M2, false)
        assertEquals(0, store.state.value.doneCount)
    }

    @Test
    fun signingOutKeepsProgressLikeTheWebsite() {
        val prefs = InMemoryPreferences()
        val store = CertificationStore(prefs)
        store.signIn("TAA-ABC")
        store.setModuleDone(CertSection.M5, true)
        store.signOut()

        val restored = CertificationStore(prefs).state.value
        assertFalse(restored.authed)
        assertEquals(setOf("m5"), restored.doneModules)
    }

    @Test
    fun unknownStoredSectionFallsBackToTheStart() {
        val prefs = InMemoryPreferences()
        prefs.edit().putString("screen", "p99").apply()
        assertEquals(CertSection.HowToUse, CertificationStore(prefs).state.value.lastSection)
    }
}

/** Just enough SharedPreferences for the store, without Robolectric. */
private class InMemoryPreferences : SharedPreferences {
    private val values = mutableMapOf<String, Any?>()

    override fun getAll(): MutableMap<String, *> = values.toMutableMap()
    override fun getString(key: String, defValue: String?) = values[key] as String? ?: defValue

    @Suppress("UNCHECKED_CAST")
    override fun getStringSet(key: String, defValues: MutableSet<String>?) = (values[key] as Set<String>?)?.toMutableSet() ?: defValues
    override fun getInt(key: String, defValue: Int) = values[key] as Int? ?: defValue
    override fun getLong(key: String, defValue: Long) = values[key] as Long? ?: defValue
    override fun getFloat(key: String, defValue: Float) = values[key] as Float? ?: defValue
    override fun getBoolean(key: String, defValue: Boolean) = values[key] as Boolean? ?: defValue
    override fun contains(key: String) = key in values
    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) = Unit
    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) = Unit

    override fun edit(): SharedPreferences.Editor = object : SharedPreferences.Editor {
        private val pending = mutableMapOf<String, Any?>()
        private var clear = false
        override fun putString(key: String, value: String?) = apply { pending[key] = value }
        override fun putStringSet(key: String, values: MutableSet<String>?) = apply { pending[key] = values?.toSet() }
        override fun putInt(key: String, value: Int) = apply { pending[key] = value }
        override fun putLong(key: String, value: Long) = apply { pending[key] = value }
        override fun putFloat(key: String, value: Float) = apply { pending[key] = value }
        override fun putBoolean(key: String, value: Boolean) = apply { pending[key] = value }
        override fun remove(key: String) = apply { pending[key] = null }
        override fun clear() = apply { clear = true }
        override fun commit(): Boolean {
            apply()
            return true
        }
        override fun apply() {
            if (clear) values.clear()
            pending.forEach { (key, value) -> if (value == null) values.remove(key) else values[key] = value }
        }
    }
}
