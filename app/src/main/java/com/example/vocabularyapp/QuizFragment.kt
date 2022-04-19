package com.example.vocabularyapp

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.findNavController

import kotlinx.android.synthetic.main.fragment_new_word.*
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.fragment_quiz_and.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class QuizFragment : Fragment() {
    companion object {//for the values that don't change in my app
        var correctCount = 0
        var wrongCount = 0
        var questionCount = 0
        var questionCounter = 1
        var pointer: Int = 0
        var onlyOnce: Boolean = true//if I don't use this ,question count always increment when fragment is created

        //When the word list gotten from the database,it can be processed more easily
        val words = HashMap<String, String>()
        val turkishWords = ArrayList<String>()
        var backupTurkishWords = ArrayList<String>()
    }

    lateinit var correctWord: String
    lateinit var correctEnglishWord: String
    lateinit var db: SQLiteDatabase


    private val btnArr = ArrayList<Button>()//for the options
    lateinit var correctBtn: Button
    var selectedBtn: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this command removes back button functionality
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
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCorrect.text = "Doğru : $correctCount"
        tvWrong.text = "Yanlış : $wrongCount"
        btnArr.add(btnA)
        btnArr.add(btnB)
        btnArr.add(btnC)
        btnArr.add(btnD)

        context?.let {
            db = it.openOrCreateDatabase("VocabularyDB", Context.MODE_PRIVATE, null)
        }
        if (onlyOnce) {
            getDataFromDatabase()
            onlyOnce = false
        }
        questionCount = turkishWords.size
        tvQuestionCounter.text = "$questionCounter/$questionCount"
        createQuestion()
        imgBtnReturnToMenu.setOnClickListener {//All quiz is reset when return first fragment
            correctCount = 0
            wrongCount = 0
            questionCount = 0
            questionCounter = 1
            pointer = 0
            onlyOnce = true
            words.clear()
            turkishWords.clear()
            backupTurkishWords.clear()
            val action = QuizFragmentDirections.actionQuizFragmentToFirstFragment()
            view.findNavController().navigate(action)
        }
        btnCheck.setOnClickListener {
            checkResult(it)

        }
        //To make the selected button green
        btnA.setOnClickListener {
            selectedBtn = btnA
            btnA.setBackgroundColor(Color.parseColor("#BEBEBE"))
            btnB.setBackgroundColor(Color.parseColor("#696969"))
            btnC.setBackgroundColor(Color.parseColor("#696969"))
            btnD.setBackgroundColor(Color.parseColor("#696969"))
        }
        btnB.setOnClickListener {
            selectedBtn = btnB
            btnB.setBackgroundColor(Color.parseColor("#BEBEBE"))
            btnA.setBackgroundColor(Color.parseColor("#696969"))
            btnC.setBackgroundColor(Color.parseColor("#696969"))
            btnD.setBackgroundColor(Color.parseColor("#696969"))
        }
        btnC.setOnClickListener {
            selectedBtn = btnC
            btnC.setBackgroundColor(Color.parseColor("#BEBEBE"))
            btnB.setBackgroundColor(Color.parseColor("#696969"))
            btnA.setBackgroundColor(Color.parseColor("#696969"))
            btnD.setBackgroundColor(Color.parseColor("#696969"))
        }
        btnD.setOnClickListener {
            selectedBtn = btnD
            btnD.setBackgroundColor(Color.parseColor("#BEBEBE"))
            btnB.setBackgroundColor(Color.parseColor("#696969"))
            btnC.setBackgroundColor(Color.parseColor("#696969"))
            btnA.setBackgroundColor(Color.parseColor("#696969"))
        }

    }

    private fun checkResult(view: View) {
        pointer++
        questionCounter++
        if (selectedBtn != null && selectedBtn == correctBtn) {
            correctCount++
            val action = QuizFragmentDirections.actionQuizFragmentToCorrectWrongFragment(
                correctWord,
                pointer,
                questionCount,
                correctCount,
                correctEnglishWord
            )
            view.findNavController().navigate(action)
        } else {
            wrongCount++
            val action = QuizFragmentDirections.actionQuizFragmentToWrongFragment(
                correctWord,
                pointer,
                questionCount,
                correctCount,
                correctEnglishWord
            )
            view.findNavController().navigate(action)
        }
    }

    /*First, the correct answer is added to the button list.
    Then random elements are selected from the previously created word lists.
    Each selected word is deleted from the list and added to the deleted list.
    Finally, the elements in the deleted list are placed back in their previous lists.
    */
    private fun createQuestion() {
        println("pointer = $pointer")
        val turkishWord = turkishWords[pointer]
        correctWord = turkishWord
        val englishWord = words[turkishWord]
        correctEnglishWord = englishWord!!
        tvWord.text = englishWord
        val deleted = ArrayList<String>()
        deleted.add(turkishWord)
        backupTurkishWords.remove(turkishWord)
        var correctIndex = Random().nextInt(4)
        correctBtn = btnArr[correctIndex]
        btnArr[correctIndex].text = turkishWord
        var index: Int
        for (i in 0..3) {
            if (i != correctIndex) {
                println("size= ${backupTurkishWords.size}")
                index = Random().nextInt(backupTurkishWords.size)
                println("index = $index")
                btnArr[i].text = backupTurkishWords[index]
                deleted.add(backupTurkishWords[index])
                backupTurkishWords.removeAt(index)
            }
        }
        for (i in 0..3) {
            backupTurkishWords.add(deleted[i])
        }
    }

    private fun getDataFromDatabase() {
        try {
            val cursor = db.rawQuery("select * from vocabulary", null)
            val turkish = cursor.getColumnIndex("turkish")
            val english = cursor.getColumnIndex("english")
            words.clear()
            while (cursor.moveToNext()) {
                words[cursor.getString(turkish)] = cursor.getString(english)
                turkishWords.add(cursor.getString(turkish))
                backupTurkishWords.add(cursor.getString(turkish))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}