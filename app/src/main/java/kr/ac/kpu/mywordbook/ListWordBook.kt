package kr.ac.kpu.mywordbook

class ListWordBook (var title : String?, var date: String?){
    var text : String? = null
    constructor(title : String?, date : String?, text : String?) : this(title, date){
        this.title = title
        this.date = date
        this.text = text
    }
}