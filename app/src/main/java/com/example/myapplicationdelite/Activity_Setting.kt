package com.example.myapplicationdelite

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Activity_Setting : AppCompatActivity() {
    var tv_date: TextView? = null
    var et_all_money: EditText? = null
    var tv_save: TextView? = null
    var tv_money_on_day: TextView? = null
    var all_money: Int? = null
    var all_days: Int? = null
    var date: Date? = null
    var dateString: String? = ""


    override fun onStart() {
        super.onStart()
        // Сообщение в Лог
        Log.d("MyLog", ".")
        Log.d("MyLog", "onStart 2ого активити!")

//      *** INTENT from MainActivity ***
        intent_main_act()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        // Сообщение в Лог
        Log.d("MyLog", ".")
        Log.d("MyLog", ".")
        Log.d("MyLog", ".")
        Log.d("MyLog", "Начало 2ого активити!")

        tv_date = findViewById(R.id.tv_date)
        tv_save = findViewById(R.id.tv_save)
        et_all_money = findViewById(R.id.et_all_money)
        tv_money_on_day = findViewById(R.id.tv_money_on_day)
        tv_date?.text = SimpleDateFormat("d MMMM").format(System.currentTimeMillis())

//      *** Получение даты ***
        get_date()
//      слушатели нажатия кнопок
        tv_save?.setOnClickListener {
            Log.d("MyLog", ".")
            Log.d("MyLog", "Идет сохранение из 2-ого активити")


            val intent = Intent(this, MainActivity::class.java).apply {
//                Передаем скорректированную "сумму до конца периода" на основной экран
                // Проверка данных
                Log.d("MyLog", "Передаем скорректированную all_money на основной экран")

                if (Integer.parseInt(et_all_money?.text.toString()) == all_money) {
                    // сумма не менялась
                    Log.d(
                        "MyLog",
                        "   Не менялась! = " + all_money
                    )
                } else Log.d(
                    "MyLog",
                    "   Новая сумма на весь период = " + Integer.parseInt(et_all_money?.text.toString())
                )
                putExtra("cash1", Integer.parseInt(et_all_money?.text.toString()))


//                Передаем скорректированную "dateString" на основной экран
                // Проверка данных
                Log.d("MyLog", "Передаем скорректированную dateString на основной экран")
                if (dateString == "") {
                    dateString = intent.getStringExtra("date")
                    // Дата не менялась
                    Log.d(
                        "MyLog",
                        "   Не менялась! = " + dateString
                    )
                } else Log.d("MyLog", "   Новая дата = " + dateString)
                putExtra("date1", dateString)
            }
            startActivity(intent)
        }
//      Слушатель нажатия на "et_all_money"
        et_all_money?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            all_days = intent.getIntExtra("days", -1)
            all_money = Integer.parseInt(et_all_money?.text.toString())

            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER
                || actionId == EditorInfo.IME_ACTION_DONE
            ) {
                //сделать, что нужно по нажатию на ENTER

                tv_money_on_day?.text = ((all_money!! / all_days!!).toString() + " в день  ")
            }
            false

        })
    }

    override fun onStop() {
        super.onStop()
        // Сообщение в Лог
        Log.d("MyLog", ".")
        Log.d("MyLog", "Остановка 2ого активити!")
        Log.d("MyLog", ".")
    }
    //      *** INTENT from MainActivity ***
    fun intent_main_act() {
        all_money = intent.getIntExtra("cash", -1)
        et_all_money?.setText(all_money.toString())
        tv_money_on_day?.text = (intent.getIntExtra("cash_on_day", -1).toString() + " в день  ")
//        tv_date?.text = "до" + my_format_day(intent.getStringExtra("date"))
        val date2 = intent.getStringExtra("date")
        tv_date?.text = "до " + date2

//        Проверка данных
        Log.d("MyLog", "et_all_money при старте= " + intent.getIntExtra("cash", -1).toString())
        Log.d("MyLog", "tv_date при старте= " + "до " + date2)
    }
    //      Переводим в красивый формат даты
    fun my_format_day(date: String): String {
        val originalFormat: DateFormat = SimpleDateFormat("dd MM yyyy")
        val targetFormat: DateFormat = SimpleDateFormat("d MMMM")
        val date_end: Date = originalFormat.parse(date)
        val formattedDate: String = targetFormat.format(date_end)
        return formattedDate
    }

    fun get_date() {
//        Все что связано с датами и днями
        var cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

//                // создаем специальный шаблон даты для расчета кол-ва дней
                val mySaveFormat = "dd MM yyyy"
                val sdf1 = SimpleDateFormat(mySaveFormat, Locale.US)
                // дата окончания периода
                dateString = sdf1.format(cal.time) // Переводим Calendar в строку
                date = sdf1.parse(dateString) // Переводим строку в формат Даты
                tv_date?.text = "до " + my_format_day(dateString!!) // Показываем до какого числа

                if (!dateString.equals("null")) {
                    all_days = calculation_of_day(dateString!!)
                } else all_days = intent.getIntExtra("days", -1)

                // Проверка данных
                Log.d("MyLog", "all_days во 2ом Активити = " + all_days.toString())

                // Проверка данных
                Log.d("MyLog", "*** *** ***")
                Log.d("MyLog", "all_money во 2ом Активити = " + all_money.toString())

                Log.d("MyLog", "all_days во 2ом Активити = " + all_days.toString())

                tv_money_on_day?.text = ((all_money!! / all_days!!).toString() + " в день  ")
                Log.d("MyLog", "money_on_day во 2ом Активити = " + tv_money_on_day?.text)
                Log.d("MyLog", "*** *** ***")

            }

        //        Вызываем всплывающее окно с календарем
        tv_date?.setOnClickListener {
            DatePickerDialog(
                this@Activity_Setting, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }

    fun calculation_of_day(date: String): Int {
        var date2 = date
//        var date2 = "11 08 2021"
        if (date.equals("-1") || date.equals("") || date.equals("null")) {
            date2 = "25 07 2092"
            /*
                // Проверка данных
                Log.d("MyLog", "*** calculation_of_day ***")
                Log.d("MyLog", "Дата не загрузилась, значение по умолчанию: " + date2)
                Log.d("MyLog", "*** end calculation_of_day ***")
                */
        }
//          Создаем специальный шаблон даты для расчета кол-ва дней
        val mySaveFormat = "dd MM yyyy"
        val sdf1 = SimpleDateFormat(mySaveFormat, Locale.US)
        // записываем сегодняшнюю дату в нужном нам формате
        val inputString1 = sdf1.format(Date())
        val date1: Date = sdf1.parse(inputString1)
        var date = sdf1.parse(date2) // Переводим строку в формат Даты
        val diff = date.time - date1.time
        var all_days =
            Integer.parseInt(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toString()) + 1

        //          Выводим кол-во дней, заранее форматируем
        return all_days
    }
}
