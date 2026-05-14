package pt.ipbeja.pi.piproject.identificationInsect

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.idkey.IdentificationKey
import pt.ipbeja.pi.piproject.util.Util
import java.io.IOException

/**
 * Activity to display the identification result.
 * Apresenta a ordem identificada, descricao e imagem do resultado (final) da chave dicotomica.
 */
class ShowResult : AppCompatActivity() {

    private var resultKey: String? = null
    private var ordem: String? = null

    private lateinit var go: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Inicializa a Activity: carrega o resultado baseado no intent,
         * exibe ordem, descricao (scrollavel) e imagem do resultado.
         * Configura botoes para guardar identificacao ou terminar.
         * @param savedInstanceState Estado anterior da Activity (se houver).
         */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_result)

        resultKey = intent.getStringExtra("fragmentID")

        try {
            val idKey = IdentificationKey.getInstance(applicationContext)
            val result = resultKey?.let { idKey?.getResult(it) }

            if (result != null) {
                ordem = result.ordem
                findViewById<TextView>(R.id.orderLbl).text = ordem

                val descTxt = findViewById<TextView>(R.id.descLbl)
                descTxt.movementMethod = ScrollingMovementMethod()
                descTxt.text = Util.removeSpaces(result.description)

                assets.open(result.imageLocation).use { stream ->
                    val bitmap = BitmapFactory.decodeStream(stream)
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        go = findViewById(R.id.button7)
        go.setOnClickListener {
            val intent = Intent()
            intent.putExtra("fragmentID", true)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    /**
     * Handler para botao "Guardar": abre SaveIdentification passando o resultado e ordem.
     * @param view O botao clicado.
     */
    fun onSaveQuitClick(view: View?) {
        val intent = Intent(this, SaveIdentification::class.java)
        intent.putExtra("fragmentID", this.resultKey)
        intent.putExtra("order", this.ordem)
        startActivityForResult(intent, SAVE_IDENT)
    }

    /**
     * Handler para botao "Sair": encerra a Activity sinalizando para regressar ao menu.
     * @param view O botao clicado.
     */
    fun onQuitClick(view: View?) {
        val resultIntent = Intent()
        resultIntent.putExtra("finish", true)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    /**
     * Callback invocado quando SaveIdentification retorna com resultado.
     * Se sucesso, sinaliza para terminar toda a sequencia e voltar ao menu.
     * @param requestCode Codigo do pedido.
     * @param resultCode Codigo do resultado (OK = sucesso).
     * @param data Intent com dados do resultado (se houver).
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SAVE_IDENT && resultCode == RESULT_OK) {
            val resultIntent = Intent()
            resultIntent.putExtra("finish", true)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    companion object {
        private const val SAVE_IDENT = 1
    }
}