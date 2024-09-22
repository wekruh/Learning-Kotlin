package com.wekruh.mynotesupgradedvers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.wekruh.mynotesupgradedvers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteList : ArrayList<notes>
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        noteList = ArrayList<notes>()
        noteAdapter = NoteAdapter(noteList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = noteAdapter

        try {

            val database = this.openOrCreateDatabase("Notes", Context.MODE_PRIVATE,null)

            val cursor = database.rawQuery("SELECT * FROM notes",null)
            val noteIx = cursor.getColumnIndex("note")
            val idIx = cursor.getColumnIndex("id")

            while (cursor.moveToNext()) {
                val text = cursor.getString(noteIx)
                val id = cursor.getInt(idIx)
                val note = notes(text,id)
                noteList.add(note)
                println("name: $text id: $id")
            }

            noteAdapter.notifyDataSetChanged()

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStart() {
        super.onStart()
        binding.recyclerView.isVisible = true
        binding.button.isVisible = true
    }

    fun addNote(view : View) {
        intent = Intent(this, NoteAddingActivity::class.java)
        startActivity(intent)
    }

    fun delAll(view: View){
        alert()
    }

    fun alert(){
        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle("Save")
        alert.setMessage("Are You Sure?")

        alert.setPositiveButton("Yes") {dialog, which ->
            try {
                val database = this.openOrCreateDatabase("Notes", Context.MODE_PRIVATE,null)
                database.execSQL("DELETE FROM notes")
                intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                noteAdapter.notifyDataSetChanged()
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(this,"Deleted the all notes!",Toast.LENGTH_LONG).show()
        }
        alert.setNegativeButton("No") {dialog, which ->
            Toast.makeText(applicationContext,"Didn't deleted any notes!",Toast.LENGTH_LONG).show()
        }

        alert.show()
    }

    }

