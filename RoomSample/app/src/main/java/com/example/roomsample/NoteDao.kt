package com.example.roomsample

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Query("select * from note_table order by id asc")
    suspend fun getAllNotes(): List<NoteEntity>

    @Query("select * from note_table where title LIKE :title")
    suspend fun findNoteByTitle(title: String): NoteEntity

    @Insert
    suspend fun insert(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)


}