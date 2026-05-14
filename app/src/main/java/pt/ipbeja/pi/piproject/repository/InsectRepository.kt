package pt.ipbeja.pi.piproject.repository

import pt.ipbeja.pi.piproject.persistence.Identification
import pt.ipbeja.pi.piproject.persistence.IdentificationDao

/**
 * Repository that encapsulates data operations related to insect identifications.
 * Provides a simple, testable API to the rest of the app.
 */
class InsectRepository(private val dao: IdentificationDao) {

    suspend fun getAll(): List<Identification> = dao.getAll()

    suspend fun insert(identification: Identification) = dao.insert(identification)

    suspend fun delete(identification: Identification) = dao.delete(identification)

    suspend fun update(identification: Identification) = dao.update(identification)

    suspend fun updateOrderInIdentification(id: Long, order: String) = dao.updateOrderInIdentification(id, order)
}

