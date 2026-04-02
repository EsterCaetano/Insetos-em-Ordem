package pt.ipbeja.pi.piproject.credits

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pt.ipbeja.pi.piproject.R

/**
 * Activity to display the Privacy Policy information.
 */
class PrivacyPolicyInfo : AppCompatActivity() {

    private lateinit var go: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy_info)

        go = findViewById(R.id.button11)

        // Navigate back on button click
        go.setOnClickListener {
            finish()
        }

        // Load and display the Privacy Policy HTML content
        val htmlAsString = getString(R.string.Privacy_Policy_INFO)
        val webView = findViewById<WebView>(R.id.webView)
        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null)
    }
}