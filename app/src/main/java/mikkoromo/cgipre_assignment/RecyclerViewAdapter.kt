package mikkoromo.cgipre_assignment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_item.view.*
import java.net.URI


class RecyclerViewAdapter(context: Context) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var recordList = emptyList<Record>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.textView_name
        val notes: TextView = itemView.textView_notes
        val date: TextView = itemView.textView_date
        val rarity: TextView = itemView.textView_rarity
        val latitude: TextView = itemView.textView_latitude
        val longitude: TextView = itemView.textView_longitude
        val imagePath: ImageView = itemView.imgView_photo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.MyViewHolder {
        val view = inflater.inflate(R.layout.rv_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val record = recordList[position]
        holder.name.text = record.name
        holder.notes.text = record.notes
        holder.date.text = record.timestamp
        when (record.rarity){
            "1" -> holder.rarity.text = "Common"
            "2" -> holder.rarity.text = "Rare"
            "3" -> holder.rarity.text = "Extremely rare"
        }
        holder.latitude.text = "lat: ${record.latitude}"
        holder.longitude.text = "lon: ${record.longitude}"
        if (record.imagePath.isNullOrEmpty()) {
            holder.imagePath.setImageResource(R.drawable.ic_action_close)
        } else {
            holder.imagePath.setImageURI(record.imagePath?.toUri())
        }
        Log.d("dbg", record.toString())
    }

    internal fun setData(newRecordList: List<Record>) {
        this.recordList = newRecordList
        notifyDataSetChanged()
    }

}