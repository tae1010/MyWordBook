package kr.ac.kpu.mywordbook

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_shared_word_book.*
import kotlinx.android.synthetic.main.fragshared.*
import kr.ac.kpu.mywordbook.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*
내 단어장에서 공유시킨 단어장이 데이터베이스에 share로 이동하고 share에 있는 단어장을 리스트뷰에 띄워줌
 */

class SharedFragment : Fragment() {

    val database = Firebase.database

    lateinit var listview: ListView
    lateinit var adapter: WordBookAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val email2 = activity!!.intent.getStringExtra("email")
        //Toast.makeText(activity, "$email2", Toast.LENGTH_SHORT).show()
        

        var swbList = arrayListOf<ListWordBook>()
        val view = inflater.inflate(R.layout.fragshared, container, false)


        listview = view!!.findViewById(R.id.lv_sharedWordBook) as ListView


        val myRef = database.getReference("users/share")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    val myRef2 = database.getReference("users/share/${snapshot.key}")
                    myRef2.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            adapter = WordBookAdapter()
                            listview.adapter = adapter
                            for (snapshot in p0.children) {
                                swbList.add(ListWordBook("${snapshot.key.toString()}", "${myRef2.key.toString()}"))
                                //Toast.makeText(activity, "1", Toast.LENGTH_SHORT).show()
                            }
                            for (i in 0 until swbList.size) {
                                adapter.addItem("${swbList[i].title}", "${swbList[i].date}")
                                //Toast.makeText(activity, "2", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        })

        //Toast.makeText(activity, "111", Toast.LENGTH_SHORT).show()


        listview.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(activity, SharedWordBookActivity::class.java)
                //intent.putExtra("email",email2)
                //val bundle = intent.getBundleExtra("bundle")
               // val email2 = bundle!!.getSerializable("email2")
                //Toast.makeText(activity, "$email2", Toast.LENGTH_SHORT).show()

                //intent.putExtra("swb",swbList)
                //intent.putExtra("position",position)
                //intent.putExtra("email",email)
                intent.putExtra("title", swbList[position].title)
                intent.putExtra("email", swbList[position].date)
                intent.putExtra("email2",email2)
                startActivity(intent)
            }
        return view
    }
}