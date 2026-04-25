package pt.ipbeja.pi.piproject.listSavedInsects

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipbeja.pi.piproject.R

class PdfPreviewActivity : AppCompatActivity() {

    private var renderer: PdfRenderer? = null
    private var descriptor: ParcelFileDescriptor? = null
    private var currentPage: PdfRenderer.Page? = null
    private var currentIndex = 0

    private lateinit var imageView: ImageView
    private lateinit var pageLabel: TextView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_preview)

        imageView = findViewById(R.id.pdfImageView)
        pageLabel = findViewById(R.id.pageLabel)
        prevButton = findViewById(R.id.prevPageButton)
        nextButton = findViewById(R.id.nextPageButton)

        findViewById<Button>(R.id.closePdfButton).setOnClickListener { finish() }

        val uriString = intent.getStringExtra(EXTRA_PDF_URI)
        if (uriString.isNullOrBlank()) {
            Toast.makeText(this, "PDF inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        try {
            val pdfUri = Uri.parse(uriString)
            descriptor = contentResolver.openFileDescriptor(pdfUri, "r")
            val localDescriptor = descriptor
            if (localDescriptor == null) {
                Toast.makeText(this, "Não foi possível abrir o PDF", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            renderer = PdfRenderer(localDescriptor)
            if (renderer?.pageCount == 0) {
                Toast.makeText(this, "PDF sem páginas", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            prevButton.setOnClickListener {
                if (currentIndex > 0) {
                    showPage(currentIndex - 1)
                }
            }

            nextButton.setOnClickListener {
                val pageCount = renderer?.pageCount ?: 0
                if (currentIndex < pageCount - 1) {
                    showPage(currentIndex + 1)
                }
            }

            showPage(0)
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao abrir PDF", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showPage(index: Int) {
        val localRenderer = renderer ?: return
        currentPage?.close()
        currentPage = localRenderer.openPage(index)
        currentIndex = index

        val page = currentPage ?: return
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.WHITE)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        imageView.setImageBitmap(bitmap)

        val pageCount = localRenderer.pageCount
        pageLabel.text = "Página ${index + 1} / $pageCount"
        prevButton.isEnabled = index > 0
        nextButton.isEnabled = index < pageCount - 1
    }

    override fun onDestroy() {
        currentPage?.close()
        renderer?.close()
        descriptor?.close()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_PDF_URI = "extra_pdf_uri"
    }
}

