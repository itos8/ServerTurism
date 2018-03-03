package server

import com.google.gson.Gson
import com.mongodb.client.FindIterable
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