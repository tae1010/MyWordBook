package kr.ac.kpu.mywordbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class WordAdapter : BaseAdapter(){
    private var wordList = ArrayList<ListWord>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val context = parent!!.context

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.word_list, parent, false)
        }

        val egText = view!!.findViewById(R.id.egWord) as TextView
        val krText = view!!.findViewById(R.id.krWord) as TextView

        val wordListItem = wordList[position]

        egText.setText(wordListItem.egWord)
        krText.setText(wordListItem.krWord)

        return view

    }

    override fun getCount(): Int {
        return wordList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return wordList[position]
    }
    fun addItem(egWord: String,krWord: String) {
        val item = ListWord(egWord,krWord)

        item.egWord = egWord
        item.krWord = krWord

        wordList.add(item)
    }
}