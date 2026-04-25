package pt.ipbeja.pi.piproject.listSavedInsects

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.graphics.text.LineBreaker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.launch
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.identificationInsect.MapsActivity
import pt.ipbeja.pi.piproject.idkey.IdentificationKey
import pt.ipbeja.pi.piproject.persistence.Identification
import pt.ipbeja.pi.piproject.persistence.MyIdentificationsDb
import pt.ipbeja.pi.piproject.util.Util

/**
 * Activity to list all saved identifications.
 */
class MyIdentifications : AppCompatActivity() {

    private data class TaxonomyInfo(
        val fullClassification: String,
        val order: String,
        val suborderOrGroup: String?
    )

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val db by lazy { MyIdentificationsDb.getDatabase(this) }
    private lateinit var adapter: IdentificationAdapter
    private var idents: List<Identification> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_identifications)


        loadList()

        findViewById<View>(R.id.button12).setOnClickListener { finish() }

        findViewById<View>(R.id.buttonMap).setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("showAll", true)
            startActivity(intent)
        }
    }

    private fun loadList() {
        lifecycleScope.launch {
            idents = db.identificationDao().getAll().reversed()
            adapter = IdentificationAdapter(this@MyIdentifications, idents)
            val identsLst = findViewById<ListView>(R.id.identsLst)
            identsLst.adapter = adapter
            setListener(identsLst)
        }
    }

    private fun setListener(insectsListView: ListView) {
        insectsListView.onItemClickListener = OnItemClickListener { _, view, position, _ ->
            val item = insectsListView.getItemAtPosition(position) as Identification
            view.setBackgroundColor(Color.LTGRAY)
            showPopupMenu(view, item)
        }
    }

    private fun showPopupMenu(
        view: View,
        identification: Identification
    ) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.myinsects_action_menu, popup.menu)
        popup.setOnDismissListener { view.setBackgroundColor(Color.TRANSPARENT) }
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.idmoreinfo -> {
                    showMoreInformation(identification)
                    true
                }
                R.id.idorder -> {
                    changeOrder(identification)
                    true
                }
                R.id.idsend -> {
                    sendEmailWithImageFromList(identification)
                    true
                }
                R.id.idpdf -> {
                    generatePdfForItem(identification)
                    true
                }
                R.id.iddelete -> {
                    deleteItem(identification)
                    true
                }
                R.id.idcancel -> true
                else -> false
            }
        }
        popup.show()
    }

    private fun updateOrder(identification: Identification, selectedClassification: String) {
        lifecycleScope.launch {
            try {
                val key = IdentificationKey.getInstance(this@MyIdentifications)
                val selectedResult = key?.getResultByOrder(selectedClassification)

                if (selectedResult != null) {
                    identification.keyId = selectedResult.id
                    identification.order = selectedResult.ordem
                } else {
                    // Fallback for unexpected values outside the loaded key.
                    identification.order = selectedClassification
                }

                db.identificationDao().update(identification)
                loadList()

                Toast.makeText(
                    this@MyIdentifications,
                    "${getString(R.string.order)} ${identification.order}",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(this@MyIdentifications, "Erro ao atualizar dados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeOrder(identification: Identification) {
        val classifications = try {
            IdentificationKey.getInstance(this)
                ?.getOrders()
                .orEmpty()
                .distinct()
                .sortedBy { it.lowercase(Locale.getDefault()) }
        } catch (e: Exception) {
            emptyList()
        }

        if (classifications.isEmpty()) {
            Toast.makeText(this, "Erro ao carregar ordens", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.correct_order)
        builder.setItems(classifications.toTypedArray()) { _, which ->
            updateOrder(identification, classifications[which])
        }
        builder.show()
    }

    private fun showMoreInformation(identification: Identification) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.moreinfotext)
        val resultId = identification.keyId
        try {
            val description = IdentificationKey.getInstance(this)?.getResult(identification.keyId)?.description ?: ""
            builder.setMessage(Util.removeSpaces(description))
            builder.setNeutralButton("Close") { _, _ -> }
            builder.setPositiveButton("+Info") { _, _ ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/0B1REHtVuyBybX1BRcGRrWnc5S28/view"))
                startActivity(browserIntent)
            }
            val alert = builder.create()
            alert.show()
            alert.findViewById<TextView>(android.R.id.message)?.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun deleteItem(identification: Identification) {
        lifecycleScope.launch {
            db.identificationDao().delete(identification)
            // Recarrega a lista para garantir que o item desaparece visualmente
            loadList()
            Toast.makeText(this@MyIdentifications, "Item eliminado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmailWithImageFromList(item: Identification) {
        val imageURI = Uri.parse(item.photoURI)
        val latitude = item.latitude
        val longitude = item.longitude
        val calendar = Calendar.getInstance().apply { time = item.timestamp }
        val timeAndDate = dateFormat.format(calendar.time)
        val taxonomy = resolveTaxonomy(item)
        val suborder = taxonomy.suborderOrGroup ?: getString(R.string.not_specified)
        val description = try {
            val rawDescription = IdentificationKey.getInstance(this)?.getResult(item.keyId)?.description.orEmpty()
            Util.removeSpaces(rawDescription).ifBlank { getString(R.string.not_specified) }
        } catch (e: Exception) {
            getString(R.string.not_specified)
        }

        val emailAddress = "insetosemordem2025@gmail.com "
        val emailSubject = "${taxonomy.order} / $suborder - $timeAndDate"
        val emailBody = """
            ${getString(R.string.emailBody)}
            ${getString(R.string.email_order_label)} ${taxonomy.order}
            ${getString(R.string.email_suborder_label)} $suborder
            ${getString(R.string.email_classification_label)} ${taxonomy.fullClassification}
            ${getString(R.string.email_description_label)} $description
            Latitude: $latitude Longitude: $longitude

            https://www.google.com/maps/search/?api=1&query=$latitude,$longitude
        """.trimIndent()

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "plain/text"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            putExtra(Intent.EXTRA_TEXT, emailBody)
            putExtra(Intent.EXTRA_STREAM, imageURI)
        }
        startActivity(Intent.createChooser(emailIntent, "Sending email..."))
    }

    private fun resolveTaxonomy(item: Identification): TaxonomyInfo {
        val fullClassification = try {
            IdentificationKey.getInstance(this)?.getResult(item.keyId)?.ordem?.trim().orEmpty()
        } catch (e: Exception) {
            ""
        }.ifBlank { item.order.trim() }

        val order = extractTaxonomyValue(fullClassification, Regex("""(?i)\\b(?:ordem|order)\\s+([\\p{L}-]+)"""))
            ?: item.order.trim()

        val suborderOrGroup = extractTaxonomyValue(fullClassification, Regex("""(?i)\\b(?:subordem|suborder)\\s+([\\p{L}-]+)"""))
            ?: extractTaxonomyValue(fullClassification, Regex("""(?i)\\b(?:grupo|group)\\s+([\\p{L}-]+)"""))

        return TaxonomyInfo(fullClassification, order, suborderOrGroup)
    }

    private fun extractTaxonomyValue(classification: String, pattern: Regex): String? {
        val match = pattern.find(classification) ?: return null
        return match.groupValues.getOrNull(1)?.trim().takeUnless { it.isNullOrEmpty() }
    }

    private fun generatePdfForItem(identification: Identification) {
        // Filtrar identificações com a mesma ordem
        val filteredIdents = idents.filter { it.order == identification.order }

        // Criação do documento PDF
        val pdfDocument = PdfDocument()

        // Para cada identificação filtrada, criar uma página no PDF
        for (ident in filteredIdents) {
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = pdfDocument.startPage(pageInfo)

            val canvas = page.canvas
            val paint = android.graphics.Paint()
            paint.color = android.graphics.Color.BLACK
            paint.textSize = 12f

            // Escreve os dados da identificação na página
            var yPos = 25f
            canvas.drawText("Ordem: ${ident.order}", 10f, yPos, paint)
            yPos += 25f
            canvas.drawText("Coordenadas: ${ident.dMSCoord}", 10f, yPos, paint)
            yPos += 25f
            canvas.drawText("Latitude: ${ident.latitude}, Longitude: ${ident.longitude}", 10f, yPos, paint)
            yPos += 25f
            canvas.drawText("Data: ${dateFormat.format(ident.timestamp)}", 10f, yPos, paint)
            yPos += 25f
            val description = IdentificationKey.getInstance(this)?.getResult(ident.keyId)?.description ?: ""
            val cleanDescription = Util.removeSpaces(description)
            yPos = drawMultilineText(canvas, paint, "Descrição: $cleanDescription", 10f, yPos, 580f)

            // Se houver foto, adiciona ao PDF
            if (ident.photoURI.isNotEmpty()) {
                try {
                    val bitmap = android.provider.MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(ident.photoURI))
                    val scaledBitmap = android.graphics.Bitmap.createScaledBitmap(bitmap, 580, 300, true)
                    canvas.drawBitmap(scaledBitmap, 10f, yPos, null)
                } catch (e: Exception) {
                    // Se não conseguir carregar a imagem, continua sem ela
                    e.printStackTrace()
                }
            }

            pdfDocument.finishPage(page)
        }

        // Especifica o caminho onde o PDF será salvo
        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Classification_${identification.order}_${System.currentTimeMillis()}.pdf")
        filePath.parentFile?.mkdirs()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Salvar usando MediaStore para Downloads
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, "Classification_${identification.order}_${System.currentTimeMillis()}.pdf")
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }
                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                uri?.let {
                    contentResolver.openOutputStream(it)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    contentValues.clear()
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                    contentResolver.update(it, contentValues, null, null)
                    Toast.makeText(this, "PDF guardado em Downloads", Toast.LENGTH_SHORT).show()
                    openGeneratedPdf(it)
                } ?: run {
                    Toast.makeText(this, "Erro ao criar arquivo", Toast.LENGTH_SHORT).show()
                }
            } else {
                pdfDocument.writeTo(FileOutputStream(filePath))
                Toast.makeText(this, "PDF guardado", Toast.LENGTH_SHORT).show()
                val fileUri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    filePath
                )
                openGeneratedPdf(fileUri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erro ao guardar PDF", Toast.LENGTH_SHORT).show()
        }

        // Fecha o documento PDF
        pdfDocument.close()
    }

    private fun drawMultilineText(canvas: android.graphics.Canvas, paint: android.graphics.Paint, text: String, x: Float, startY: Float, maxWidth: Float): Float {
        var currentY = startY
        val lines = text.split("\n")
        for (line in lines) {
            val words = line.split(" ")
            var currentLine = ""
            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                if (paint.measureText(testLine) <= maxWidth) {
                    currentLine = testLine
                } else {
                    canvas.drawText(currentLine, x, currentY, paint)
                    currentY += paint.textSize + 5
                    currentLine = word
                }
            }
            if (currentLine.isNotEmpty()) {
                canvas.drawText(currentLine, x, currentY, paint)
                currentY += paint.textSize + 5
            }
        }
        return currentY
    }

    private fun openGeneratedPdf(pdfUri: Uri) {
        val previewIntent = Intent(this, PdfPreviewActivity::class.java).apply {
            putExtra(PdfPreviewActivity.EXTRA_PDF_URI, pdfUri.toString())
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(previewIntent)
    }
}
