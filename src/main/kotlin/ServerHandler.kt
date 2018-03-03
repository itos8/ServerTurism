package server

import com.google.gson.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

//Converte una stringa in JSON
fun parseStringToJSON (str: String) : JsonObject
{
    val gson = Gson()
    val element = gson.fromJson(str, JsonElement::class.java)
    val jsonObj = element.asJsonObject

    return jsonObj
}

//Converte un JSON nella classe user
fun UserFromJSON (obj : JsonObject): User
{
    val gs = Gson()

    val user = gs.fromJson(obj, User::class.java)

    return user
}

//Invio della risposta alla richiesta di registrazione
fun sendResponseReg(res : String, addr: InetAddress)
{
    val obj = JsonObject()
    obj.addProperty("response", res)

    val client = DatagramSocket()
    val ba = obj.toString().toByteArray()
    //val ad = InetAddress.getByName(addr.toString())
    val dp = DatagramPacket(ba, ba.size, addr, 8890)
    client.send(dp)
    println("Risposta inviata : $res")
}

//Funzione di registrazione di un nuovo utente
fun register(user: User, addr: InetAddress)
{
    println(addr.hostAddress)
    if (MongoReg(user))
        sendResponseReg("Yes", addr)
    else
        sendResponseReg("No", addr)
}

//Funzione che parte quando viene ricevuto un pacchetto
fun receive (obj : String, addr: InetAddress)
{
    val jsonobj = parseStringToJSON(obj)

    when
    {
        jsonobj.has("register") -> register(UserFromJSON(jsonobj.getAsJsonObject("register")), addr)
    }
}