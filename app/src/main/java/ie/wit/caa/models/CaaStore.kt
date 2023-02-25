package ie.wit.caa.models

interface CaaStore {
    fun findAll() : List<CaaModel>
    fun findById(id: Long) : CaaModel?
    fun create(caa: CaaModel)
    fun update(caa: CaaModel)
    fun delete(caa: CaaModel)
    fun findByName(name: String): CaaModel?

}