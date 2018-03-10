package server

import com.mongodb.client.model.geojson.Point
import org.litote.kmongo.*

private  val mon = KMongo.createClient("localhost",27017)
private val db = mon.getDatabase("Cicero")
private val col = db.getCollection<User>("Users")
private val places = db.getCollection<Place>("Places")

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

fun MongoPos(point: Point): List<Place>
{
    var list = mutableListOf<Place>()

    try {

        list.addAll(places.find("${MongoOperator.geoIntersects}:\n" +
                "                     {${MongoOperator.geometry}:{ \"type\" : \"Point\",\n" +
                "                          \"coordinates\" : ${point.coordinates} }\n" +
                "                      }"))

        return list
    }
    catch (e: NullPointerException)
    {
        return listOf()
    }
}

fun MongoNewPlace (place: Place) : Boolean
{
    try {
        if (places.findOneById(place.coordinates) != null) {
            return false
        }
        else
        {
            places.insertOne(place)
            return true
        }
    } catch (iae: IllegalArgumentException){
        return false
    }
}