package kr.ac.kpu.mywordbook

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_picture_text.*


class PictureTextActivity : AppCompatActivity() {
    lateinit var ocrImage: ImageView
    lateinit var resultEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultEditText = ocrResultEt
        ocrImage = ocrImageView

        //set an onclick listener on the button to trigger///// the @pickImage() method
        selectImageBtn.setOnClickListener {
            pickImage()
        }

        //set an onclick listener on the button to trigger the @processImage method
        processImageBtn.setOnClickListener {
            processImage(processImageBtn)
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
        }
    }
    fun processImage(v: View) {
        if (ocrImage.drawable != null) {
            resultEditText.setText("")
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
                    resultEditText.setText("Failed")
                }
        } else {
            Toast.makeText(this, "Select an Image First", Toast.LENGTH_LONG).show()
        }

    }
    private fun processTextBlock(result: FirebaseVisionText) {
        var i:Int=0
        val resultText=result.text
        if (result.textBlocks.size == 0) {
            resultEditText.setText("No Text Found")
            return
        }

        //translate start
        val options= FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(FirebaseTranslateLanguage.KO)
            .build()
        val englishKoreanTranslator= FirebaseNaturalLanguage.getInstance().getTranslator(options)
        //model download
        englishKoreanTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
            }
        //translate end
        for(block in result.textBlocks){

            val blockText=block.text
            for(line in block.lines){
                val lineText=line.text
                for(element in line.elements){
                    i++
                    val elementText=element.text
                    englishKoreanTranslator.translate(elementText)
                        .addOnSuccessListener{translatedText->
                            resultEditText.append(translatedText+"\n")
                        }
                        .addOnFailureListener {
                            resultEditText.append(elementText+"\n")
                        }
                    //resultEditText.append(elementText+"\n")
                }
            }
        }
    }
}