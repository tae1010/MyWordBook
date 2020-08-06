package kr.ac.kpu.mywordbook

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_word_book.*

class WordBookActivity : AppCompatActivity() {

    lateinit var listview : ListView
    lateinit var adapter : WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_book)

        adapter = WordAdapter()
        listview = findViewById(R.id.word_listview)
        listview.adapter = adapter

        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        //adapter.addItem("$title","$date")


        btn_addWord.setOnClickListener {
            val dlgView = layoutInflater.inflate(R.layout.add_wordbook, null)
            val btn_picture : Button = dlgView.findViewById(R.id.add_picture)
            val btn_text : Button = dlgView.findViewById(R.id.add_text)
            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgView)
            dlgBuilder.show()

            btn_picture.setOnClickListener {
                val intent = Intent(this,PictureTextActivity::class.java)
                startActivity(intent)
            }
            btn_text.setOnClickListener {
                val dlg = layoutInflater.inflate(R.layout.input_wordbook, null)
                val dlgBuilder = AlertDialog.Builder(this)
                dlgBuilder.setView(dlg)
                val etWord = dlgView.findViewById<EditText>(R.id.edit_word)
                val etMeaning = dlgView.findViewById<EditText>(R.id.edit_meaning)

                dlgBuilder.setPositiveButton("추가") { dialogInterface, i ->
                   adapter.addItem("${etWord.text}","${etMeaning.text}")
                   //adapter.addItem("가나","다가가")

                }.setNegativeButton("취소") { dialogInterface, i ->

                }.show()
            }
        }
    }
}