package com.example.roomsample

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope, View.OnClickListener {

    private var noteDB: NoteRoomDatabase? = null
    private var adapter: NoteListAdapter? = null

    private lateinit var mJob: Job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mJob = Job()

        noteDB = NoteRoomDatabase.getDatabase(this)
        adapter = NoteListAdapter(this, noteDB!!)

        recycler_notes.adapter = adapter
        recycler_notes.layoutManager = LinearLayoutManager(this)

        button_new_note.setOnClickListener(this)
        button_find.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }

    private fun getAllNotes() {
        launch {
            val notes: List<NoteEntity>? = noteDB?.noteDao()?.getAllNotes()
            if (notes != null) {
                adapter?.setNotes(notes)
            }
        }

    }

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onClick(v: View?) {
        when (v) {
            button_new_note -> {
                val newNoteIntent = Intent(this, NewNoteActivity::class.java)
                startActivity(newNoteIntent)
            }
            button_find -> {
                findNote()
            }
        }
    }

     fun findNote() = launch {
        val strFind= edittext_find.text.toString()
         if(!TextUtils.isEmpty(strFind)){
             val note: NoteEntity?= noteDB?.noteDao()?.findNoteByTitle(strFind)
             if (note!=null){
                 val  notes: List<NoteEntity> = mutableListOf(note)
                 adapter?.setNotes(notes)
             }else{
                 getAllNotes()
             }
         }
    }
}