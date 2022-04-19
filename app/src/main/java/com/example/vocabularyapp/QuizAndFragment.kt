package com.example.vocabularyapp

import android.content.Context
import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_quiz_and.*
import kotlin.math.roundToInt


class QuizAndFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_quiz_and, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val numOfCorrect = QuizAndFragmentArgs.fromBundle(it).numOfCorrect.toDouble()
            val numOfWords = QuizAndFragmentArgs.fromBundle(it).numOfWords.toDouble()
            val rate = (numOfCorrect / numOfWords * 100).roundToInt()
            tvCorrectRate.text = "%$rate"
        }
        QuizFragment.correctCount = 0
        QuizFragment.wrongCount = 0
        QuizFragment.questionCount = 0
        QuizFragment.questionCounter = 1
        QuizFragment.pointer = 0
        QuizFragment.onlyOnce = true
        QuizFragment.words.clear()
        QuizFragment.turkishWords.clear()
        QuizFragment.backupTurkishWords.clear()
        btnRetMenu.setOnClickListener {
            val action = QuizAndFragmentDirections.actionQuizAndFragmentToFirstFragment()
            view.findNavController().navigate(action)
        }
        btnRepeat.setOnClickListener { view ->
            context?.let {
                val db = it.openOrCreateDatabase("VocabularyDB", Context.MODE_PRIVATE, null)
                val numOfRow = DatabaseUtils.queryNumEntries(db, "vocabulary")
                if (numOfRow < 5) {
                    val action = QuizAndFragmentDirections.actionQuizAndFragmentToFirstFragment()
                    view.findNavController().navigate(action)
                } else {
                    val action = QuizAndFragmentDirections.actionQuizAndFragmentToQuizFragment()
                    view.findNavController().navigate(action)
                }
            }

        }
    }
}