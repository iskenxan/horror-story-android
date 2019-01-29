package samatov.space.spookies.model.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


inline fun <reified T> Gson.fromGenericJson(json: String): T =
        this.fromJson<T>(json, object : TypeToken<T>(){}.type)