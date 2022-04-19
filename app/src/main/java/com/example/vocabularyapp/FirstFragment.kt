package com.example.vocabularyapp

import android.app.AlertDialog
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment() {

    lateinit var db: SQLiteDatabase
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
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createTable()
        btnAddNewWord.setOnClickListener {
            db = it.context.openOrCreateDatabase("VocabularyDB", Context.MODE_PRIVATE, null)
            val action = FirstFragmentDirections.actionFirstFragmentToNewWordFragment()
            view.findNavController().navigate(action)
        }
        btnQuiz.setOnClickListener {
            db = it.context.openOrCreateDatabase("VocabularyDB", Context.MODE_PRIVATE, null)
            val numOfWords = DatabaseUtils.queryNumEntries(db, "vocabulary")
            if (numOfWords >= 5) {
                val action = FirstFragmentDirections.actionFirstFragmentToQuizFragment()
                view.findNavController().navigate(action)
            } else {
                val builder = AlertDialog.Builder(this.context)
                builder.setTitle("UYARI!!!")
                builder.setMessage("Egzersiz yapabilmen için en az 5 kelime kaydetmen gerekli.\n" +
                        "Kaydedilen kelime sayısı = $numOfWords")
                builder.show()
            }
        }
    }
    private fun createTable() {
        context?.let {
            val db = it.openOrCreateDatabase("VocabularyDB", Context.MODE_PRIVATE, null)
            db.execSQL(
                "create table if not exists vocabulary(" +
                        "id serial primary key," +
                        "turkish varchar not null," +
                        "english varchar not null," +
                        "unique(turkish,english))"

            )
        }
    }

}