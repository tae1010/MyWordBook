package kr.ac.kpu.mywordbook

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.autofill.Dataset
import android.view.*
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragmywordbook.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


var wbList = arrayListOf<ListWordBook>()

class myWordBookFragment : Fragment() {

    val database = Firebase.database

    lateinit var listview : ListView
    lateinit var adapter : WordBookAdapter


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmywordbook, container,false)

        setHasOptionsMenu(true)

        adapter = WordBookAdapter()
        listview = view!!.findViewById(R.id.lv_wordbook) as ListView
        listview.adapter = adapter





        //Toast.makeText(activity,"$email",Toast.LENGTH_SHORT).show()
        val email = activity!!.intent.getStringExtra("email")
        val myRef1 = database.getReference("users/$email")

        myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                var i = 0
                for (snapshot in p0.children) {
                    wbList.add(ListWordBook("${snapshot.child("${snapshot.key}").key.toString()}","${snapshot.key.toString()}"))
                }
            }
        })
        for(i in 0 until wbList.size){
            adapter.addItem("${wbList[i].title}","${wbList[i].date}")
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu,inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newmemo -> {
                val email = activity!!.intent.getStringExtra("email")
                val dlgView = layoutInflater.inflate(R.layout.wordbookname, null)
                val addwordbook : EditText = dlgView.findViewById(R.id.ed_name)
                val dlgBuilder = AlertDialog.Builder(activity)
                dlgBuilder.setView(dlgView)

                dlgBuilder.setPositiveButton("추가") { dialogInterface, i ->

                    adapter = WordBookAdapter()

                    listview = view!!.findViewById(R.id.lv_wordbook) as ListView
                    listview.adapter = adapter

                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
                    val nowtime = current.format(formatter)

                    wbList.add(ListWordBook("${addwordbook.text}","$nowtime"))

                    val myRef = database.getReference("users")

                    for(i in 0 until wbList.size){
                        myRef.child("$email").child("${wbList[i].date}").child("${wbList[i].title}").setValue("0")
                        adapter.addItem("${wbList[i].title}","${wbList[i].date}")
                    }


                }.setNegativeButton("취소") { dialogInterface, i ->

                }.show()
            }
        }
        return false
    }
}