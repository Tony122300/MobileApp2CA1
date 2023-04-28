package ie.wit.caa.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface CaaStore {
    fun findAllInAll(caaList: MutableLiveData<List<CaaModel>>)
    fun findAll(userid:String, caaList: MutableLiveData<List<CaaModel>>)
    fun findById(userid:String, caaid: String, caa: MutableLiveData<CaaModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, caa: CaaModel)
    fun delete(userid:String, caaid: String)
    fun update(userid:String, caaid: String, caa: CaaModel)
    fun findByName(name: String): CaaModel?

}