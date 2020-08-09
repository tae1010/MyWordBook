package kr.ac.kpu.mywordbook

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.LinearLayout

class CheckableLinearLayout(context: Context,attributeSet: AttributeSet) : LinearLayout(context,attributeSet),Checkable {

    override fun isChecked(): Boolean {
        val checkBox = findViewById(R.id.word_check) as CheckBox
        return checkBox.isChecked
    }

    override fun toggle() {
        val checkBox = findViewById(R.id.word_check) as CheckBox
        isChecked = !checkBox.isChecked
    }

    override fun setChecked(checked: Boolean) {
        val checkBox = findViewById(R.id.word_check) as CheckBox
        if (checkBox.isChecked != checked) checkBox.isChecked = checked
    }
}