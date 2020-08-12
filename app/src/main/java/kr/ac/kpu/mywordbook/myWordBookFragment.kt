package kr.ac.kpu.mywordbook

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.service.autofill.Dataset
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.size
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
var wList = arrayListOf<ListWord>()

class myWordBookFragment : Fragment() {

    val database = Firebase.database

    lateinit var listview: ListView
    lateinit var adapter: WordBookAdapter

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater : MenuInflater = activity!!.menuInflater
        inflater.inflate(R.menu.menu1,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        adapter = WordBookAdapter()
        listview = view!!.findViewById(R.id.lv_wordbook) as ListView
        listview.adapter = adapter

        val email = activity!!.intent.getStringExtra("email")

        val myRef = database.getReference("users")

        val myRef2 = database.getReference("users/share")

        val myRef3 = database.getReference("users/$email/${wbList[info.position].date}/${wbList[info.position].title}")


        return when(item.itemId){
            R.id.share ->{

                //myRef2.child("$email").child("${wbList[info.position].title}")

                myRef3.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (snapshot in p0.children) {
                            wList.add(ListWord(snapshot.key,snapshot.value.toString()))
                        }
                        //Toast.makeText(activity,"${wList.size}",Toast.LENGTH_SHORT).show()
                        for(i in 0 until wList.size) {
                            myRef2.child("$email").child("${wbList[info.position].title}")
                                .child("${wList[i].egWord}").setValue("${wList[i].krWord}")
                        }
                        //Toast.makeText(activity,"${wList.size}",Toast.LENGTH_SHORT).show()
                    }
                })

                for(i in 0 until wbList.size){
                    adapter.addItem("${wbList[i].title}","${wbList[i].date}")
                }
                Toast.makeText(activity,"공유되었습니다.",Toast.LENGTH_SHORT).show()

                //Toast.makeText(activity,"rksk",Toast.LENGTH_SHORT).show()
                //Toast.makeText(activity,"${wList.size}",Toast.LENGTH_SHORT).show()
                true
            }

            R.id.delete ->{

                for (i in 0 until wbList.size) {
                    if(wbList[info.position].date == "${wbList[i].date}") {
                        myRef.child("$email").child("${wbList[i].date}").removeValue()
                        //Toast.makeText(activity, "${info.position}  $i", Toast.LENGTH_SHORT).show()
                    }
                }
                wbList.removeAt(info.position)
                adapter.notifyDataSetChanged()

                for(i in 0 until wbList.size){
                    adapter.addItem("${wbList[i].title}","${wbList[i].date}")
                }
                Toast.makeText(activity,"삭제되었습니다.",Toast.LENGTH_SHORT).show()

                true
            }


            else -> super.onContextItemSelected(item)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragmywordbook, container, false)

        setHasOptionsMenu(true)


        listview = view!!.findViewById(R.id.lv_wordbook) as ListView


        registerForContextMenu(listview)


        //adapter.notifyDataSetChanged() //삭제하거나 추가할때 리스트뷰 갱신

        //Toast.makeText(activity,"$email",Toast.LENGTH_SHORT).show()
        val email = activity!!.intent.getStringExtra("email")
        val myRef1 = database.getReference("users/$email")

        val intent2 = Intent(activity, SharedFragment::class.java)
        intent2.putExtra("email2",email)
        //Toast.makeText(activity, "$email", Toast.LENGTH_SHORT).show()

        myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {



                for (snapshot in p0.children) {
                    val myRef2 = database.getReference("users/$email/${snapshot.key}")
                    myRef2.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            adapter = WordBookAdapter()
                            listview.adapter = adapter

                            for (snapshot in p0.children) {
                                wbList.add(ListWordBook("${snapshot.key.toString()}", "${myRef2.key.toString()}"))
                                //Toast.makeText(activity,"${snapshot.key.toString()}",Toast.LENGTH_SHORT).show()
                            }

                            for (i in 0 until wbList.size) {
                                adapter.addItem("${wbList[i].title}", "${wbList[i].date}")
                                Log.d(TAG, "###############################")
                            }
                        }
                    })
                }
            }
        })



        listview.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(activity, WordBookActivity::class.java)
                intent.putExtra("email",email)
                intent.putExtra("title", wbList[position].title)
                intent.putExtra("date", wbList[position].date)
                startActivity(intent)
            }


        wbList.clear()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newmemo -> {
                val email = activity!!.intent.getStringExtra("email")
                val dlgView = layoutInflater.inflate(R.layout.wordbookname, null)
                val addwordbook: EditText = dlgView.findViewById(R.id.ed_name)
                val dlgBuilder = AlertDialog.Builder(activity)
                dlgBuilder.setView(dlgView)

                dlgBuilder.setPositiveButton("추가") { dialogInterface, i ->

                    adapter = WordBookAdapter()

                    listview = view!!.findViewById(R.id.lv_wordbook) as ListView
                    listview.adapter = adapter

                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
                    val nowtime = current.format(formatter)

                    wbList.add(ListWordBook("${addwordbook.text}", "$nowtime"))

                    val myRef = database.getReference("users")

                    //for (i in wbList.size-1 until wbList.size) {
                    //    myRef.child("$email").child("${wbList[i].date}").child("${wbList[i].title}").setValue("0")
                    //    //myRef.child("share").child("$email").child("${wbList[i].title}").setValue("0")
                    //    adapter.addItem("${wbList[i].title}", "${wbList[i].date}")
                    //}
                        myRef.child("$email").child("${wbList[wbList.size-1].date}").child("${wbList[wbList.size-1].title}").setValue("0")
                        //myRef.child("share").child("$email").child("${wbList[i].title}").setValue("0")
                    for(i in 0 until wbList.size){
                        adapter.addItem("${wbList[i].title}", "${wbList[i].date}")
                    }


                }.setNegativeButton("취소") { dialogInterface, i ->

                }.show()
            }
        }
        return false
    }



}