package pt.ipbeja.pi.piproject.identificationInsect

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import pt.ipbeja.pi.piproject.R
import java.io.IOException

/**
 * Activity to display a zoomed version of an image using PhotoView.
 * Permite ao utilizador fazer zoom, pan e rotacao da imagem com gestos táteis.
 */
class ImageZoom : AppCompatActivity() {

    private lateinit var go: Button

    /**
     * Inicializa a Activity: carrega a imagem dos assets (se fornecido no intent)
     * e configura o botao para regressar a tela anterior.
     * A imagem é manipulavel com zoom, pan e rotacao via PhotoView.
     * @param savedInstanceState Estado anterior da Activity (se houver).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_zoom)

        val optionImage = intent.getStringExtra("optionImage")
        val photoView = findViewById<PhotoView>(R.id.zoom_photo)

        // Load image from assets if the path is provided
        if (optionImage != null) {
            try {
                assets.open(optionImage).use { imageStream ->
                    val bitmap = BitmapFactory.decodeStream(imageStream)
                    photoView.setImageBitmap(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        go = findViewById(R.id.button10)

        // Return to previous screen with a result
        go.setOnClickListener {
            val backIntent = Intent()
            backIntent.putExtra("fragmentID", true)
            setResult(RESULT_OK, backIntent)
            finish()
        }
    }
}