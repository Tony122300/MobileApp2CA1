//package ie.wit.caa.models
//
//import timber.log.Timber
//
//var lastId = 0L
//
//internal fun getId(): Long {
//    return lastId++
//}
//object CaaManager : CaaStore {
//
//        val caas = ArrayList<CaaModel>()
//
//        override fun findAll(): List<CaaModel> {
//            return caas
//        }
//
//        override fun findById(id:Long) : CaaModel? {
//            val foundCaa: CaaModel? = caas.find { it.id == id }
//            return foundCaa
//        }
//
//    override fun update(caa: CaaModel) {
//        var foundCrime: CaaModel? = caas.find{p ->p.id == caa.id}
//        if (foundCrime != null){
//            foundCrime.name = caa.name
//            foundCrime.type = caa.type
//            foundCrime.level = caa.level
//            foundCrime.date = caa.date
//            foundCrime.description = caa.description
//            foundCrime.lat = caa.lat
//            foundCrime.lng = caa.lng
//            foundCrime.zoom = caa.zoom
//            logAll()
//        }
//    }
//
//        override fun create(caa: CaaModel) {
//            caa.id = getId()
//            caas.add(caa)
//            logAll()
//        }
//
//    override fun delete(caa: CaaModel) {
//        caas.remove(caa)
//    }
//
//    override fun findByName(name: String): CaaModel? {
//        TODO("Not yet implemented")
//    }
//
//    fun logAll() {
//            Timber.v("** crime/accident List **")
//            caas.forEach { Timber.v("crime/accident ${it}") }
//        }
//
//    }