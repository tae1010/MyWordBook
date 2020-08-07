package kr.ac.kpu.mywordbook

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_word_book.*

class WordBookActivity : AppCompatActivity() {

    val database = Firebase.database

    lateinit var listview : ListView
    lateinit var adapter : WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_book)

        var wList = arrayListOf<ListWord>()

        adapter = WordAdapter()
        listview = findViewById(R.id.word_listview)
        listview.adapter = adapter

        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val email = intent.getStringExtra("email")

        val myRef1 = database.getReference("users/$email/$date/$title")

        myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    wList.add(ListWord("${snapshot.key.toString()}", "${snapshot.value.toString()}"))
                }
            }
        })


        btn_addWord.setOnClickListener {
            val dlgView = layoutInflater.inflate(R.layout.add_wordbook, null)
            val btn_picture : Button = dlgView.findViewById(R.id.add_picture)
            val btn_text : Button = dlgView.findViewById(R.id.add_text)
            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgView)
            dlgBuilder.show()

            btn_picture.setOnClickListener {
                val intent = Intent(this,PictureTextActivity::class.java)
                intent.putExtra("title",title)
                intent.putExtra("date",date)
                intent.putExtra("email",email)
                startActivity(intent)
            }
            btn_text.setOnClickListener {
                val dlg = layoutInflater.inflate(R.layout.input_wordbook, null)
                val dlgBuilder = AlertDialog.Builder(this)
                dlgBuilder.setView(dlg)


                dlgBuilder.setPositiveButton("추가") { dialogInterface, i ->
                    val etWord = dlg.findViewById<EditText>(R.id.edit_word)
                    val etMeaning  = dlg.findViewById<EditText>(R.id.edit_meaning)
                    wList.add(ListWord("${etWord.text}","${etMeaning.text}"))

                    val myRef = database.getReference("users/$email/$date/$title")

                    for (i in 0 until wList.size) {
                        myRef.child("${wList[i].egWord}").setValue("${wList[i].krWord}")
                        adapter.addItem("${wList[i].egWord}", "${wList[i].krWord}")
                    }
/*
                    for(i in 0 until wList.size) {
                        adapter.addItem("${wList[i].egWord}", "${wList[i].krWord}")
                    }*/
                    wList.clear()

                }.setNegativeButton("취소") { dialogInterface, i ->

                }.show()
            }
        }
    }
}