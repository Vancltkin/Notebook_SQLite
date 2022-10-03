package com.vanclykin.notebooksqlite

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vanclykin.notebooksqlite.databinding.EditActivityBinding
import com.vanclykin.notebooksqlite.db.IntentConstants
import com.vanclykin.notebooksqlite.db.MyDbManager
import java.text.SimpleDateFormat
import java.time.LocalDateTime.now
import java.util.*

class EditActivity : AppCompatActivity() {
    lateinit var binding: EditActivityBinding

    private var id = 0
    var isEditState = false
    private val imageRequestCode = 10
    private var tempImageUri = "empty"
    private val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = EditActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getMyIntents()

        binding.btAddPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }

        binding.imButtonDeleteImage.setOnClickListener {
            binding.imageLayout.visibility = View.GONE
            binding.btAddPhoto.visibility = View.VISIBLE


        }

        binding.btSave.setOnClickListener {
            val myTitle = binding.edTitle.text.toString()
            val myContent = binding.edContent.text.toString()
            if (myTitle != "" && myContent != "") {
                if (isEditState) {
                    myDbManager.updateItem(myTitle, myContent, tempImageUri, id, getCurrentTime())
                } else {
                    myDbManager.insertToDb(myTitle, myContent, tempImageUri, getCurrentTime())
                }
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            binding.imPhoto.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(
                data?.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            binding.imageLayout.visibility = View.VISIBLE
            binding.btAddPhoto.visibility = View.GONE
        }
    }

    private fun getMyIntents() {
        val i = intent

        if (i != null) {
            //Log.d("MyLog","Data received:" + i.getStringExtra(IntentConstants.INTENT_TITLE_KEY))
            if (i.getStringExtra(IntentConstants.INTENT_TITLE_KEY) != null) {

                //binding.btAddPhoto.visibility = View.GONE

                binding.edTitle.setText(i.getStringExtra(IntentConstants.INTENT_TITLE_KEY))
                binding.edContent.setText(i.getStringExtra(IntentConstants.INTENT_CONTENT_KEY))

                isEditState = true
                id = i.getIntExtra(IntentConstants.INTENT_ID_KEY, 0)

                if (i.getStringExtra(IntentConstants.INTENT_URI_KEY) != "empty") {

                    binding.imageLayout.visibility = View.VISIBLE
                    binding.btAddPhoto.visibility = View.GONE

                    binding.imPhoto.setImageURI(Uri.parse(i.getStringExtra(IntentConstants.INTENT_URI_KEY)))
                    //binding.imButtonDeleteImage.visibility = View.GONE

                }
            }
        }
    }

    private fun getCurrentTime(): String{
        val postTime = Calendar.getInstance().time
        val format = SimpleDateFormat("dd.MM.yy kk:mm", Locale.getDefault())
        val fTime = format.format(postTime)
        return fTime.toString()
    }
}