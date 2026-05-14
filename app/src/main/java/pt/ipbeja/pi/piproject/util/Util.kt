package pt.ipbeja.pi.piproject.util

/**
 * Utilidade com funcoes auxiliares para manipulacao de texto.
 */
object Util {
    /**
     * Remove espaços multiple e leading/trailing da string, incluindo quebras de linha com espacos.
     * Converte linhas com apenas espacos/tabs em linhas vazias.
     * @param s String a processar.
     * @return String com espacos normalizados.
     */
    fun removeSpaces(s: String): String {
        val descriptionNoMultipleSpaces = s.replace("^\\s*".toRegex(), "")
        return descriptionNoMultipleSpaces.replace("\\n( |\\t)*".toRegex(), "\n")
    }
}