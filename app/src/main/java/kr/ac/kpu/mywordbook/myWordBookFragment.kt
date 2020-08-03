package kr.ac.kpu.mywordbook

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragmywordbook.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


var wbList = arrayListOf<ListWordBook>()

class myWordBookFragment : Fragment() {

    lateinit var listview : ListView
    lateinit var adapter : WordBookAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmywordbook, container,false)

        setHasOptionsMenu(true)

        adapter = WordBookAdapter()
        listview = view!!.findViewById(R.id.lv_wordbook) as ListView
        listview.adapter = adapter

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
        val nowtime = current.format(formatter)

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

                    for(i in 0 until wbList.size){
                        adapter.addItem("${wbList[i].title}","${wbList[i].date}")
                    }


                }.setNegativeButton("취소") { dialogInterface, i ->

                }.show()
            }
        }
        return false
    }
}