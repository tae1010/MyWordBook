package kr.ac.kpu.mywordbook

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.LinearLayout

class CheckableLinearLayout(context: Context,attributeSet: AttributeSet) : LinearLayout(context,attributeSet),Checkable {
    val checkBox = findViewById(R.id.word_check) as CheckBox
    override fun isChecked(): Boolean {

        return checkBox.isChecked
    }

    override fun toggle() {

        isChecked = !checkBox.isChecked
    }

    override fun setChecked(checked: Boolean) {
        if (checkBox.isChecked != checked) checkBox.isChecked = checked
    }
}