package kr.ac.kpu.mywordbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SharedWordBookActivity : AppCompatActivity() {

    val database = Firebase.database

    lateinit var listview : ListView
    lateinit var adapter : NoCheckBoxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_word_book)


        var swList = arrayListOf<ListWord>()

        listview = findViewById(R.id.shared_listview)




        //val swb = intent.getStringArrayExtra("swb")
        //val position = intent.getStringExtra("position")
        val title = intent.getStringExtra("title")
        val email = intent.getStringExtra("email")

        val myRef = database.getReference("users/share/$email/$title")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                adapter = NoCheckBoxAdapter()
                listview.adapter = adapter

                for (snapshot in p0.children) {
                    swList.add(ListWord("${snapshot.key.toString()}", "${snapshot.value.toString()}"))
                }

                for (i in 0 until swList.size) {
                    adapter.addItem("${swList[i].egWord}", "${swList[i].krWord}")
                }
                swList.clear()
            }
        })



    }
}