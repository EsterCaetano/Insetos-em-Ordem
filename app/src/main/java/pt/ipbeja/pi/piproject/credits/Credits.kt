package pt.ipbeja.pi.piproject.credits

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pt.ipbeja.pi.piproject.R

/**
 * Credits activity
 * Displays the credits screen with buttons to navigate to other info screens.
 */
class Credits : AppCompatActivity() {

    // Buttons for navigation
    private lateinit var go: Button
    private lateinit var go2: Button
    private lateinit var go3: Button
    private lateinit var go4: Button
    private lateinit var go5: Button
    private lateinit var go6: Button
    private lateinit var go7: Button
    private lateinit var go8: Button
    private lateinit var go9: Button
    private lateinit var go10: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)

        // Initialize buttons
        go = findViewById(R.id.button13)
        go2 = findViewById(R.id.button_privacy_policy)
        go3 = findViewById(R.id.button9)
        go4 = findViewById(R.id.button_ip_beja)
        go5 = findViewById(R.id.button_targis)
        go6 = findViewById(R.id.button_pca)
        go7 = findViewById(R.id.button_muhnac)
        go8 = findViewById(R.id.button_ce3c)
        go9 = findViewById(R.id.button_cmfcr)
        go10 = findViewById(R.id.button_leiden)

        // Set click listeners for navigation
        go.setOnClickListener { finish() } // Back button

        go2.setOnClickListener {
            val intent = Intent(this@Credits, PrivacyPolicyInfo::class.java)
            startActivity(intent)
        }

        go3.setOnClickListener {
            startActivity(Intent(this@Credits, Info::class.java))
        }

        go4.setOnClickListener {
            startActivity(Intent(this@Credits, IPBejaImageZoom::class.java))
        }

        go5.setOnClickListener {
            startActivity(Intent(this@Credits, TagisImageZoom::class.java))
        }

        go6.setOnClickListener {
            startActivity(Intent(this@Credits, PCAImageZoom::class.java))
        }

        go7.setOnClickListener {
            startActivity(Intent(this@Credits, MuhnacImageZoom::class.java))
        }

        go8.setOnClickListener {
            startActivity(Intent(this@Credits, Ce3cImageZoom::class.java))
        }

        go9.setOnClickListener {
            startActivity(Intent(this@Credits, CMFCRImageZoom::class.java))
        }

        go10.setOnClickListener {
            startActivity(Intent(this@Credits, LeidenImageZoom::class.java))
        }
    }
}