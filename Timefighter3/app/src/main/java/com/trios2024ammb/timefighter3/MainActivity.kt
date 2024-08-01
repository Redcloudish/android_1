package com.trios2024amdj.timefighter2

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class MainActivity : AppCompatActivity() {

    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView
    private lateinit var tapMeButton: Button
    private lateinit var constraintLayout: ConstraintLayout

    private var score = 0
    private var gameStarted = false

    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft = 60

    private lateinit var bounceAnimation: Animation
    private lateinit var moveAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect views to variables
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)
        constraintLayout = findViewById(R.id.main)

        // Load animations
        bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        moveAnimation = AnimationUtils.loadAnimation(this, R.anim.move)

        // Set button click listener
        tapMeButton.setOnClickListener {
            incrementScore()
            animateButton()
        }

        resetGame()
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }
        score++
        val newScore = getString(R.string.your_score, score)
        gameScoreTextView.text = newScore
    }

    private fun resetGame() {
        score = 0
        val initialScore = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore

        val initialTimeLeft = getString(R.string.time_left, 60)
        timeLeftTextView.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
                val timeLeftString = getString(R.string.time_left, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message, score), Toast.LENGTH_LONG).show()
        resetGame()
    }

    private fun animateButton() {
        // Apply bounce animation
        tapMeButton.startAnimation(bounceAnimation)

        // Move button to a random position
        val randomStartMargin = (Math.random() * (constraintLayout.width - tapMeButton.width)).toInt()
        val randomTopMargin = (Math.random() * (constraintLayout.height - tapMeButton.height)).toInt()

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setMargin(R.id.tap_me_button, ConstraintSet.START, randomStartMargin)
        constraintSet.setMargin(R.id.tap_me_button, ConstraintSet.TOP, randomTopMargin)
        constraintSet.applyTo(constraintLayout)

        // Apply move animation
        tapMeButton.startAnimation(moveAnimation)
    }
}