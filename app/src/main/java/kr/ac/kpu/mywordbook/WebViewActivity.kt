package kr.ac.kpu.mywordbook

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragsearch.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share
/*
사전을 이용한 단어검색
 */
class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        registerForContextMenu(webView)
        //registerForContextMenu() 메서드 컨텍스트 메뉴를 표시할 뷰로 웹 뷰를지정 앱을실행한 후 해당 웹 뷰를 롱클릭하면 컨텍스트 메뉴가 표시됨

        webView.apply{ // 웹뷰 기본설정 : 아래 두가지는 반드시 설정
            settings.javaScriptEnabled = true // 자바스크립트를 동작시키도록 기능 활성화
            webViewClient = WebViewClient() // webviewClient를 클래스로 지정하지 않으면 웹뷰가 아니고 자체 웹 브라우저가 동작하게 됨
        }
        webView.loadUrl("https://dict.naver.com/")
        // 뷰,액션ID, 이벤트 // 사용하지않을시 _로 표기
    }

    override fun onBackPressed() {  // onBackPressed() 메소드를 오버라이드
        if(webView.canGoBack()){  // 웹 뷰가 이전페이지로 갈수 있다면
            webView.goBack() // 이전 페이지로 이동하고
        }else{
            super.onBackPressed() // 그럴 수 없다면 원래 동작을 수행, 즉 종료
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu) // menuinflater 객체의 inflate()메서드를 사용하여 리소스 지정
        return true // true를 반환하면 액티비티에 메뉴가 있음을 알림
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){ // 메뉴 아이템으로 분기
            R.id.action_naver,R.id.action_home ->{  // 돋보기아이콘 클릭시 네이버사전이동
                webView.loadUrl("https://en.dict.naver.com/#/main")
                return true
            }
            R.id.action_daum -> {
                webView.loadUrl("https://dic.daum.net/index.do?dic=eng&q=")
                return true
            }
            R.id.action_google -> {
                webView.loadUrl("https://www.google.com/search?q=google+translate&oq=google+translate&aqs=chrome.0.69i59j0l7.4170j0j9&sourceid=chrome&ie=UTF-8")
                return true
            }

            R.id.action_email -> {
                email("sanki@kpu.co.kr","kpu", webView.url.toString())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context,menu)// menuInflater.inflate()메소드로 메뉴 리소스를 컨텍스트 메뉴로 사용
    }
/*
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_share -> {
                share(webView.url.toString())//페이지공유
            }
            R.id.action_browser -> {
                browse(webView.url.toString())//기본 웹 브라우저에서 열기
            }
        }
        return super.onContextItemSelected(item)
    }
*/

}