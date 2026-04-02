package pt.ipbeja.pi.piproject.credits

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pt.ipbeja.pi.piproject.R

/**
 * Activity to display zoomed Ce3c image.
 */
class Ce3cImageZoom : AppCompatActivity() {

    private lateinit var go: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ce3c_image_zoom)

        go = findViewById(R.id.button7)

        // Navigate back on button click
        go.setOnClickListener {
            finish()
        }
    }
}