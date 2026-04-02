package pt.ipbeja.pi.piproject.identificationInsect

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.idkey.IdentificationKey
import pt.ipbeja.pi.piproject.idkey.QuestionNode
import java.io.IOException

/**
 * Classification activity
 * Handles the step-by-step identification process using the key.
 */
class Classificacao : AppCompatActivity() {

    private var currentFragmentID: String? = null
    private var optionAEndpoint: String? = null
    private var optionBEndpoint: String? = null

    private lateinit var node: QuestionNode
    private lateinit var go: Button
    private lateinit var go2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classificacao)

        val intent = intent
        currentFragmentID = intent.getStringExtra("fragmentID")

        go = findViewById(R.id.button7)
        go.setOnClickListener {
            val backIntent = Intent()
            backIntent.putExtra("fragmentID", true)
            setResult(RESULT_OK, backIntent)
            finish()
        }

        go2 = findViewById(R.id.button3)
        go2.setOnClickListener {
            val backHome = Intent()
            backHome.putExtra("finish", true)
            setResult(RESULT_OK, backHome)
            finish()
        }

        try {
            val key = IdentificationKey.getInstance(applicationContext)
            if (key != null) {
                node = currentFragmentID?.let { key.getQuestion(it) }!!

                val txt = findViewById<TextView>(R.id.txt)
                txt.text = node.question

                val btnA = findViewById<Button>(R.id.optionA)
                btnA.text = node.optionA.text

                val btnB = findViewById<Button>(R.id.optionB)
                btnB.text = node.optionB.text

                this.optionAEndpoint = node.optionA.gotoId
                this.optionBEndpoint = node.optionB.gotoId

                val imgA = findViewById<ImageView>(R.id.imageOptionA)
                val imgB = findViewById<ImageView>(R.id.imageOptionB)

                this.setImage(imgA, node.optionA.imageLocation)
                this.setImage(imgB, node.optionB.imageLocation)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun goToNextQuestion(newFragmentID: String?) {
        try {
            val key = IdentificationKey.getInstance(applicationContext)
            if (key != null && newFragmentID != null) {
                if (key.isQuestion(newFragmentID)) {
                    val intent = Intent(this, Classificacao::class.java)
                    intent.putExtra("fragmentID", newFragmentID)
                    startActivityForResult(intent, NEXT_STEP)
                } else {
                    val node = key.getResult(newFragmentID)
                    val order = node?.id

                    val resultActivityIntent = Intent(this, ShowResult::class.java)
                    resultActivityIntent.putExtra("fragmentID", order)
                    startActivityForResult(resultActivityIntent, NEXT_STEP)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun onOptionAClick(view: View?) {
        goToNextQuestion(this.optionAEndpoint)
    }

    fun onOptionBClick(view: View?) {
        goToNextQuestion(this.optionBEndpoint)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEXT_STEP && resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra("finish", false)) {
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }

    fun onImageAClick(view: View?) {
        val intent = Intent(this, ImageZoom::class.java)
        intent.putExtra("optionImage", node.optionA.imageLocation)
        this.startActivity(intent)
    }

    fun onImageBClick(view: View?) {
        val intent = Intent(this, ImageZoom::class.java)
        intent.putExtra("optionImage", node.optionB.imageLocation)
        this.startActivity(intent)
    }

    private fun setImage(image: ImageView, imagePath: String) {
        try {
            assets.open(imagePath).use { ims ->
                val bitmap = BitmapFactory.decodeStream(ims)
                image.setImageBitmap(bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val NEXT_STEP = 1
    }
}