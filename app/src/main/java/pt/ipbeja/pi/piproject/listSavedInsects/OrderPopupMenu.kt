package pt.ipbeja.pi.piproject.listSavedInsects

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.idkey.IdentificationKey
import pt.ipbeja.pi.piproject.persistence.Identification
import pt.ipbeja.pi.piproject.persistence.MyIdentificationsDb
import java.io.IOException

/**
 * Popup menu to change the order of a saved insect identification.
 */
class OrderPopupMenu(
    private val context: Context,
    view: View,
    private val identification: Identification,
    private val db: MyIdentificationsDb,
    identificationsArray: Array<Identification?>?,
    private val adapter: ArrayAdapter<*>?, // Made adapter a property to use in the click listener
    private val identsLst: View
) : PopupMenu(context, view) {

    init {
        try {
            menuInflater.inflate(R.menu.insect_order_popup_menu, menu)

            val key = IdentificationKey.getInstance(context)
            if (key != null) {
                val orders = key.getOrders()
                for (i in orders.indices) {
                    if (i < menu.size()) {
                        menu.getItem(i).setTitle(orders[i])
                    }
                }
            }

            setOnMenuItemClickListener { item ->
                val newOrder = item.title.toString()

                try {
                    val resultNode = IdentificationKey.getInstance(context)?.getResultByOrder(newOrder)
                    if (resultNode != null) {
                        identification.keyId = resultNode.id
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    db.identificationDao().updateOrderInIdentification(identification.id.toLong(), newOrder)
                    // Refresh the list on the UI thread instead of restarting the activity
                    (context as? Activity)?.runOnUiThread {
                        adapter?.notifyDataSetChanged()
                    }
                }
                true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
