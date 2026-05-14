package pt.ipbeja.pi.piproject.identificationInsect

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.idkey.KeyOption
import java.io.IOException

/**
 * Dialog to display more information about an identification option.
 * Exibe descricao detalhada de uma opcao e a sua imagem associada.
 * Permite clique na imagem para abrir zoom.
 * Created by Vicenzo on 01-02-18.
 */
class MoreInfoPopupDialog(private val activity: Activity, private val option: KeyOption) : Dialog(activity) {

    private var close: Button? = null

    /**
     * Inicializa o dialogo: carrega dados da opcao (descricao e imagem),
     * configura listeners para fechar e para abrir zoom da imagem.
     * @param savedInstanceState Estado anterior do dialogo (se houver).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_moreinfopopup)

        close = findViewById(R.id.closeBtn)
        close?.setOnClickListener { dismiss() }

        val image = findViewById<ImageView>(R.id.imgView)
        val descText = findViewById<TextView>(R.id.descLbl)

        descText.text = option.description

        try {
            activity.assets.open(option.imageLocation).use { ims ->
                val bitmap = BitmapFactory.decodeStream(ims)
                image.setImageBitmap(bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        image.setOnClickListener {
            val intent = Intent(activity, ImageZoom::class.java).apply {
                putExtra("optionImage", option.imageLocation)
            }
            activity.startActivity(intent)
        }
    }
}