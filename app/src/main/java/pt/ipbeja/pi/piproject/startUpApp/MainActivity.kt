package pt.ipbeja.pi.piproject.startUpApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.credits.Credits
import pt.ipbeja.pi.piproject.identificationInsect.Classificacao
import pt.ipbeja.pi.piproject.listSavedInsects.MyIdentifications
import pt.ipbeja.pi.piproject.persistence.MyIdentificationsDb

class MainActivity : AppCompatActivity() {

    private val db by lazy { MyIdentificationsDb.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        setInitialButtonState()
    }

    private fun setInitialButtonState() {
        lifecycleScope.launch {
            val idCount = db.identificationDao().count()
            val myIdsButton = findViewById<Button>(R.id.button)
            myIdsButton.isEnabled = idCount > 0
        }
    }

    fun onClickToClassification(view: View?) {
        val intent = Intent(this, Classificacao::class.java)
        intent.putExtra("fragmentID", "Q1")
        startActivity(intent)
    }

    fun onClickToMyInsects(view: View?) {
        val intent = Intent(this, MyIdentifications::class.java)
        startActivity(intent)
    }

    fun onClickToCredits(view: View?) {
        val intent = Intent(this, Credits::class.java)
        startActivity(intent)
    }
}
