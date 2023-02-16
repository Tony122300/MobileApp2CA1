package ie.wit.caa.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}
class CaaMemStore : CaaStore {

        val caas = ArrayList<CaaModel>()

        override fun findAll(): List<CaaModel> {
            return caas
        }

        override fun findById(id:Long) : CaaModel? {
            val foundCaa: CaaModel? = caas.find { it.id == id }
            return foundCaa
        }

        override fun create(caa: CaaModel) {
            caa.id = getId()
            caas.add(caa)
            logAll()
        }

        fun logAll() {
            Timber.v("** crime/accident List **")
            caas.forEach { Timber.v("crime/accident ${it}") }
        }
    }