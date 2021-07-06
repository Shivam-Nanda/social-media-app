package com.example.socialmediaapp.Daos

import com.example.socialmediaapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {

    private val db=FirebaseFirestore.getInstance()
    private val usersCollections = db.collection("users")

    fun addUser(user : User?){

        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usersCollections.document(user.uid).set(it)
            }
        }
    }

    fun removeUser(uid: String){

            GlobalScope.launch(Dispatchers.IO) {
                usersCollections.document(uid).delete()
            }

    }

    fun getUserById(uId:String) : Task<DocumentSnapshot>{
        return usersCollections.document(uId).get()
    }
}