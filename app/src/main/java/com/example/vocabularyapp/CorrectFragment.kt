package com.example.vocabularyapp

import android.app.AlertDialog
import android.content.Context
import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_correct.*

class CorrectFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_correct, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var turkishWord = ""
        var englishWord = ""
        var pointer = 0
        var numOfWords = 0
        var numOfCorrect = 0

        arguments?.let {
            turkishWord = CorrectFragmentArgs.fromBundle(it).turkishWord
            englishWord = WrongFragmentArgs.fromBundle(it).englishWord
            pointer = CorrectFragmentArgs.fromBundle(it).pointer
            numOfWords = CorrectFragmentArgs.fromBundle(it).numOfWords
            numOfCorrect = CorrectFragmentArgs.fromBundle(it).numOfCorrect
        }
        tvEnglishTurkish.text = "$englishWord = $turkishWord"

        btnContinueCorrect.setOnClickListener {
            startAction(view,pointer,numOfCorrect,numOfWords)
            }
        btnLearned.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("UYARI!!!")
            builder.setMessage("Öğrendiğin kelime silinecek, onaylıyor musun?")
            builder.setPositiveButton("Evet"){_,_ ->
                context?.let {
                    val db = it.openOrCreateDatabase("VocabularyDB",Context.MODE_PRIVATE,null)
                    db.execSQL("delete from vocabulary where turkish = '$turkishWord' ")
                    startAction(view,pointer,numOfCorrect,numOfWords)
                }
            }
            builder.setNegativeButton("Hayır"){_,_ ->
                //No action
            }
            builder.show()

        }

    }

    private fun startAction(view:View,pointer:Int,numOfCorrect:Int,numOfWords:Int){
        if(pointer == numOfWords){
            val action = CorrectFragmentDirections.actionCorrectFragmentToQuizAndFragment(
                numOfCorrect,
                numOfWords
            )
            view.findNavController().navigate(action)
        }else{
            val action = CorrectFragmentDirections.actionCorrectWrongFragmentToQuizFragment()
            view.findNavController().navigate(action)
        }
    }
}