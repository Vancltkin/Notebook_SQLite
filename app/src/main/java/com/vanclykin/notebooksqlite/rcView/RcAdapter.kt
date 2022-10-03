package com.vanclykin.notebooksqlite.rcView

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.vanclykin.notebooksqlite.EditActivity
import com.vanclykin.notebooksqlite.R
import com.vanclykin.notebooksqlite.db.IntentConstants
import com.vanclykin.notebooksqlite.db.ListItem
import com.vanclykin.notebooksqlite.db.MyDbManager

class RcAdapter(listMain: ArrayList<ListItem>, contextMainActivity: Context) :
    RecyclerView.Adapter<RcAdapter.MyHolder>() {
    private var listArray = listMain
    var context = contextMainActivity

    class MyHolder(itemView: View, contextViewHolder: Context) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)


        private val context = contextViewHolder

        fun setData(item: ListItem) {
            tvTitle.text = item.title
            tvTime.text = item.time

            itemView.setOnClickListener {

                val intent = Intent(context, EditActivity::class.java).apply {

                    putExtra(IntentConstants.INTENT_TITLE_KEY, item.title)
                    putExtra(IntentConstants.INTENT_CONTENT_KEY, item.content)
                    putExtra(IntentConstants.INTENT_URI_KEY, item.uri)
                    putExtra(IntentConstants.INTENT_ID_KEY, item.id)
                }

                context.startActivity(intent)

            }
        }
    }

    //берет наш шаблон layout и готовит для рисования и создает наш MyHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_view_item, parent, false), context)
    }

    //подключает данные массива к шаблону и после рисуется
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray[position])

    }

    //кол-во элементов в списке (массиве)
    override fun getItemCount(): Int {
        return listArray.size
    }

    fun updateAdapter(listItems: List<ListItem>) {
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItemAdapter(pos: Int, dbManager: MyDbManager) {
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }
}



















