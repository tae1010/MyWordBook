package kr.ac.kpu.mywordbook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_picture_text.*


class PictureTextActivity : AppCompatActivity() {


    lateinit var ocrImage: ImageView
    lateinit var listview: ListView
    lateinit var adapter: WordAdapter

    val database = Firebase.database
    var transWordList = ArrayList<ListWord>()
    var transWordList1 = ArrayList<ListWord>()

    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_text)


        ocrImage = ocrImageView

        //set an onclick listener on the button to trigger///// the @pickImage() method
        selectImageBtn.setOnClickListener {
            pickImage()
        }

        //set an onclick listener on the button to trigger the @processImage method
        processImageBtn.setOnClickListener {
            processImage(processImageBtn)
        }

        camera.setOnClickListener {
            if(isPermitted(CAMERA_PERMISSION)) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this,CAMERA_PERMISSION,2)
            }
        }
    }
    fun isPermitted(permissions : Array<String>) : Boolean{
        for(permission in permissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,3)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       when(requestCode){
           2 ->{
               var checked = true
               for(grant in grantResults){
                   if(grant != PackageManager.PERMISSION_GRANTED){
                       checked = false
                       break
                   }
               }
               if(checked){
                   openCamera()
               }
           }
       }
    }

    fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            ocrImage.setImageURI(data!!.data)   //ocrImage에 select한 이미지의 uri저장
        } else if(requestCode == 3 && resultCode == Activity.RESULT_OK){
            val bitmap = data?.extras?.get("data") as Bitmap
            ocrImageView.setImageBitmap(bitmap)
        }
    }

    fun processImage(v: View) {
        if (ocrImage.drawable != null) {
            //resultEditText.setText("")
            v.isEnabled = false
            val bitmap = (ocrImage.drawable as BitmapDrawable).bitmap
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

            detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    v.isEnabled = true
                    processTextBlock(firebaseVisionText)
                }
                .addOnFailureListener {
                    v.isEnabled = true
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    //resultEditText.setText("Failed")
                }
        } else {
            Toast.makeText(this, "Select an Image First", Toast.LENGTH_LONG).show()
        }

    }

    private fun processTextBlock(result: FirebaseVisionText) {

        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val email = intent.getStringExtra("email")

        adapter = WordAdapter()
        listview = findViewById(R.id.ocrResultLv)
        listview.adapter = adapter

        val checkedItems = listview.checkedItemPositions
        val count = adapter.count

        var i: Int = 0
        val resultText = result.text
        if (result.textBlocks.size == 0) {
            Toast.makeText(this, "No Text Found", Toast.LENGTH_SHORT).show()
            //resultEditText.setText("No Text Found")
            return
        }

        //translate start
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(FirebaseTranslateLanguage.KO)
            .build()
        val englishKoreanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
        //model download
        englishKoreanTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
            }
        //translate end
        for (block in result.textBlocks) {
            val blockText = block.text
            for (line in block.lines) {
                val lineText = line.text
                for (element in line.elements) {
                    val elementText = element.text
                    englishKoreanTranslator.translate(elementText)
                        .addOnSuccessListener { translatedText ->
                            transWordList.add(ListWord("$elementText", "$translatedText"))
                            //adapter.addItem("$elementText", "$translatedText")
                        }
                        .addOnFailureListener {
                            //resultEditText.append(elementText+"\n")
                            transWordList.add(ListWord("$elementText", "$elementText"))
                        }
                    //Toast.makeText(this,"${transWordList.size}",Toast.LENGTH_SHORT).show()
                    //adapter.addItem("${transWordList}", "$translatedText")
                }
            }
        }

        for (i in 0 until transWordList.size) {
            adapter.addItem("${transWordList[i].egWord}", "${transWordList[i].krWord}")
            adapter.notifyDataSetChanged()
            //Toast.makeText(this,"가가가",Toast.LENGTH_SHORT).show()
        }

        pt_add.setOnClickListener {
            //adapter.clearItem().
            for (i in 0 until transWordList.size) {
               if (checkedItems.get(i)) {
                    //transWordList.removeAt(i)
                    transWordList1.add(
                        ListWord("${transWordList[i].egWord}", "${transWordList[i].krWord}"))
                }
            }
            /*for (i in 0 until transWordList.size) {
                adapter.addItem("${transWordList[i].egWord}", "${transWordList[i].krWord}")
            }*/
            listview.clearChoices()
            //Toast.makeText(this, "${transWordList1.size} ${transWordList.size}", Toast.LENGTH_SHORT).show()
            //adapter.notifyDataSetChanged()
            for (i in 0 until transWordList1.size) {
                val myRef = database.getReference("users/$email/$date/$title")
                myRef.child("${transWordList1[i].egWord}").setValue("${transWordList1[i].krWord}")
            }
            transWordList1.clear()
            finish()
            val intent = Intent(this,WordBookActivity::class.java)
            intent.putExtra("email",email)
            intent.putExtra("title", title)
            intent.putExtra("date", date)
            startActivity(intent)
        }
    }
}
