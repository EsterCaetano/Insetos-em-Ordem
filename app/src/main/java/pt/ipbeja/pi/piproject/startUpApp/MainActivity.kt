package pt.ipbeja.pi.piproject.startUpApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.credits.Credits
import pt.ipbeja.pi.piproject.identificationInsect.Classificacao
import pt.ipbeja.pi.piproject.listSavedInsects.MyIdentifications
import pt.ipbeja.pi.piproject.persistence.MyIdentificationsDb

/**
 * Activity principal do menu - ponto de entrada apos intro.
 * Oferece atalhos para identificar insetos, visualizar registos ou creditos.
 */
class MainActivity : AppCompatActivity() {

    private val db by lazy { MyIdentificationsDb.getDatabase(this) }

    /**
     * Inicializa a Activity com o layout do menu.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Atualiza o estado do botao "Os meus insetos" conforme tenha registos guardados (enabled se > 0).
     */
    override fun onResume() {
        super.onResume()
        setInitialButtonState()
    }

    /**
     * Consulta base de dados para obter contagem de identificacoes e atualiza estado do botao.
     */
    private fun setInitialButtonState() {
        lifecycleScope.launch {
            val idCount = db.identificationDao().count()
            val myIdsButton = findViewById<Button>(R.id.button)
            myIdsButton.isEnabled = idCount > 0
        }
    }

    /**
     * Handler para botao "Identificar": inicia a chave dicotomica desde a primeira pergunta (Q1).
     */
    fun onClickToClassification(view: View?) {
        val intent = Intent(this, Classificacao::class.java)
        intent.putExtra("fragmentID", "Q1")
        startActivity(intent)
    }

    /**
     * Handler para botao "Os meus insetos": abre lista de identificacoes guardadas.
     */
    fun onClickToMyInsects(view: View?) {
        val intent = Intent(this, MyIdentifications::class.java)
        startActivity(intent)
    }

    /**
     * Handler para botao "Creditos": abre tela de creditos e instituicoes.
     */
    fun onClickToCredits(view: View?) {
        val intent = Intent(this, Credits::class.java)
        startActivity(intent)
    }
}
