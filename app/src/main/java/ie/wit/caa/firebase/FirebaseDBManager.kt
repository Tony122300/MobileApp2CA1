package ie.wit.caa.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.caa.models.CaaModel
import ie.wit.caa.models.CaaStore
import timber.log.Timber

object FirebaseDBManager : CaaStore {
   var database: DatabaseReference = FirebaseDatabase.getInstance("https://mobileapp2-371501-default-rtdb.europe-west1.firebasedatabase.app/").reference

   // ref = Database.database("https://<databaseName><region>.firebasedatabase.app")
    override fun findAll(caaList: MutableLiveData<List<CaaModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, caaList: MutableLiveData<List<CaaModel>>) {
        database.child("user-crimes").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<CaaModel>()
                    val children = snapshot.children
                    children.forEach {
                        val caa = it.getValue(CaaModel::class.java)
                        localList.add(caa!!)
                    }
                    database.child("user-crimes").child(userid)
                        .removeEventListener(this)

                    caaList.value = localList
                }
            })
    }

    override fun findById(userid: String, caaid: String, caa: MutableLiveData<CaaModel>) {
        database.child("user-crimes").child(userid)
            .child(caaid).get().addOnSuccessListener {
                caa.value = it.getValue(CaaModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, caa: CaaModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("crimes").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        caa.uid = key
        val caaValues = caa.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/crimes/$key"] = caaValues
        childAdd["/user-crimes/$uid/$key"] = caaValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, caaid: String) {
        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/crimes/$caaid"] = null
        childDelete["/user-crimes/$userid/$caaid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, caaid: String, caa: CaaModel) {

    }

    override fun findByName(name: String): CaaModel? {
        TODO("Not yet implemented")
    }
}
