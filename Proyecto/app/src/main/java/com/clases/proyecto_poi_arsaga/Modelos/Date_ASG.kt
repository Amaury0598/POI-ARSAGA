package com.clases.proyecto_poi_arsaga.Modelos

import java.util.*

class Date_ASG(

) {

    companion object {
        fun getActualDate(): String {
            val cal = Calendar.getInstance()
            val Day = cal.get(Calendar.DAY_OF_MONTH)
            val Month = cal.get(Calendar.MONTH)
            val Year = cal.get(Calendar.YEAR)
            var fDay = Day.toString()
            var fMonth = Month.toString()
            var fYear = Year.toString()
            if (Day < 10) {
                fDay = "0$fDay"
            }
            if (Month < 10) {
                fMonth = "0$fMonth"
            }
            return "$fDay/$fMonth/$fYear"
        }
        //Regresa verdadero si la fecha introducida es mayor a la actual
        fun compareToActualDate(date : String):Boolean{
            val actualDate = getActualDate()
            val fDay = date.substring(0,2)
            val fMonth = date.substring(3,5)
            val fYear = date.substring(6,10)
            val dateString = fYear+fMonth+fDay
            val dateInt = dateString.toInt()
            //                                          11/05/2021 => 20210511

            val aDay = actualDate.substring(0,2)
            val aMonth = actualDate.substring(3,5)
            val aYear = actualDate.substring(6,10)
            val aDateString = aYear+aMonth+aDay
            val aDateInt = aDateString.toInt()

            return dateInt >= aDateInt


        }

        fun canPayWithASGCoins(date : String): Boolean {
            val actualDate = getActualDate()
            val fDay = date.substring(0,2)
            val fMonth = date.substring(3,5)
            val fYear = date.substring(6,10)
            val dateString = fYear+fMonth+fDay
            val dateInt = dateString.toInt()
            dateInt + 1
            //                                          11/05/2021 => 20210511

            val aDay = actualDate.substring(0,2)
            val aMonth = actualDate.substring(3,5)
            val aYear = actualDate.substring(6,10)
            val aDateString = aYear+aMonth+aDay
            val aDateInt = aDateString.toInt()

            return dateInt >= aDateInt
        }
    }

}