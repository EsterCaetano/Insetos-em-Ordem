package pt.ipbeja.pi.piproject.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipbeja.pi.piproject.util.Coordinates
import java.util.Date

/**
 * Entity class representing an insect identification record in the database.
 */
@Entity
data class Identification(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    /**
     * The key ID from the identification key XML.
     */
    var keyId: String,

    /**
     * The identified insect order.
     */
    var order: String,

    /**
     * Latitude of the identification location.
     */
    var latitude: Double,

    /**
     * Longitude of the identification location.
     */
    var longitude: Double,

    /**
     * Timestamp of when the identification was saved.
     */
    var timestamp: Date,

    /**
     * URI of the photo associated with the identification.
     */
    var photoURI: String
) {
    /**
     * Returns the coordinates in Degrees, Minutes, Seconds format.
     */
    val dMSCoord: String
        get() = Coordinates.anglesToDMS(latitude, longitude)
}