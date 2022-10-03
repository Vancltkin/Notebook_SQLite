package com.vanclykin.notebooksqlite

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanclykin.notebooksqlite.databinding.ActivityMainBinding
import com.vanclykin.notebooksqlite.db.MyDbManager
import com.vanclykin.notebooksqlite.db.MyDbNameClass
import com.vanclykin.notebooksqlite.rcView.RcAdapter

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val myDbManager = MyDbManager(this)
    private val myAdapter = RcAdapter(ArrayList(), this)


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initSearchView()

        binding.btAdd.setOnClickListener {
            val i = Intent(this, EditActivity::class.java)
            startActivity(i)
        }

        binding.ivLogo.setOnClickListener {
            val dialog = AlertDialog.Builder(this@MainActivity)
                .setTitle(getString(R.string.faq))
                .setMessage("Версия приложения: ${BuildConfig.VERSION_NAME}")
                .setCancelable(true)
                .setNegativeButton("Закрыть", null)
                .create()
            dialog.show()
        }
    }

    override fun onResume() {

        super.onResume()
        myDbManager.openDb()
        fillAdapter()

    }

    override fun onDestroy() {

        super.onDestroy()
        myDbManager.closeDb()
    }

    // элементы по вертикал
    private fun init() {

        binding.rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapManager()
        swapHelper.attachToRecyclerView(binding.rcView)
        binding.rcView.adapter = myAdapter
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val list = myDbManager.readDbData(newText!!)
                myAdapter.updateAdapter(list)
                return true
            }

        })
    }

    private fun fillAdapter() {

        val list = myDbManager.readDbData("")
        myAdapter.updateAdapter(list)
        if (list.size > 0) {

            binding.tvNoElements.visibility = View.GONE
        } else {
            binding.tvNoElements.visibility = View.VISIBLE
        }
    }

    private fun getSwapManager(): ItemTouchHelper {

        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val listener = DialogInterface.OnClickListener { _, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        myAdapter.removeItemAdapter(viewHolder.adapterPosition, myDbManager) //эта
                    } else {fillAdapter()}
                }


                val dialog = AlertDialog.Builder(this@MainActivity)
                    .setTitle("Удаление заметки")
                    .setMessage("Вы точно хотите удалить заметку?")
                    .setNegativeButton("Отмена", listener)
                    .setCancelable(false)
                    .setPositiveButton("Удалить", listener)
                    .create()
                dialog.show()



            }
        })
    }
}