package pt.ipbeja.pi.piproject.credits

import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pt.ipbeja.pi.piproject.R

/**
 * Info activity
 * Displays general information about the application in a WebView.
 */
class Info : AppCompatActivity() {

    private lateinit var go: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        go = findViewById(R.id.button14)

        go.setOnClickListener {
            finish() // Navigate back to previous screen
        }

        // Load and display HTML content
        val htmlAsString = getString(R.string.Info_About_APP)
        val webView = findViewById<WebView>(R.id.webView)
        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null)
    }
}