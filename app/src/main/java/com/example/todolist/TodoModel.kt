package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.FileDescriptor

@Entity
data class TodoModel(
    var title:String,           //title of the task
    var description:String,     // description of the tasak
    var category:String,        // category
    var time:Long,              // time
    var date:Long,              // date
    var isFinished:Int = 0,    // task finished or not
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0             //id
            )

