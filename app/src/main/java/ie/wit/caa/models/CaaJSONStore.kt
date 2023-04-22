//package ie.wit.caa.models
//
//import android.content.Context
//import android.net.Uri
//import androidx.lifecycle.MutableLiveData
//import com.google.gson.*
//import com.google.gson.reflect.TypeToken
//import ie.wit.caa.api.CAAClient
//import ie.wit.caa.helpers.exists
//import ie.wit.caa.helpers.read
//import ie.wit.caa.helpers.write
//import retrofit2.Call
//import retrofit2.Callback
//
//import retrofit2.Response
//import timber.log.Timber
//import java.lang.reflect.Type
//import java.util.*
//
//
//const val JSON_FILE = "caa.json"
//val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
//    .registerTypeAdapter(Uri::class.java, UriParser())
//    .create()
//val listType: Type = object : TypeToken<ArrayList<CaaModel>>() {}.type
//
//fun generateRandomId(): Long {
//    return Random().nextLong()
//}
//
//object CaaJSONStore : CaaStore {
//    private lateinit var context: Context
//
//    private var caas = ArrayList<CaaModel>()
//
//    fun initialize(context: Context) {
//        this.context = context
//        if (exists(context, JSON_FILE)) {
//            deserialize()
//        }
//    }
//
//    override fun findAll(caaList: MutableLiveData<List<CaaModel>>) {
//      val call = CAAClient.getApi().getall()
//
//        call.enqueue(object : Callback<List<CaaModel>> {
//            override fun onResponse(call: Call<List<CaaModel>>,
//                                    response: Response<List<CaaModel>>
//            ) {
//                caaList.value = response.body() as ArrayList<CaaModel>
//                Timber.i("Retrofit JSON = ${response.body()}")
//            }
//
//            override fun onFailure(call: Call<List<CaaModel>>, t: Throwable) {
//                Timber.i("Retrofit Error : $t.message")
//            }
//        })
//    }
//
//
//
//    override fun findById(id: Long): CaaModel? {
//        val foundCaa: CaaModel? = caas.find { it.id == id }
//        return foundCaa
//    }
//
//    override fun create(caa: CaaModel) {
//        caa.id = generateRandomId()
//        caas.add(caa)
//        serialize()
//    }
//
//    override fun delete(caa: CaaModel) {
//        caas.remove(caa)
//        serialize()
//    }
//
//    override fun update(caa: CaaModel) {
////        val caaList = findAll() as ArrayList<CaaModel>
////        val foundCrime: CaaModel? = caaList.find { p -> p.id == caa.id }
////        if (foundCrime != null) {
////            foundCrime.name = caa.name
////            foundCrime.type = caa.type
////            foundCrime.level = caa.level
////            foundCrime.date = caa.date
////            foundCrime.description = caa.description
////            foundCrime.lat = caa.lat
////            foundCrime.lng = caa.lng
////            foundCrime.zoom = caa.zoom
////        }
////        serialize()
//    }
//
//
//    override fun findByName(name: String): CaaModel? {
//        val foundCaa: CaaModel? = caas.find { it.name.equals(name, ignoreCase = true) }
//        return foundCaa
//    }
//
//
//    private fun serialize() {
//        val jsonString = gsonBuilder.toJson(caas, listType)
//        write(context, JSON_FILE, jsonString)
//    }
//
//    private fun deserialize() {
//        val jsonString = read(context, JSON_FILE)
//        caas = gsonBuilder.fromJson(jsonString, listType)
//    }
//
//    private fun logAll() {
//        caas.forEach { Timber.i("$it") }
//    }
//}
//
//
//
//class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
//    override fun deserialize(
//        json: JsonElement?,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): Uri {
//        return Uri.parse(json?.asString)
//    }
//
//    override fun serialize(
//        src: Uri?,
//        typeOfSrc: Type?,
//        context: JsonSerializationContext?
//    ): JsonElement {
//        return JsonPrimitive(src.toString())
//    }
//
//}
//
