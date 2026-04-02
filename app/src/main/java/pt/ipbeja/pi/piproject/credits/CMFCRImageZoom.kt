package pt.ipbeja.pi.piproject.credits

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pt.ipbeja.pi.piproject.R

/**
 * Activity to display zoomed CMFCR image.
 */
class CMFCRImageZoom : AppCompatActivity() {

    private val go: Button by lazy { findViewById(R.id.button7) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cmfcrimage_zoom)

        // Navigate back on button click
        go.setOnClickListener {
            finish()
        }
    }
}
