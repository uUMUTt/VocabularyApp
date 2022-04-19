package com.example.vocabularyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_correct.*
import kotlinx.android.synthetic.main.fragment_wrong.*
import kotlinx.android.synthetic.main.fragment_wrong.btnContinueCorrect
import kotlinx.android.synthetic.main.fragment_wrong.tvEnglishTurkish


class WrongFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //back button is disable
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
        return inflater.inflate(R.layout.fragment_wrong, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var turkishWord = ""
        var englishWord = ""
        var pointer = 0
        var numOfWords = 0
        var numOfCorrect = 0
        arguments?.let {
            turkishWord = WrongFragmentArgs.fromBundle(it).turkishWord
            englishWord = WrongFragmentArgs.fromBundle(it).englishWord
            pointer = WrongFragmentArgs.fromBundle(it).pointer
            numOfWords = WrongFragmentArgs.fromBundle(it).numOfWords
            numOfCorrect = CorrectFragmentArgs.fromBundle(it).numOfCorrect
        }
        tvEnglishTurkish.text = "$englishWord = $turkishWord"
        btnContinueCorrect.setOnClickListener {
            if (pointer == numOfWords) {
                val action = WrongFragmentDirections.actionWrongFragmentToQuizAndFragment(numOfCorrect,numOfWords)
                view.findNavController().navigate(action)
            } else {
                val action = WrongFragmentDirections.actionWrongFragmentToQuizFragment()
                view.findNavController().navigate(action)
            }

        }
    }
}