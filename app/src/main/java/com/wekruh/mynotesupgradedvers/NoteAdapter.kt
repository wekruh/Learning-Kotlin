package com.wekruh.mynotesupgradedvers

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.wekruh.mynotesupgradedvers.databinding.RecyclerRowBinding

class NoteAdapter(val noteList : ArrayList<notes>) : RecyclerView.Adapter<NoteAdapter.NoteHolder>(){
    private lateinit var database : SQLiteDatabase

    class NoteHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder{
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteHolder(binding)}

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.binding.itemTextView.text = noteList.get(position).note
        holder.binding.itemTextView.setOnClickListener {
            println("${noteList.get(position).id}")
        }
        database = holder.itemView.context.openOrCreateDatabase("Notes", Context.MODE_PRIVATE, null)


        fun deleteItem(position: Int) {
            val deletedNote = noteList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, noteList.size)

            try {
                database.execSQL("DELETE FROM notes WHERE id = ${deletedNote.id}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        holder.binding.itemImageView.setOnClickListener {
            deleteItem(position)
        }


    }

}

