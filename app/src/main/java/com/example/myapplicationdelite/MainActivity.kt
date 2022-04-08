package com.example.myapplicationdelite

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.media.ToneGenerator

import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity()/*, View.OnClickListener*/ {
    var btn_one: Button? = null
    var btn_two: Button? = null
    var btn_three: Button? = null
    var btn_four: Button? = null
    var btn_five: Button? = null
    var btn_six: Button? = null
    var btn_seven: Button? = null
    var btn_eight: Button? = null
    var btn_nine: Button? = null
    var btn_zero: Button? = null
    var btn_enter: Button? = null
    var btn_point: Button? = null
    var btn_backspace: Button? = null
    var tv_result: TextView? = null
    var tv_back: TextView? = null
    var tv_setting: TextView? = null
    var on_day: TextView? = null
    var tv_money_on_day: TextView? = null
    var money_on_day: Int = 0
    var money_on_day_old: Int = 0
    var all_days: Int = -1
    var all_money: Int? = -1
    var all_money_old: Int? = -1
    var date: String = ""
    var date_old: String = ""

    //      для сохранения данных в на флешку телефона
    lateinit var sPref_cash: SharedPreferences
    lateinit var sPref_days: SharedPreferences
    lateinit var sPref_cash_on_day: SharedPreferences
    lateinit var sPref_date: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_one = findViewById(R.id.btn_one)
        btn_two = findViewById(R.id.btn_two)
        btn_three = findViewById(R.id.btn_three)
        btn_four = findViewById(R.id.btn_four)
        btn_five = findViewById(R.id.btn_five)
        btn_six = findViewById(R.id.btn_six)
        btn_seven = findViewById(R.id.btn_seven)
        btn_eight = findViewById(R.id.btn_eight)
        btn_nine = findViewById(R.id.btn_nine)
        btn_zero = findViewById(R.id.btn_zero)
        btn_point = findViewById(R.id.btn_point)
        btn_enter = findViewById(R.id.btn_enter)
        btn_backspace = findViewById(R.id.btn_backspace)
        tv_back = findViewById(R.id.tv_back)
        tv_result = findViewById(R.id.tv_result)
        tv_money_on_day = findViewById(R.id.tv_money_on_day)
        tv_setting = findViewById(R.id.tv_setting)
        on_day = findViewById(R.id.on_day)

        // Сообщение в Лог
        Log.d("MyLog", ".")
        Log.d("MyLog", ".")
        Log.d("MyLog", ".")
        Log.d("MyLog", "Начало Главного активити!")


//      *** INTENT from two Activity ***
        intent_two_act()

//      *** Запуск приложения после закрытия ***
        start_after_close()

//      *** Если Данные во 2-ом Активити были изменены, то мы должны ЗАНОВО пересчитываем бюджет на день ***
        check_recount_money_on_day()


//        Показываем в активити бюджет на день
        tv_money_on_day?.text = money_on_day.toString()
//        Показываем в активити вариативную кнопку настроек
        tv_setting?.text = all_money.toString() + " на " + all_days.toString() + " дней  ⚙"
//      слушатели нажатия кнопок
        btn_one?.setOnClickListener {
            tv_result?.append("1")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_two?.setOnClickListener {
            tv_result?.append("2")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_three?.setOnClickListener {
            tv_result?.append("3")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_four?.setOnClickListener {
            tv_result?.append("4")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_five?.setOnClickListener {
            tv_result?.append("5")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_six?.setOnClickListener {
            tv_result?.append("6")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_seven?.setOnClickListener {
            tv_result?.append("7")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_eight?.setOnClickListener {
            tv_result?.append("8")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_nine?.setOnClickListener {
            tv_result?.append("9")
            money_on_day_chande()
            money_on_day_check_zero()
        }

        btn_zero?.setOnClickListener {
//          Если строка пустая, то не добавлять
            if (tv_result?.text?.isEmpty() == false) { // строка не пустая
                tv_result?.append("0")
                money_on_day_chande()
//                money_on_day_check_zero()
            } /*else if (tv_result?.text?.contains("0") == false) {
                tv_result?.append("0")
            }*/

        }

        btn_point?.setOnClickListener {
//          Если строка пустая, то не добавлять
            if (tv_result?.text?.isEmpty() == true) {
                tv_result?.append("0")
                tv_result?.append(".")
//          Если строка уже содержит точку, то больше не ставить
            } else if (tv_result?.text?.contains(".") == false) {
                tv_result?.append(".")
            }
        }

        btn_backspace?.setOnClickListener {

//            Это конечно же костыль, по другому не додумался
//            Если остался последний символ или Если строка пустая,
//            то возвращаем значение дневого бюджета на прежний уровень
            if (tv_result?.text?.length == 1) {
                tv_money_on_day?.text = money_on_day.toString()
            }
            if (tv_result?.text?.isEmpty() == true) {
                tv_money_on_day?.text = money_on_day.toString()
            }
//          Иначе (Если строка не пустая, то пересчитываем дневной бюджет)
            else {
//          Удаляем один символ в конце и записываем
                var previous_result = tv_result?.text?.dropLast(1)
                tv_result?.text = previous_result
                money_on_day_chande()
                money_on_day_check_zero()
            }
        }

        btn_enter?.setOnClickListener {
//          Если строка не пустая, то меняем дневной бюджет навсегда
            if (tv_result?.text?.isEmpty() == false) {
                // резервирум последнее значение для возможности его отмены (tv_back)
                money_on_day_old = money_on_day
                all_money_old = all_money


                var tv_result_s = tv_result?.text.toString()
//                Вычитаем из бюджета на день и из общей суммы
                money_on_day -= tv_result?.text.toString().toInt()
                all_money = all_money?.minus(tv_result?.text.toString().toInt())
//                И сразу запоказываем в нужных местах
                tv_setting?.text = all_money.toString() + " на " + all_days.toString() + " дней  ⚙"
                tv_result?.text = ""


                tv_back?.text = "❮  Вернуть $tv_result_s"
                tv_back?.isClickable
            }
        }

        tv_back?.setOnClickListener {
            // Возвращаем из резерва при отмене операции
            money_on_day = money_on_day_old
            all_money = all_money_old
//          И сразу запоказываем в нужных местах
            tv_setting?.text = all_money.toString() + " на " + all_days.toString() + " дней  ⚙"
            tv_money_on_day?.text = money_on_day.toString()

            tv_back?.isClickable
            tv_back?.text = ""
        }

        tv_setting?.setOnClickListener {
            Log.d("MyLog", ".")
            Log.d("MyLog", "Запуск слушателя нажатия на кнопку 'Настройки'")
            val intent = Intent()
            intent.setAction("Activity_Setting")
            Log.d("MyLog", "days (отправится во 2-е активити) = " + all_days)
            intent.putExtra("days", all_days)
            Log.d("MyLog", "all_money (отправится во 2-е активити) = " + all_money)
            intent.putExtra("cash", all_money)
            if (date.equals("-1")) {
                date = "25 07 2092"
            }
            Log.d("MyLog", "date (отправится во 2-е активити) = " + date)
            intent.putExtra("date", date)
            Log.d("MyLog", "money_on_day (отправится во 2-е активити) = " + money_on_day)
            intent.putExtra("cash_on_day", money_on_day)

            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        // Сообщение в Лог
        Log.d("MyLog", ".")
        Log.d("MyLog", ".")
        Log.d("MyLog", "Остановка Главного активити!")
        Log.d("MyLog", "Идет сохранение из Гл. активити")

        // Сохраняем "Общую сумму" в SharedPreferences
        sPref_cash = getPreferences(MODE_PRIVATE)
        var ed_cash: SharedPreferences.Editor = sPref_cash.edit()
        ed_cash.putString("SAVED_CASH", all_money.toString())
        ed_cash.commit()
        // Вывод сообщения о сохранении в Лог
        Log.d("MyLog", "all_money сохраняемое = " + all_money.toString())

        // Сохраняем "кол-во дней" в SharedPreferences
        sPref_days = getPreferences(MODE_PRIVATE)
        var ed_days: SharedPreferences.Editor = sPref_days.edit()
        ed_days.putString("SAVED_DAYS", all_days.toString())
        ed_days.commit()
        // Вывод сообщения о сохранении в Лог
        Log.d("MyLog", "all_days сохраняемое = " + all_days.toString())

        // Сохраняем "Бюджет на день" в SharedPreferences
        sPref_cash_on_day = getPreferences(MODE_PRIVATE)
        var ed_cash_on_day: SharedPreferences.Editor = sPref_cash_on_day.edit()
        ed_cash_on_day.putString("SAVED_CASH_ON_DAY", money_on_day.toString())
        ed_cash_on_day.commit()
        // Вывод сообщения о сохранении в Лог
        Log.d("MyLog", "money_on_day сохраняемое = " + money_on_day.toString())

        // Сохраняем "дату окончания" в SharedPreferences
        sPref_date = getPreferences(MODE_PRIVATE)
        var ed_date: SharedPreferences.Editor = sPref_date.edit()
        ed_date.putString("SAVED_DATE", intent.getStringExtra("date1"))
        ed_date.commit()
        // Вывод сообщения о сохранении в Лог
        Log.d("MyLog", "date сохраняемое = " + date)

        // И выводим сообщение о сохранении
        val toast = Toast.makeText(applicationContext, "SAVING DATA", Toast.LENGTH_SHORT).show()
    }

    //      *** INTENT from two Activity ***
    fun intent_two_act() {
        //        Если второе активити уже было открыто, то мы сможем получить значения
        Log.d("MyLog", "*** START INTENT ***")
        all_money = intent.getIntExtra("cash1", -1)
        Log.d("MyLog", "all_money (intent)= " + all_money)
        date = intent.getStringExtra("date1").toString()
        Log.d("MyLog", "date (accept intent)= " + date)

        all_days = calculation_of_day(date)
        Log.d("MyLog", "all_days = " + all_days)
        Log.d("MyLog", "*** end intent ***")
        Log.d("MyLog", ".")
    }

    //      *** Запуск приложения после закрытия ***
    fun start_after_close() {
        if (all_money == -1) {
            // Сообщение в Лог
            Log.d("MyLog", "*** Запуск приложения после закрытия ***")
//        if (all_money == -1 || all_days == -1) {


            // загружаем из SharedPreferences сохраненное кол-во дней
            sPref_days = getPreferences(MODE_PRIVATE)
            all_days = sPref_days.getString("SAVED_DAYS", "-1")?.toInt()!!
            // Сообщение в Лог
            Log.d("MyLog", "загружаем из SharedPreferences сохраненное кол-во дней")
            Log.d("MyLog", "all_days = " + all_days)

            // загружаем из SharedPreferences дату окончания периода
            sPref_date = getPreferences(MODE_PRIVATE)
            date = sPref_date.getString("SAVED_DATE", "-1").toString()
            // Сообщение в Лог
            Log.d("MyLog", "загружаем из SharedPreferences дату окончания периода")
            Log.d("MyLog", "date = " + date)

            // Загружаем из SharedPreferences сохраненные данные Оставшейся суммы на весь период
            sPref_cash = getPreferences(MODE_PRIVATE)
            all_money = sPref_cash.getString("SAVED_CASH", "-1")?.toInt()
            // Сообщение в Лог
            Log.d(
                "MyLog",
                "Загружаем из SharedPreferences деньги на весь период"
            )
            Log.d("MyLog", "all_money = " + all_money)

//          считаем снова кол-во дней
            // Сообщение в Лог
            Log.d("MyLog", ".")
            Log.d(
                "MyLog",
                "снова считаем кол-во дней до конца периода= " + calculation_of_day(date)
            )
            Log.d("MyLog", ".")

            Log.d("MyLog", ".")
            Log.d("MyLog", "*** Проверяем изменился ли день ***")

            // (НОВЫЙ ДЕНЬ) Если старое и новое значение "кол-ва дней" не равны, то
            if (calculation_of_day(date) != all_days) {

                // Сообщение в Лог
                Log.d("MyLog", ".")
                Log.d("MyLog", "НОВЫЙ ДЕНЬ!")
                Log.d("MyLog", "старое и новое значение 'количество дней' не равны")
                Log.d("MyLog", ".")

                // 1. загружаем из SharedPreferences сохраненные данные Оставшейся суммы на весь период
                sPref_cash = getPreferences(MODE_PRIVATE)
                all_money = sPref_cash.getString("SAVED_CASH", "-1")?.toInt()
                // Сообщение в Лог
                Log.d(
                    "MyLog",
                    "1. загружаем из SharedPreferences сохраненные деньги на весь период"
                )
                Log.d("MyLog", "all_money = " + all_money)

                // 2. загружаем из SharedPreferences сохраненные данные Оставшейся суммы бюджета на день
                sPref_cash_on_day = getPreferences(MODE_PRIVATE)
                money_on_day = sPref_cash_on_day.getString("SAVED_CASH_ON_DAY", "-1")?.toInt()!!
                // Сообщение в Лог
                Log.d(
                    "MyLog",
                    "2. загружаем из SharedPreferences сохраненный остаток бюджета на день"
                )
                Log.d("MyLog", "money_on_day = " + money_on_day)

                // 3. записываем новое кол-во дней до конца периода
                // Сообщение в Лог
                Log.d("MyLog", "3. записываем новое кол-во дней до конца периода")
                Log.d("MyLog", "all_days = " + calculation_of_day(date))
                all_days = calculation_of_day(date)


                // 4. Прибавляем оставшюся сумму старого бюджета на день к остальной сумме на весь период
                all_money = all_money?.plus(money_on_day)
                // Сообщение в Лог
                Log.d(
                    "MyLog",
                    "4. Прибавляем оставшюся сумму старого бюджета на день к остальной сумме на весь период"
                )
                Log.d("MyLog", "all_money = " + all_money)

                // 5. Рассчитываем снова бюджет на день
                money_on_day = all_money!! / all_days
                Log.d("MyLog", "5. Рассчитываем снова бюджет на день")
                Log.d("MyLog", "money_on_day = " + money_on_day)

//          И если все верно, то супер, иначе это не я
            } else { // Если сутки не изменились, то деньги на день остаются
//          Загружаем из SharedPreferences сохраненные данные Оставшейся суммы бюджета на день
                sPref_cash_on_day = getPreferences(MODE_PRIVATE)
                money_on_day = sPref_cash_on_day.getString("SAVED_CASH_ON_DAY", "-1")?.toInt()!!

                Log.d("MyLog", "День не менялся!")


                //            создаем короткий звуковой сигнал
//            уведомляющий о загрузке данных из SharedPreferences
                val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
//          и тостим для наглядности
                val toast =
                    Toast.makeText(applicationContext, "Load_CASH", Toast.LENGTH_SHORT).show()
            }
        }
        Log.d("MyLog", "*** end проверки дня ***")
        Log.d("MyLog", ".")
    }

    //      *** Проверка пересчета бюджета на день ***
    fun check_recount_money_on_day() {
        //    Каждый раз должно высчитываться количество дней, оставшееся до конца периода
//    и если это кол-во изменилось, то оставшаеся сумма за прошлый день прибавляется
//    к общей сумме и снова все делится на количество оставшихся дней

//        У нас кол-во дней сохраняется при выходе,
//        теперь, при каждом входе в приложение нужно снова посчитать кол-во дней до конца периода
//        и сравнивать эти числа, если они не равны, то


        //      загружаем из SharedPreferences дату окончания периода
        sPref_date = getPreferences(MODE_PRIVATE)
        date_old = sPref_date.getString("SAVED_DATE", "-1").toString()
//      загружаем из SharedPreferences сумму до окончания периода
        sPref_cash = getPreferences(MODE_PRIVATE)
        all_money_old = sPref_cash.getString("SAVED_CASH", "-1")?.toInt()


        Log.d("MyLog", "ПРОВЕРКА пересчета Бюджета на день")

        if (calculation_of_day(date) != all_days
//            1-ое условие: Если Новый день
            || (all_money != all_money_old && all_money != -1)
//            2-ое условие: Если Изменилась сумма на весь период
            || (!date_old.equals(date) && !date.equals("null"))
        )
//            3-ее условие: Если Изменилась дата конца периода
        {
            if (calculation_of_day(date) != all_days) {
                Log.d("MyLog", "*** *** ***")
                Log.d("MyLog", "calculation_of_day(date) = " + calculation_of_day(date))
                Log.d("MyLog", "all_days = " + all_days)
                Log.d("MyLog", "*** *** ***")
            }
            if (all_money != all_money_old) {
                Log.d("MyLog", "*** *** ***")
                Log.d("MyLog", "all_money = " + all_money)
                Log.d("MyLog", "all_money_old сохраненные = " + all_money_old)
                Log.d("MyLog", "*** *** ***")
            }
            if (!date_old.equals(date)) {
                Log.d("MyLog", "*** *** ***")
                Log.d("MyLog", "date_old сохраненные = " + date_old)
                Log.d("MyLog", "date = " + date)
                Log.d("MyLog", "*** *** ***")
            }


            Log.d("MyLog", "   Пересчитываем Бюджет на день!")
            money_on_day = all_money!! / all_days
        } else {
            //        Иначе оставляем сохраненную сумму

            // загружаем из SharedPreferences сохраненный бюджет на день
            sPref_cash_on_day = getPreferences(MODE_PRIVATE)
            money_on_day = sPref_cash_on_day.getString("SAVED_CASH_ON_DAY", "-1")?.toInt()!!
            // Сообщение в Лог
            Log.d("MyLog", "*** Изменений для бюджета на день НЕТ ***")
        }
//        Выводим полученный Бюджет на день
        Log.d("MyLog", "money_on_day = " + money_on_day)
        Log.d("MyLog", "*** end проверки ***")
        Log.d("MyLog", ".")
    }

    //    Функция изменяет дневной бюджет
    fun money_on_day_chande() {
        if (tv_result?.text?.isEmpty() == false) {
            tv_money_on_day?.text =
                (money_on_day - tv_result?.text.toString().toInt()).toString()
        }
    }

    //    Функция проверяет дневной бюджет на нулевое значение и меняет цвета
    fun money_on_day_check_zero() {
        if (tv_result?.text?.isEmpty() == false) {
            if (tv_result?.text.toString().toInt() >= money_on_day) {
                tv_money_on_day?.setTextColor(getResources().getColor(R.color.GAS_red))
            } else tv_money_on_day?.setTextColor(getResources().getColor(R.color.GAS_orange))
        }
    }

    //    Метод для подсчета колчичества дней от сегодняшнего дня до заданной даты
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