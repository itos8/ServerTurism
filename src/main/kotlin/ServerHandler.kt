package server

import com.google.gson.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress

//Converte una stringa in JSON
private fun parseStringToJSON (str: String) : JsonObject
{
    val gson = Gson()
    val element = gson.fromJson(str, JsonElement::class.java)

    return element.asJsonObject
}

private fun parseAnytoJSON(obj:Any): JsonObject
{
    val gson = Gson()

    val element = parseStringToJSON(gson.toJson(obj))

    return element
}

private fun parseListToJSON (list: List<Any>): JsonElement
{
    var element = JsonArray()

    var gs = Gson()

    for (entry in list)
    {
        element.add(gs.toJsonTree(entry,Any::class.java))
    }

    return element
}

//Converte un JSON nella classe user
private fun userFromJSON (obj : JsonObject): User
{
    val gs = Gson()

    return gs.fromJson(obj, User::class.java)
}

//Converte un JSON a un oggetto di tipo Login
private fun loginFromJSON (obj : JsonObject): Login
{
    val gs = Gson()

    val login = gs.fromJson(obj, Login::class.java)

    return login
}

private fun positionFromJSON(obj: JsonObject): Position
{
    val gs = Gson()

    return gs.fromJson(obj, Position::class.java)
}

private fun placeFromJSON (obj : JsonObject) : Place
{
    val gs = Gson()

    return gs.fromJson(obj, Place::class.java)
}

//Invio della risposta alla richiesta di registrazione
private fun sendResponse(res : String, addr: InetAddress)
{
    val obj = JsonObject()
    obj.addProperty("response", res)

    val client = DatagramSocket()
    val ba = obj.toString().toByteArray()
    val dp = DatagramPacket(ba, ba.size, addr, 8890)
    client.send(dp)
    println("Risposta inviata : $res")
}

private fun sendPlacesList(list : List<Place>, addr: InetAddress)
{
    val obj = JsonObject()
    obj.add("list", parseListToJSON(list))

    println(obj.toString())
    val client = DatagramSocket()
    val ba = obj.toString().toByteArray()
    val dp = DatagramPacket(ba, ba.size, addr, 8890)
    client.send(dp)
}

//Funzione di registrazione di un nuovo utente
private fun register(user: User, addr: InetAddress)
{
    try {
        val mail = InternetAddress(user.mail)
        mail.validate()
    }
    catch (ae: AddressException) {
        sendResponse("No", addr)
        return
    }
    println(addr.hostAddress)
    if (mongoReg(user))
        sendResponse("Yes", addr)
    else
        sendResponse("No", addr)
}

//Funzione di login
private fun login(login: Login, addr: InetAddress)
{
    println(addr.hostAddress)
    if (mongoLog(login))
        sendResponse("Yes", addr)
    else
        sendResponse("No", addr)
}

private fun geolocalization(position: Position, addr: InetAddress)
{
    sendPlacesList(mongoPos(position.coordinates!!), addr)
}

private fun newPlace(place: Place, addr: InetAddress)
{
    if (mongoNewPlace(place))
        sendResponse("Yes", addr)
    else
        sendResponse("No", addr)
}

//Funzione che parte quando viene ricevuto un pacchetto
fun receive (obj : String, addr: InetAddress)
{
    val jsonobj = parseStringToJSON(obj)

    when
    {
        jsonobj.has("register") -> register(userFromJSON(jsonobj.getAsJsonObject("register")), addr)
        jsonobj.has("login") -> login(loginFromJSON(jsonobj.getAsJsonObject("login")), addr)
        jsonobj.has("position") -> geolocalization(positionFromJSON(jsonobj.getAsJsonObject("position")), addr)
        jsonobj.has("newPlace") -> newPlace(placeFromJSON(jsonobj.getAsJsonObject("newPlace")), addr)
    }
}