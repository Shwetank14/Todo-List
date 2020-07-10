package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Array
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

const val DataBaseName = "Todo.db"
class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar // This object holds the value of day/month/Year types
    lateinit var dateSetListner:DatePickerDialog.OnDateSetListener    // Interface that will be invoked when we click on any value on the dialog of calender


    lateinit var timeSetListner:TimePickerDialog.OnTimeSetListener

    var finalDate = 0L
    var finalTime = 0L

    //for spinner labels

    val labels = arrayListOf<String>("Personal","Business","Insurance","Shopping","Banking","school","Work")


    val db by lazy{
        ApplicationDataBase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)


        dateId.setOnClickListener(this)
        timeInp.setOnClickListener(this)
        createTask.setOnClickListener(this)

        setupSpinner()


    }

    private fun setupSpinner() {

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labels)

        labels.sort()

        mySpinner.adapter = adapter
    }

    override fun onClick(v: View) {
        when(v.id)
        {
            R.id.dateId->{
                setListner()
            }
            R.id.timeInp->{
                setTimeListner()
            }
            R.id.createTask->{
                saveTodo()
            }
        }
    }

    private fun saveTodo() {
        val category = mySpinner.selectedItem.toString()
        val title = titleInp.editText?.text.toString()
        val description = descriptionInp.editText?.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO){
                return@withContext db.todoDAO().insertTask(
                    TodoModel(
                        title,
                        description,category,
                        finalDate,
                        finalTime

                    )
                )
            }
            finish()

            Toast.makeText(this@TaskActivity," New Task Added",Toast.LENGTH_SHORT).show()

        }
    }

    private fun setTimeListner() {
        myCalendar = Calendar.getInstance()

        timeSetListner = TimePickerDialog.OnTimeSetListener{ _: TimePicker, hourOfday: Int, minuteOfDay: Int ->
            myCalendar.set(Calendar.HOUR_OF_DAY,hourOfday)
            myCalendar.set(Calendar.MINUTE,minuteOfDay)

            updateTime()
        }
        val timePickerDialog = TimePickerDialog(this,timeSetListner,myCalendar.get(Calendar.HOUR_OF_DAY),
        myCalendar.get(Calendar.MINUTE),false)

        timePickerDialog.show()
    }

    private fun updateTime() {
        val mytimeFormat = "h:mm a"
        val sdf = SimpleDateFormat(mytimeFormat)
        finalTime = myCalendar.time.time
        timeInp.setText(sdf.format(myCalendar.time))
    }

    private fun setListner() {
        myCalendar = Calendar.getInstance()

        dateSetListner = DatePickerDialog.OnDateSetListener{ _: DatePicker, year: Int, month: Int, Day: Int ->
                    myCalendar.set(Calendar.YEAR,year)
                    myCalendar.set(Calendar.MONTH,month)
                    myCalendar.set(Calendar.DAY_OF_MONTH,Day)

            updateDate()
        }

        val datePickerDialog = DatePickerDialog(this,dateSetListner,myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()            // it sets the minimum date as the current time
        datePickerDialog.show()
    }

    private fun updateDate() {
        val myformat = "EEE, d MMM yyyy"

        val sDf = SimpleDateFormat(myformat)     //sdf = Simple date format
        finalDate = myCalendar.time.time
        dateId.setText(sDf.format(myCalendar.time))

        tmp.visibility = View.VISIBLE
    }
}