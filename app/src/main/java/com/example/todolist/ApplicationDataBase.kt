package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoModel::class],version = 1)
abstract class ApplicationDataBase :RoomDatabase(){

    abstract fun todoDAO():TodoDAO


    companion object{

        @Volatile
        private var INSTANCE:ApplicationDataBase? = null

        fun getDatabase(context: Context):ApplicationDataBase{
            val tempInstance = INSTANCE
            if(tempInstance != null) return tempInstance

            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, ApplicationDataBase::class.java,
                    DataBaseName).build()
                INSTANCE = instance
                return instance
            }


        }

    }

}