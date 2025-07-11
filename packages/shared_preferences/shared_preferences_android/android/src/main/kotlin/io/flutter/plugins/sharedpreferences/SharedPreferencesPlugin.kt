// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.preference.PreferenceManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.lang.ClassCastException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

const val TAG = "SharedPreferencesPlugin"
const val SHARED_PREFERENCES_NAME = "FlutterSharedPreferences"
const val LIST_PREFIX = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBhIGxpc3Qu"
const val DOUBLE_PREFIX = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBEb3VibGUu"

private val Context.sharedPreferencesDataStore: DataStore<Preferences> by
    preferencesDataStore(SHARED_PREFERENCES_NAME)

/// SharedPreferencesPlugin
class SharedPreferencesPlugin() : FlutterPlugin, SharedPreferencesAsyncApi {
  private lateinit var context: Context
  private var backend: SharedPreferencesBackend? = null

  private var listEncoder = ListEncoder() as SharedPreferencesListEncoder

  @VisibleForTesting
  constructor(listEncoder: SharedPreferencesListEncoder) : this() {
    this.listEncoder = listEncoder
  }

  private fun setUp(messenger: BinaryMessenger, context: Context) {
    this.context = context
    try {
      SharedPreferencesAsyncApi.setUp(messenger, this, "data_store")
      backend = SharedPreferencesBackend(messenger, context, listEncoder)
    } catch (ex: Exception) {
      Log.e(TAG, "Received exception while setting up SharedPreferencesPlugin", ex)
    }
  }

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    setUp(binding.binaryMessenger, binding.applicationContext)
    LegacySharedPreferencesPlugin().onAttachedToEngine(binding)
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    SharedPreferencesAsyncApi.setUp(binding.binaryMessenger, null, "data_store")
    backend?.tearDown()
    backend = null
  }

  /** Adds property to data store of type bool. */
  override fun setBool(key: String, value: Boolean, options: SharedPreferencesPigeonOptions) {
    return runBlocking {
      val boolKey = booleanPreferencesKey(key)
      context.sharedPreferencesDataStore.edit { preferences -> preferences[boolKey] = value }
    }
  }

  /** Adds property to data store of type String. */
  override fun setString(key: String, value: String, options: SharedPreferencesPigeonOptions) {
    return runBlocking { dataStoreSetString(key, value) }
  }

  private suspend fun dataStoreSetString(key: String, value: String) {
    val stringKey = stringPreferencesKey(key)
    context.sharedPreferencesDataStore.edit { preferences -> preferences[stringKey] = value }
  }

  /** Adds property to data store of type int. Converted to Long by pigeon, and saved as such. */
  override fun setInt(key: String, value: Long, options: SharedPreferencesPigeonOptions) {
    return runBlocking {
      val intKey = longPreferencesKey(key)
      context.sharedPreferencesDataStore.edit { preferences -> preferences[intKey] = value }
    }
  }

  /** Adds property to data store of type double. */
  override fun setDouble(key: String, value: Double, options: SharedPreferencesPigeonOptions) {
    return runBlocking {
      val doubleKey = doublePreferencesKey(key)
      context.sharedPreferencesDataStore.edit { preferences -> preferences[doubleKey] = value }
    }
  }

  /** Adds property to data store of type List<String>. */
  override fun setStringList(
      key: String,
      value: List<String>,
      options: SharedPreferencesPigeonOptions
  ) {
    val valueString = LIST_PREFIX + listEncoder.encode(value)
    return runBlocking { dataStoreSetString(key, valueString) }
  }

  /** Removes all properties from data store. */
  override fun clear(allowList: List<String>?, options: SharedPreferencesPigeonOptions) {
    runBlocking {
      context.sharedPreferencesDataStore.edit { preferences ->
        allowList?.let { list ->
          list.forEach { key ->
            val preferencesKey = booleanPreferencesKey(key)
            preferences.remove(preferencesKey)
          }
        } ?: preferences.clear()
      }
    }
  }

  /** Gets all properties from data store. */
  override fun getAll(
      allowList: List<String>?,
      options: SharedPreferencesPigeonOptions
  ): Map<String, Any> {
    return runBlocking { getPrefs(allowList) }
  }

  /** Gets int (as long) at [key] from data store. */
  override fun getInt(key: String, options: SharedPreferencesPigeonOptions): Long? {
    val value: Long?
    runBlocking {
      val preferencesKey = longPreferencesKey(key)
      val preferenceFlow: Flow<Long?> =
          context.sharedPreferencesDataStore.data.map { preferences -> preferences[preferencesKey] }
      value = preferenceFlow.firstOrNull()
    }
    return value
  }

  /** Gets bool at [key] from data store. */
  override fun getBool(key: String, options: SharedPreferencesPigeonOptions): Boolean? {
    val value: Boolean?

    runBlocking {
      val preferencesKey = booleanPreferencesKey(key)
      val preferenceFlow: Flow<Boolean?> =
          context.sharedPreferencesDataStore.data.map { preferences -> preferences[preferencesKey] }

      value = preferenceFlow.firstOrNull()
    }
    return value
  }
  /** Gets double at [key] from data store. */
  override fun getDouble(key: String, options: SharedPreferencesPigeonOptions): Double? {
    val value: Double?
    runBlocking {
      val preferencesKey = stringPreferencesKey(key)
      val preferenceFlow: Flow<Double?> =
          context.sharedPreferencesDataStore.data.map { preferences ->
            transformPref(preferences[preferencesKey] as Any?, listEncoder) as Double?
          }

      value = preferenceFlow.firstOrNull()
    }
    return value
  }

  /** Gets String at [key] from data store. */
  override fun getString(key: String, options: SharedPreferencesPigeonOptions): String? {
    val value: String?
    runBlocking {
      val preferencesKey = stringPreferencesKey(key)
      val preferenceFlow: Flow<String?> =
          context.sharedPreferencesDataStore.data.map { preferences -> preferences[preferencesKey] }

      value = preferenceFlow.firstOrNull()
    }
    return value
  }

  /** Gets StringList at [key] from data store. */
  override fun getStringList(key: String, options: SharedPreferencesPigeonOptions): List<String>? {
    val value: List<*>? = transformPref(getString(key, options) as Any?, listEncoder) as List<*>?
    return value?.filterIsInstance<String>()
  }

  /** Gets all properties from data store. */
  override fun getKeys(
      allowList: List<String>?,
      options: SharedPreferencesPigeonOptions
  ): List<String> {
    val prefs = runBlocking { getPrefs(allowList) }
    return prefs.keys.toList()
  }

  private suspend fun getPrefs(allowList: List<String>?): Map<String, Any> {
    val allowSet = allowList?.toSet()
    val filteredMap = mutableMapOf<String, Any>()

    val keys = readAllKeys()
    keys?.forEach { key ->
      val value = getValueByKey(key)
      if (preferencesFilter(key.toString(), value, allowSet)) {
        val transformedValue = transformPref(value, listEncoder)
        if (transformedValue != null) {
          filteredMap[key.toString()] = transformedValue
        }
      }
    }
    return filteredMap
  }

  private suspend fun readAllKeys(): Set<Preferences.Key<*>>? {
    val keys = context.sharedPreferencesDataStore.data.map { it.asMap().keys }
    return keys.firstOrNull()
  }

  private suspend fun getValueByKey(key: Preferences.Key<*>): Any? {
    val value = context.sharedPreferencesDataStore.data.map { it[key] }
    return value.firstOrNull()
  }
}

class SharedPreferencesBackend(
    private var messenger: BinaryMessenger,
    private var context: Context,
    private var listEncoder: SharedPreferencesListEncoder = ListEncoder()
) : SharedPreferencesAsyncApi {

  init {
    try {
      SharedPreferencesAsyncApi.setUp(messenger, this, "shared_preferences")
    } catch (ex: Exception) {
      Log.e(TAG, "Received exception while setting up SharedPreferencesBackend", ex)
    }
  }

  fun tearDown() {
    SharedPreferencesAsyncApi.setUp(messenger, null, "shared_preferences")
  }

  private fun createSharedPreferences(options: SharedPreferencesPigeonOptions): SharedPreferences {
    return if (options.fileName == null) {
      PreferenceManager.getDefaultSharedPreferences(context)
    } else {
      context.getSharedPreferences(options.fileName, Context.MODE_PRIVATE)
    }
  }

  /** Adds property to data store of type bool. */
  override fun setBool(key: String, value: Boolean, options: SharedPreferencesPigeonOptions) {
    return createSharedPreferences(options).edit().putBoolean(key, value).apply()
  }

  /** Adds property to data store of type String. */
  override fun setString(key: String, value: String, options: SharedPreferencesPigeonOptions) {
    return createSharedPreferences(options).edit().putString(key, value).apply()
  }

  /** Adds property to data store of type int. Converted to Long by pigeon, and saved as such. */
  override fun setInt(key: String, value: Long, options: SharedPreferencesPigeonOptions) {
    return createSharedPreferences(options).edit().putLong(key, value).apply()
  }

  /** Adds property to data store of type double. */
  override fun setDouble(key: String, value: Double, options: SharedPreferencesPigeonOptions) {
    return createSharedPreferences(options).edit().putString(key, DOUBLE_PREFIX + value).apply()
  }

  /** Adds property to data store of type List<String>. */
  override fun setStringList(
      key: String,
      value: List<String>,
      options: SharedPreferencesPigeonOptions
  ) {
    val valueString = LIST_PREFIX + listEncoder.encode(value)
    return createSharedPreferences(options).edit().putString(key, valueString).apply()
  }

  /** Removes all properties from data store. */
  override fun clear(allowList: List<String>?, options: SharedPreferencesPigeonOptions) {
    val preferences = createSharedPreferences(options)
    val clearEditor: SharedPreferences.Editor = preferences.edit()
    val allPrefs: Map<String, *> = preferences.all
    val filteredPrefs = ArrayList<String>()
    for (key in allPrefs.keys) {
      if (preferencesFilter(key, allPrefs[key], allowList = allowList?.toSet())) {
        filteredPrefs.add(key)
      }
    }
    for (key in filteredPrefs) {
      clearEditor.remove(key)
    }
    return clearEditor.apply()
  }

  /** Gets all properties from data store. */
  override fun getAll(
      allowList: List<String>?,
      options: SharedPreferencesPigeonOptions
  ): Map<String, Any> {
    val preferences = createSharedPreferences(options)
    val allPrefs: Map<String, *> = preferences.all
    val filteredPrefs = HashMap<String, Any>()
    for (entry in allPrefs.entries) {
      if (preferencesFilter(entry.key, entry.value, allowList = allowList?.toSet())) {
        entry.value?.let { filteredPrefs.put(entry.key, transformPref(it, listEncoder) as Any) }
      }
    }
    return filteredPrefs
  }

  /** Gets int (as long) at [key] from data store. */
  override fun getInt(key: String, options: SharedPreferencesPigeonOptions): Long? {
    val preferences = createSharedPreferences(options)
    return if (preferences.contains(key)) {
      try {
        preferences.getLong(key, 0)
      } catch (e: ClassCastException) {
        // Retry with getInt in case the preference was written by native code directly.
        preferences.getInt(key, 0).toLong()
      }
    } else {
      null
    }
  }

  /** Gets bool at [key] from data store. */
  override fun getBool(key: String, options: SharedPreferencesPigeonOptions): Boolean? {
    val preferences = createSharedPreferences(options)
    return if (preferences.contains(key)) {
      preferences.getBoolean(key, true)
    } else {
      null
    }
  }
  /** Gets double at [key] from data store. */
  override fun getDouble(key: String, options: SharedPreferencesPigeonOptions): Double? {
    val preferences = createSharedPreferences(options)
    return if (preferences.contains(key)) {
      transformPref(preferences.getString(key, ""), listEncoder) as Double
    } else {
      null
    }
  }

  /** Gets String at [key] from data store. */
  override fun getString(key: String, options: SharedPreferencesPigeonOptions): String? {
    val preferences = createSharedPreferences(options)
    return if (preferences.contains(key)) {
      preferences.getString(key, "")
    } else {
      null
    }
  }

  /** Gets StringList at [key] from data store. */
  override fun getStringList(key: String, options: SharedPreferencesPigeonOptions): List<String>? {
    val preferences = createSharedPreferences(options)
    return if (preferences.contains(key)) {
      (transformPref(preferences.getString(key, ""), listEncoder) as List<*>?)?.filterIsInstance<
          String>()
    } else {
      null
    }
  }

  /** Gets all properties from data store. */
  override fun getKeys(
      allowList: List<String>?,
      options: SharedPreferencesPigeonOptions
  ): List<String> {
    val preferences = createSharedPreferences(options)
    return preferences.all
        .filter { preferencesFilter(it.key, it.value, allowList?.toSet()) }
        .keys
        .toList()
  }
}

/**
 * Returns false for any preferences that are not included in [allowList].
 *
 * If no [allowList] is provided, instead returns false for any preferences that are not supported
 * by shared_preferences.
 */
internal fun preferencesFilter(key: String, value: Any?, allowList: Set<String>?): Boolean {
  if (allowList == null) {
    return value is Boolean || value is Long || value is String || value is Double
  }

  return allowList.contains(key)
}

/** Transforms preferences that are stored as Strings back to original type. */
internal fun transformPref(value: Any?, listEncoder: SharedPreferencesListEncoder): Any? {
  if (value is String) {
    if (value.startsWith(LIST_PREFIX)) {
      return listEncoder.decode(value.substring(LIST_PREFIX.length))
    } else if (value.startsWith(DOUBLE_PREFIX)) {
      return value.substring(DOUBLE_PREFIX.length).toDouble()
    }
  }
  return value
}

/** Class that provides tools for encoding and decoding List<String> to String and back. */
class ListEncoder : SharedPreferencesListEncoder {
  override fun encode(list: List<String>): String {
    val byteStream = ByteArrayOutputStream()
    val stream = ObjectOutputStream(byteStream)
    stream.writeObject(list)
    stream.flush()
    return Base64.encodeToString(byteStream.toByteArray(), 0)
  }

  override fun decode(listString: String): List<String> {
    val byteArray = Base64.decode(listString, 0)
    val stream = StringListObjectInputStream(ByteArrayInputStream(byteArray))
    return (stream.readObject() as List<*>).filterIsInstance<String>()
  }
}
