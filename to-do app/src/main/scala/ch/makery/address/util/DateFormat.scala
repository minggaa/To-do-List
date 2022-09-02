package ch.makery.address.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import scalafx.beans.property.{ObjectProperty}


/**
* This part of the code was worked on and referred to online, as well as from Dr. Chin's Lab code.
**/
object DateFormat {
    val formatDate =  DateTimeFormatter.ofPattern("dd/MM/yyyy")

    implicit class ToDateFormatter(val date: LocalDate) {

        def asString: String = {
            if (date == null) {
                return null;
            }
            return formatDate.format(date)
        }
    }
    implicit class ToStringFormatter(val date: String) {
            
        def asLocalDate: LocalDate = {
            try {
                LocalDate.parse(date, formatDate)
            } catch {
                case  e: DateTimeParseException => null
            }
        }
    }
}
