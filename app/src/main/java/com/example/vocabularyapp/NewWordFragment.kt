package com.example.vocabularyapp

import android.app.AlertDialog
import android.content.Context
import android.database.DatabaseUtils
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_new_word.*
import kotlinx.android.synthetic.main.one_plus_toast.*
import java.lang.Exception

class NewWordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val db = it.openOrCreateDatabase("VocabularyDB", Context.MODE_PRIVATE, null)
            //To find how much words adds into the database
            val numOfVoc = DatabaseUtils.queryNumEntries(db, "vocabulary")
            tvWordCount.text = "Hafızadaki kelime sayısı = $numOfVoc"
        }
        btnRetFromNWF.setOnClickListener {
            val action = NewWordFragmentDirections.actionNewWordFragmentToFirstFragment()
            view.findNavController().navigate(action)
        }

        btnSave.setOnClickListener {
            saveToDatabase(it)
        }
    }

    private fun saveToDatabase(view: View) {
        val tr = etxtTurkish.text.toString().lowercase()
        val en = etxtEnglish.text.toString().lowercase()

        if (tr == "" || en == "") {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Hata!!")
            builder.setMessage("Lütfen tüm alanları doldurun.")
            builder.show()
        } else {
            try {
                val layout = layoutInflater.inflate(R.layout.one_plus_toast,onePlusToast)
                context?.let {context->
                    Toast(context).apply {
                        duration= Toast.LENGTH_SHORT
                        setGravity(Gravity.TOP,0,10)
                        this.view = layout
                    }.show()
                }
                context?.let {
                    val db = it.openOrCreateDatabase("VocabularyDB", Context.MODE_PRIVATE, null)
                    val sqlString = "insert into vocabulary(turkish,english) values (?,?)"
                    val statement = db.compileStatement(sqlString)
                    statement.bindString(1, tr)
                    statement.bindString(2, en)
                    statement.execute()
                    val numOfVoc = DatabaseUtils.queryNumEntries(db, "vocabulary")
                    tvWordCount.text = "Hafızadaki kelime sayısı = $numOfVoc"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //To clear all text plain
        etxtTurkish.text.clear()
        etxtEnglish.text.clear()

    }


}