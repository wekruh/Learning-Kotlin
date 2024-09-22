package com.wekruh.mynotesupgradedvers

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.wekruh.mynotesupgradedvers.databinding.ActivityNoteAddingBinding

class NoteAddingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteAddingBinding
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = this.openOrCreateDatabase("Notes", Context.MODE_PRIVATE, null)

    }

    fun saveNote(view: View) {

        val note = binding.noteText.text.toString()

        if (note != null) {
            try {

                if(note.length <= 0){
                    val toast = android.widget.Toast.makeText(applicationContext, "Enter a note please.", android.widget.Toast.LENGTH_SHORT)
                    toast.show()
                    return
                }
                database.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY, note VARCHAR)")

                val sqlString = "INSERT INTO notes (note) VALUES (?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1, note)

                statement.execute()

                intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}