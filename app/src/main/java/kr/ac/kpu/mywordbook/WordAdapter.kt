package kr.ac.kpu.mywordbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class WordAdapter : BaseAdapter(){


    private var wordList = ArrayList<ListWord>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var checkBoxPosition = position
        var view = convertView
        val context = parent!!.context

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.word_list, parent, false)
        }

        val egText = view!!.findViewById(R.id.egWord) as TextView
        val krText = view!!.findViewById(R.id.krWord) as TextView
        val cb = view!!.findViewById(R.id.word_check) as CheckBox

        //cb.setChecked((parent as ListView).isItemChecked(position))

        val wordListItem = wordList[position]
        var  listItem = ArrayList<Int>()

/*
        if(wordListItem != null){
            if(cb != null){
                cb.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                   override fun onCheckedChanged(buttonView : CompoundButton, isChecked: Boolean) {
                        if (isChecked) {
                            for (i in 0 until listItem.size) {
                                if (listItem.get(i) == checkBoxPosition)
                                    return
                            }
                            listItem.add(checkBoxPosition)
                        } else {
                            for (i in 0 until listItem.size) {
                                if (listItem.get(i) == checkBoxPosition) {
                                    listItem.remove(i)
                                    break
                                }
                            }
                        }
                    }
                })
                var a : Boolean = true
                for(i in 0 until listItem.size){
                    if(listItem.get(i) == checkBoxPosition){
                        cb.setChecked(false)
                        a = true
                        break
                    }
                }
                if(!a){
                    cb.setChecked(false)
                }
            }
        } */
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
    fun clearItem(){
        wordList.clear()
    }

}