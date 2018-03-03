package server

import org.litote.kmongo.*

val mon = KMongo.createClient("localhost",27017)
val db = mon.getDatabase("Cicero")
val col = db.getCollection<User>("Users")

//Registrazione di un nuovo utente
fun MongoReg(user: User): Boolean
{
    try {
        if (col.findOneById(user.mail) != null) {
            return false
        }
        else
        {
            col.insertOne(user)
            return true
        }
    } catch (iae: IllegalArgumentException){
        return false
    }
}

//Login di un utente registrato
fun MongoLog(login: Login) : Boolean
{
    try {
        val log = col.findOneById(login.mail)
        if ( log != null )
        {
            return log.pass == login.pass
        }
        else
            return false
    }
    catch (iae: IllegalArgumentException)
    {
        return false
    }
}