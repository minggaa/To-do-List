package ch.makery.address.util

import ch.makery.address.model.Tasks
import scalikejdbc._


/**
* This part of the code was worked on and referred to online, as well as from Dr. Chin's Lab code.
**/
trait Database {
    val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"

    val dbURL = "jdbc:derby:myDB;create=true;";
    // initialize JDBC driver & connection pool
    Class.forName(derbyDriverClassname)

    ConnectionPool.singleton(dbURL, "me", "mine")

    // ad-hoc session provider on the REPL
    implicit val session: DBSession = AutoSession
}

object Database extends Database{
    def setupDB() = {
        if (!hasDBInitialize)
            Tasks.initializeTable()
        }

    def hasDBInitialize : Boolean = {
        DB getTable "Tasks" match {
            case Some(x) => true
            case None => false
        }
    }
}

