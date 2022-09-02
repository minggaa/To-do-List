package ch.makery.address.model

import scalafx.beans.property.{ObjectProperty, StringProperty, LongProperty, BooleanProperty}
import scalafx.collections.ObservableBuffer
import scala.util.{ Try, Success, Failure }
import ch.makery.address.util.DateFormat._
import ch.makery.address.util.Database
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import scalikejdbc._


/**
* This part of the code was worked on and referred to online, as well as from Dr. Chin's Lab code.
**/
class Tasks (
    val priorityS: String, 
    val titleS: String, 
    val dueD: String
) extends Database {

    def this()      = this(null, null, null)
    var taskID      = LongProperty(0)
    var checked     = BooleanProperty(false)  //work on this!!
    var priority    = StringProperty(priorityS)
    var title       = new StringProperty(titleS)
    var due         = ObjectProperty[LocalDate](ToStringFormatter(dueD).asLocalDate)
    var description = new StringProperty("")


    def isExist: Boolean = {
        DB readOnly { implicit session =>
            sql"""
                select * from tasks 
                where id = ${taskID.value}
            """.map(rs => rs.string("title")).single.apply()
        } match {
            case Some(x) => true
            case None => false
        }
    }
    
    def save(): Try[Long] = {
    //  if !exists: task info will be saved according to the id value generated and assigned
        if (!(isExist)) {
            Try(DB autoCommit { implicit session => taskID.value = sql"""
                    insert into tasks (checked, priority, title, due, description) values 
                    (${checked.value}, ${priority.value}, ${title.value}, ${due.value.asString}, ${description.value})
                """.updateAndReturnGeneratedKey.apply();    taskID.value
            })
        } else {
    //  if exists: then task info will be updated by following the id value instead of the tasks title (prevents duplication or errors)
            Try(DB autoCommit { implicit session => sql"""
                update tasks 
                set
                checked     = ${checked.value},
                priority    = ${priority.value},
                title       = ${title.value},
                due         = ${due.value.asString},
                description = ${description.value}
                    where id = ${taskID.value}
                """.update.apply()
            })
        }
    }

    def saveCheck(): Try[Int] = {
        if (isExist) {
            Try(DB autoCommit { implicit session => sql"""
                update tasks 
                set
                checked     = ${checked.value}
                    where id = ${taskID.value}
                """.update.apply()
            })
        } else 
            throw new Exception("Cannot update checked.")
    }

    def delete(): Try[Int] = {
        if (isExist) {
            Try(DB autoCommit { implicit session => 
            sql"""
                delete from tasks 
                where id = ${taskID.value}
                """.update.apply()
            })
        } else 
            throw new Exception("Task does not exist in the database.")
    }
}

object Tasks extends Database{
    
    val tasksData = new ObservableBuffer[Tasks]()

    def apply (
            taskIDL:      Long,
            checkedB:     Boolean,
            priorityS:    String,
            titleS:       String, 
            dueS:         String,
            descriptionS: String
        ) : Tasks = {

        new Tasks(priorityS, titleS, dueS) {
            taskID.value        = taskIDL
            checked.value       = checkedB
            description.value   = descriptionS
        }
    }

    def initializeTable() = {
        DB autoCommit { implicit session => 
            sql"""
            create table tasks (
              id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
              checked boolean,
              priority varchar(64), 
              title varchar(254), 
              due varchar(64),
              description varchar(254)
            )
            """.execute.apply()
        }
    }

    def getAllTasks: List[Tasks] = {
        DB readOnly { implicit session =>
            sql"select * from tasks".map(rs => Tasks(
                rs.long("id"),
                rs.boolean("checked"),
                rs.string("priority"), 
                rs.string("title"),
                rs.string("due"), 
                rs.string("description")
            )).list.apply()
        }
    }
}