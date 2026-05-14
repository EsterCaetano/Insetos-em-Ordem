package pt.ipbeja.pi.piproject.identificationInsect

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.persistence.Identification
import pt.ipbeja.pi.piproject.persistence.MyIdentificationsDb
import pt.ipbeja.pi.piproject.startUpApp.MainActivity
import pt.ipbeja.pi.piproject.util.Coordinates
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Activity para guardar uma identificacao na base de dados.
 * Permite tirar foto, selecionar localizacao no mapa, e guardar toda a informacao (foto, ordem, coordenadas, data).
 * A localizacao inicial e obtida automaticamente via GPS se permissao concedida.
 */
class SaveIdentification : AppCompatActivity() {

    private var resultId: String? = null
    private var order: String? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var timestamp: Date? = null
    private var currentPhotoURI: Uri? = null
    private var hasReturnedFromMap = false // Flag to check if we returned from map

    private lateinit var maps: Button
    private lateinit var myIdsButton: Button
    private lateinit var img: ImageView

    private val db by lazy { MyIdentificationsDb.getDatabase(this) }
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val requestLocationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            updateLocation()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            img.setImageURI(currentPhotoURI)
            updateSaveButtonState() // Check if we can enable the button
        }
    }

    private val selectPictureLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            currentPhotoURI = it
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            img.setImageURI(currentPhotoURI)
            updateSaveButtonState() // Check if we can enable the button
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Inicializa a Activity: extrai dados do resultado, verifica permissões de localizacao,
         * configura launchers para camera e galeria, e carrega coordenadas do GPS.
         * @param savedInstanceState Estado anterior da Activity (se houver).
         */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_identification)

        resultId = intent.getStringExtra("fragmentID")
        order = intent.getStringExtra("order")

        findViewById<TextView>(R.id.orderTxt).text = order

        timestamp = Date()
        findViewById<TextView>(R.id.timestampTxt).text = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault()).format(timestamp!!)

        img = findViewById(R.id.pictureView)
        myIdsButton = findViewById(R.id.saveButton)
        myIdsButton.isEnabled = false // Disable by default

        checkLocationPermission()

        maps = findViewById(R.id.maps)
        maps.setOnClickListener {
            val intent = Intent(this@SaveIdentification, MapsActivity::class.java).apply {
                putExtra("picture", currentPhotoURI)
                putExtra("resultid", resultId)
                putExtra("ordem", order)
                putExtra("latitude2", latitude)
                putExtra("longitude2", longitude)
            }
            startActivity(intent)
        }

        checkNewCoordinates()
    }

    /**
     * Verifica se a permissão de localizacao foi concedida.
     * Se sim, obtém localização imediata; se não, pede permissão.
     */
    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                updateLocation()
            }
            else -> {
                requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    /**
     * Obtém a última localização conhecida e atualiza as coordenadas no ecrã.
     */
    private fun updateLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                findViewById<TextView>(R.id.coordsTxt).text = Coordinates.anglesToDMS(latitude, longitude)
                // DO NOT update save button state here.
            }
        }
    }

    /**
     * Ativa/desativa o botão "Guardar" baseado na disponibilidade de foto e localizacao.
     * Botao so ativado se foto foi tirada/selecionada E utilizador retornou do mapa.
     */
    private fun updateSaveButtonState() {
        myIdsButton.isEnabled = currentPhotoURI != null && hasReturnedFromMap
    }

    /**
     * Handler para botao "Guardar": cria Identification com todos dados e insere na BD.
     * Regressa ao menu principal apos sucesso.
     * @param view O botao clicado.
     */
    fun onSaveClick(view: View?) {
        lifecycleScope.launch {
            val ident = Identification(
                0,
                resultId ?: "",
                order ?: "",
                latitude,
                longitude,
                timestamp ?: Date(),
                currentPhotoURI.toString()
            )
            db.identificationDao().insert(ident)

            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }
    }

    /**
     * Handler para botao "Tirar foto": abre camera para capturar foto.
     * @param view O botao clicado.
     */
    fun onClickTakePicture(view: View?) {
        try {
            val photoFile = createTmpImageFile()
            val photoURI = FileProvider.getUriForFile(this, "pt.ipbeja.pi.piproject.fileprovider", photoFile)
            currentPhotoURI = photoURI
            takePictureLauncher.launch(photoURI)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    /**
     * Handler para botao "Carregar foto": abre galeria para selecionar imagem.
     * @param view O botao clicado.
     */
    fun onClickLoadPicture(view: View?) {
        selectPictureLauncher.launch(arrayOf("image/*"))
    }

    @Throws(IOException::class)
    /**
     * Cria ficheiro temporario para guardar a foto tirada pela camera.
     * @return Ficheiro criado com nome unico (timestamp + ".jpg").
     * @throws IOException Se erro ao criar ficheiro.
     */
    private fun createTmpImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    /**
     * Verifica se regressou do mapa com novas coordenadas.
     * Se sim, atualiza latitude/longitude e habilita botao guardar.
     */
    private fun checkNewCoordinates() {
        val extras = intent.extras ?: return
        if (extras.getInt("extra1") == 1) {
            hasReturnedFromMap = true // User has returned from map
            latitude = extras.getDouble("novaLatitude")
            longitude = extras.getDouble("novaLongitude")
            currentPhotoURI = extras.getParcelable("picturefinal")
            order = extras.getString("ordemfinal")
            resultId = extras.getString("resultidfinal")

            findViewById<TextView>(R.id.coordsTxt).text = Coordinates.anglesToDMS(latitude, longitude)
            findViewById<TextView>(R.id.orderTxt).text = order
            img.setImageURI(currentPhotoURI)

            updateSaveButtonState() // Check if we can enable the button
        }
    }
}
