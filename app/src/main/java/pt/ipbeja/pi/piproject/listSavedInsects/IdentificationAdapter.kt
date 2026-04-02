package pt.ipbeja.pi.piproject.listSavedInsects

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.persistence.Identification
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Adapter for displaying a list of identifications.
 * Created by vxf on 2/2/18.
 */
class IdentificationAdapter(context: Context, objects: List<Identification?>) :
    ArrayAdapter<Identification?>(context, 0, objects) {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false)
        val ident = getItem(position) ?: return view

        val orderTxt = view.findViewById<TextView>(R.id.orderTxt)
        val coordsTxt = view.findViewById<TextView>(R.id.coordsTxt)
        val dateTxt = view.findViewById<TextView>(R.id.dateTxt)
        val imageView = view.findViewById<ImageView>(R.id.coverView)

        orderTxt.text = ident.order
        coordsTxt.text = ident.dMSCoord
        dateTxt.text = dateFormatter.format(ident.timestamp)

        try {
            val photoUri = Uri.parse(ident.photoURI)
            context.contentResolver.openInputStream(photoUri)?.use { stream ->
                val image = BitmapFactory.decodeStream(stream)
                imageView.setImageBitmap(image)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return view
    }
}