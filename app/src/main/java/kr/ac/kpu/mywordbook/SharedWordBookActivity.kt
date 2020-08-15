package kr.ac.kpu.mywordbook

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_shared_word_book.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
/*
공유 단어장에 있는 단어들을 보여줌
 */
class SharedWordBookActivity : AppCompatActivity() {

    val database = Firebase.database

    lateinit var listview : ListView
    lateinit var adapter : NoCheckBoxAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_word_book)


        var swList = arrayListOf<ListWord>()
        var swList2 = arrayListOf<ListWord>()

        listview = findViewById(R.id.shared_listview)




        //val swb = intent.getStringArrayExtra("swb")
        //val position = intent.getStringExtra("position")
        val title = intent.getStringExtra("title")
        val email = intent.getStringExtra("email")
        val email2 = intent.getStringExtra("email2")


        val myRef = database.getReference("users/share/$email/$title")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                adapter = NoCheckBoxAdapter()
                listview.adapter = adapter

                for (snapshot in p0.children) {
                    swList.add(ListWord("${snapshot.key.toString()}", "${snapshot.value.toString()}"))
                    swList2.add(ListWord("${snapshot.key.toString()}", "${snapshot.value.toString()}"))
                }

                for (i in 0 until swList.size) {
                    adapter.addItem("${swList[i].egWord}", "${swList[i].krWord}")
                }
                swList.clear()
            }
        })

// 버튼을 클릭하면 공유단어장에 있는 단어장이 내 단어장으로 들어옴
        btn_SharedWord.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
            val nowtime = current.format(formatter)

            adapter = NoCheckBoxAdapter()
            listview.adapter = adapter

            val myRef2 = database.getReference("users")
            for(i in 0 until swList2.size) {
                myRef2.child("$email2").child("$nowtime").child("$title").child("${swList2[i].egWord}")
                    .setValue("${swList2[i].krWord}")
            }
            finish()
        }
    }
}