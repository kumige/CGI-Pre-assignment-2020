package mikkoromo.cgipre_assignment

import androidx.lifecycle.LiveData

class RecordRepository(private val recordDao: DAO) {

    val allRecords: LiveData<List<Record>> = recordDao.getRecords()
    val allRecordsByRarity: LiveData<List<Record>> = recordDao.getRecordsByRarity()

    fun insert(record: Record) {
        recordDao.insertRecord(record)
    }
}