package app.denhan.module

import android.content.Context

/**
 * Created by Chirag.
 * Created on 19/01/18.
 */
class ResourceProvider (private val context: Context){

    fun getStringResource(stringId: Int): String{
        return context.getString(stringId)
    }

}