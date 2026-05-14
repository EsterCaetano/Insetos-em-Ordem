package pt.ipbeja.pi.piproject.idkey

import android.content.Context
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.IOException
import java.io.InputStream
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Singleton object for parsing and managing the identification key XML.
 */
class IdentificationKey private constructor() {

    private val orders = mutableListOf<String>()
    private val nodes = HashMap<String, QuestionNode>()
    private val results = HashMap<String, ResultNode>()

    /**
     * Returns the list of available insect orders.
     */
    fun getOrders(): List<String> = orders

    /**
     * Adds a question node to the key.
     */
    fun addQuestion(node: QuestionNode) {
        nodes[node.id] = node
    }

    /**
     * Adds a result node to the key.
     */
    fun addResult(node: ResultNode) {
        results[node.id] = node
        orders.add(node.ordem)
    }

    /**
     * Gets a question node by ID.
     */
    fun getQuestion(id: String): QuestionNode? = nodes[id]

    /**
     * Gets a result node by ID.
     */
    fun getResult(id: String): ResultNode? = results[id]

    /**
     * Gets a result node by insect order name.
     */
    fun getResultByOrder(order: String): ResultNode? {
        val index = orders.indexOf(order)
        if (index == -1) return null
        val id = index + 1
        return results["R$id"]
    }

    /**
     * Checks if the given ID corresponds to a question node.
     */
    fun isQuestion(id: String): Boolean = nodes.containsKey(id)

    /**
     * Checks if the given ID corresponds to a result node.
     */
    fun isResult(id: String): Boolean = results.containsKey(id)

    companion object {
        @Volatile
        private var instance: IdentificationKey? = null

        /**
         * Returns the singleton instance of IdentificationKey.
         * Loads the XML key based on the device language.
         */
        @Throws(IOException::class)
        fun getInstance(ctx: Context): IdentificationKey? {
            return instance ?: synchronized(this) {
                instance ?: loadKey(ctx).also { instance = it }
            }
        }

        /**
         * Carrega o ficheiro XML da chave dicotomica conforme a linguagem do dispositivo (PT ou EN).
         * @param ctx Contexto da aplicacao para acesso aos assets.
         * @return Instancia de IdentificationKey carregada ou null se erro.
         */
        private fun loadKey(ctx: Context): IdentificationKey? {
            val language = Locale.getDefault().language
            val fileName = if (language.startsWith("pt")) "chave.xml" else "chave-en.xml"
            return ctx.assets.open(fileName).use { loadXML(it) }
        }

        /**
         * Parse um elemento 'option' do XML e retorna um objeto KeyOption.
         * @param e Elemento XML da opcao.
         * @return KeyOption com texto, descricao, imagem e ID de destino.
         */
        private fun parseOption(e: Element): KeyOption {
            val gotoId = e.getAttribute("goto")
            val text = e.getElementsByTagName("text").item(0).textContent
            val description = e.getElementsByTagName("description").item(0).textContent
            val imageLocation = e.getElementsByTagName("imageLocation").item(0).textContent
            return KeyOption(gotoId, description, imageLocation, text)
        }

        /**
         * Parse um elemento 'node' (pergunta) do XML e retorna um QuestionNode.
         * Cada pergunta tem um ID, texto, e duas opcoes (A e B).
         * @param e Elemento XML da pergunta.
         * @return QuestionNode com pergunta, opcoes e ID.
         */
        private fun parseQuestion(e: Element): QuestionNode {
            val id = e.getAttribute("id")
            val question = e.getElementsByTagName("question").item(0).textContent
            val options = e.getElementsByTagName("options").item(0) as Element
            val optionA = parseOption(options.getElementsByTagName("option").item(0) as Element)
            val optionB = parseOption(options.getElementsByTagName("option").item(1) as Element)
            return QuestionNode(id, question, optionA, optionB)
        }

        /**
         * Parse um elemento 'result' (resultado/ordem) do XML e retorna um ResultNode.
         * @param e Elemento XML do resultado.
         * @return ResultNode com ID, ordem, descricao e imagem.
         */
        private fun parseResult(e: Element): ResultNode {
            val id = e.getAttribute("id")
            val ordem = e.getElementsByTagName("ordem").item(0).textContent
            val description = e.getElementsByTagName("description").item(0).textContent
            val imageLocation = e.getElementsByTagName("imageLocation").item(0).textContent
            return ResultNode(id, ordem, description, imageLocation)
        }

        /**
         * Carrega e parse o XML completo da chave dicotomica do InputStream.
         * Popula a instancia IdentificationKey com todas as perguntas e resultados.
         * @param `is` InputStream com o conteudo XML.
         * @return IdentificationKey carregada ou null se erro durante parse.
         */
        private fun loadXML(`is`: InputStream): IdentificationKey? {
            val key = IdentificationKey()
            try {
                val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(`is`)
                val nodes = doc.getElementsByTagName("nodes").item(0) as Element
                val nList = nodes.getElementsByTagName("node")

                for (i in 0 until nList.length) {
                    val node = nList.item(i)
                    if (node.nodeType == Node.ELEMENT_NODE) {
                        key.addQuestion(parseQuestion(node as Element))
                    }
                }

                val results = doc.getElementsByTagName("results").item(0) as Element
                val rList = results.getElementsByTagName("result")

                for (i in 0 until rList.length) {
                    val node = rList.item(i)
                    if (node.nodeType == Node.ELEMENT_NODE) {
                        key.addResult(parseResult(node as Element))
                    }
                }
                return key
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}