package mikkoromo.cgipre_assignment

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ObservationViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: RecordRepository
    var allRecords: LiveData<List<Record>>
    var allRecordsByRarity: LiveData<List<Record>>

    init {
        val db = Database.getInstance(application)
        repository = RecordRepository(db!!.dao())
        allRecords = repository.allRecords
        allRecordsByRarity = repository.allRecordsByRarity
    }

    fun insert(record: Record) = viewModelScope.launch {
        repository.insert(record)
    }

}