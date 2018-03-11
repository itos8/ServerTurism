package server

import com.mongodb.MongoClient
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.geoWithin
import org.bson.codecs.pojo.PojoCodecProvider

import com.mongodb.client.model.geojson.Polygon
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries

private  val mon = MongoClient("localhost",27017)
private val db = mon.getDatabase("Cicero")
private var col = db.getCollection("Users", User::class.java)
private var places = db.getCollection("Places", Place::class.java)
private val codec = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()))

fun MongoCodec()
{
    col = col.withCodecRegistry(codec)
    places = places.withCodecRegistry(codec)
}

//Registrazione di un nuovo utente
fun MongoReg(user: User): Boolean
{
    try {
        if (col.find(eq("mail", user.mail)).count() > 0) {
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
        val log = col.find(eq("mail", login.mail)).first()
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

        list.addAll(places.find())

        return list
    }
    catch (e: NullPointerException)
    {
        return listOf()
    }
}

/*places.find("{area:\n" +
        "                       {${MongoOperator.geoIntersects}:\n" +
                "                       {${MongoOperator.geometry}:{ \"type\" : \"Point\",\n" +
                "                          \"coordinates\" : ${point.coordinates.values} }\n" +
                "                      }\n}\n}")*/

fun MongoNewPlace (place: Place) : Boolean
{
    try {
        if (places.find(eq("coordinates", place.coordinates)).count() > 0) {
            return false
        }
        else
        {
            places.insertOne(place)
            return true
        }
    } catch (iae: Exception){
        return false
    }
}