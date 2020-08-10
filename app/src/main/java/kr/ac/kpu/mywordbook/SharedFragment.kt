package kr.ac.kpu.mywordbook

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragshared.*
import kr.ac.kpu.mywordbook.R

class SharedFragment : Fragment() {

    val database = Firebase.database

    lateinit var listview: ListView
    lateinit var adapter: WordBookAdapter

    var swbList = arrayListOf<ListWordBook>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragshared, container,false)

        adapter = WordBookAdapter()
        listview = view!!.findViewById(R.id.lv_sharedWordBook) as ListView
        listview.adapter = adapter

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
                            for (snapshot in p0.children) {
                                swbList.add(ListWordBook("${snapshot.key.toString()}", "${myRef2.key.toString()}"))

                            }
                            Toast.makeText(activity,"${swbList.size}",Toast.LENGTH_SHORT).show()
                            for (i in 0 until swbList.size) {
                                adapter.addItem("${swbList[i].title}", "${swbList[i].date}")
                            }
                        }
                    })
                }
            }
        })

        for (i in 0 until swbList.size) {
            adapter.addItem("${swbList[i].title}", "${swbList[i].date}")
        }


        return view
    }
}