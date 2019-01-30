package samatov.space.spookies.model.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SerializerK {

   companion object {
       inline fun <reified T> fromJson(json: String): T {
           return Gson().fromJson(json, object: TypeToken<T>(){}.type)
       }
   }
}