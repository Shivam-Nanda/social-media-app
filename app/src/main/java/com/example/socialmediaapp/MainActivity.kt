package com.example.socialmediaapp

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediaapp.Daos.PostDao
import com.example.socialmediaapp.Daos.UserDao
import com.example.socialmediaapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao
    private lateinit var userDao: UserDao
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Define ActionBar object

        // Define ActionBar object
        val actionBar: ActionBar?
        actionBar = supportActionBar

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        val colorDrawable = ColorDrawable(Color.parseColor("#FF4500"))

        // Set BackgroundDrawable

        // Set BackgroundDrawable
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(colorDrawable)
        }

        fab.setOnClickListener{
            val intent = Intent(this,CreatePostActivity::class.java)
            startActivity(intent)
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        postDao = PostDao()
        userDao = UserDao()
        auth=Firebase.auth
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val postCollections = postDao.postCollection
        val query = postCollections.orderBy("createdAt",Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()

        adapter= PostAdapter(recyclerViewOptions,this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter=adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflator = menuInflater
        inflator.inflate(R.menu.my_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item1-> {

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you Sure You want to log out?")
                        .setPositiveButton("Log Out",DialogInterface.OnClickListener { dialog, which ->
//                            userDao.removeUser(auth.currentUser!!.uid)

                            mGoogleSignInClient.signOut();
                            //                LoginManager.getInstance().logOut();
                            //                auth.currentUser!!.delete().addOnCompleteListener{
                            //                    if (it.isSuccessful){
                            //                        Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show()
                            //                    }
                            //                }
                            //                GoogleAuthProvider.getCredential(googleIdToken,null);
                            auth.signOut()
                            val backIntent = Intent(this,SignInActivity::class.java)
                            startActivity(backIntent)

                            finish()
                        }
                        )
                        .setNegativeButton("Cancel",null)
                val alert = builder.create()
                alert.show()
                return true
            }
            R.id.item2->{
                val profileIntent = Intent(this,ProfileActivity::class.java)
                startActivity(profileIntent)
                return true
            }
            else->{
                return super.onOptionsItemSelected(item)
            }
        }

    }
}