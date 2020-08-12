package kr.ac.kpu.mywordbook

import android.R.id.checkbox
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_word_book.*
import kotlinx.android.synthetic.main.word_list.*
import kotlinx.android.synthetic.main.word_list.view.*


class WordBookActivity : AppCompatActivity() {

    val database = Firebase.database

    lateinit var listview : ListView
    lateinit var adapter : WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_book)

        var wList = arrayListOf<ListWord>()

        listview = findViewById(R.id.word_listview)
        adapter = WordAdapter()
        listview.adapter = adapter


        val count = adapter.count

        //val cb : CheckBox? = findViewById(R.id.word_check) as CheckBox
        //listview.onItemClickListener =
        //    AdapterView.OnItemClickListener { parent, view, position, id ->
        //    cb!!.setChecked(true)
        //}


        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val email = intent.getStringExtra("email")

        val myRef1 = database.getReference("users/$email/$date/$title")

        myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                adapter = WordAdapter()
                listview.adapter = adapter

                for (snapshot in p0.children) {
                    wList.add(ListWord("${snapshot.key.toString()}", "${snapshot.value.toString()}"))
                }

                for (i in 0 until wList.size) {
                    adapter.addItem("${wList[i].egWord}", "${wList[i].krWord}")
                    //Log.d(ContentValues.TAG, "###############################")
                }
                wList.clear()

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
                finish()
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
                        adapter.notifyDataSetChanged()
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

        btn_deleteWord.setOnClickListener {

            val myRef = database.getReference("users/$email/$date/$title")


            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    listview = findViewById(R.id.word_listview)
                    adapter = WordAdapter()
                    listview.adapter = adapter
                    val checkedItems = listview.checkedItemPositions

                    for (snapshot in p0.children) {
                        wList.add(ListWord("${snapshot.key.toString()}", "${snapshot.value.toString()}"))
                    }
                    //Toast.makeText(this@WordBookActivity, "5", Toast.LENGTH_SHORT).show()
                    for (i in 0 until wList.size) {
                        Toast.makeText(this@WordBookActivity, "${wList.size}", Toast.LENGTH_SHORT).show()
                        if (checkedItems.get(i)) {
                            wList.removeAt(i)
                            myRef.child("${wList[i].egWord}").removeValue()
                            //Toast.makeText(this@WordBookActivity, "5", Toast.LENGTH_SHORT).show()
                        }
                    }
                    listview.clearChoices()
                    //Toast.makeText(this, "${transWordList1.size} ${transWordList.size}", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()

                    for(i in 0 until wList.size){
                        adapter.addItem("${wList[i].egWord}","${wList[i].krWord}")
                    }
                }
            })
            //Toast.makeText(this, "${wList.size}", Toast.LENGTH_SHORT).show()

            //Toast.makeText(this,"삭제되었습니다.",Toast.LENGTH_SHORT).show()

        }
    }
}