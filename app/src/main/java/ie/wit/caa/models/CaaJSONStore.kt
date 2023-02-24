package ie.wit.caa.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.caa.helpers.exists
import ie.wit.caa.helpers.read
import ie.wit.caa.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "caa.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<CaaModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CaaJSONStore(private val context: Context) : CaaStore {

    var caas = mutableListOf<CaaModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<CaaModel> {
        logAll()
        return caas
    }

    override fun findById(id: Long): CaaModel? {
        TODO("Not yet implemented")
    }

    override fun create(caa: CaaModel) {
        caa.id = generateRandomId()
        caas.add(caa)
        serialize()
    }

    override fun delete(caa: CaaModel) {
        caas.remove(caa)
        serialize()
    }

    override fun update(caa: CaaModel) {
        val caaList = findAll() as ArrayList<CaaModel>
        var foundCrime: CaaModel? = caaList.find{ p ->p.id == caa.id}
        if (foundCrime != null){
            foundCrime.name = caa.name
            foundCrime.type = caa.type
            foundCrime.level = caa.level
            foundCrime.date = caa.date
            foundCrime.description = caa.description
            foundCrime.lat = caa.lat
            foundCrime.lng = caa.lng
            foundCrime.zoom = caa.zoom
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(caas, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        caas = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        caas.forEach { Timber.i("$it") }
    }
}


class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }

}

