package pt.ipbeja.pi.piproject.listSavedInsects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipbeja.pi.piproject.persistence.Identification
import pt.ipbeja.pi.piproject.persistence.MyIdentificationsDb
import pt.ipbeja.pi.piproject.repository.InsectRepository

/**
 * ViewModel for `MyIdentifications` activity. Encapsulates data operations and exposes
 * a LiveData list of identifications to the UI.
 */
class MyIdentificationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: InsectRepository

    private val _idents = MutableLiveData<List<Identification>>(emptyList())
    val idents: LiveData<List<Identification>> = _idents

    init {
        val db = MyIdentificationsDb.getDatabase(application)
        repo = InsectRepository(db.identificationDao())
        loadList()
    }

    fun loadList() {
        viewModelScope.launch {
            val list = repo.getAll().reversed()
            _idents.postValue(list)
        }
    }

    fun delete(identification: Identification) {
        viewModelScope.launch {
            repo.delete(identification)
            loadList()
        }
    }

    fun update(identification: Identification) {
        viewModelScope.launch {
            repo.update(identification)
            loadList()
        }
    }

    suspend fun insert(identification: Identification) {
        repo.insert(identification)
        loadList()
    }
}

