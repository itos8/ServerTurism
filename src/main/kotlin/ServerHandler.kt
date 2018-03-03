package server

import com.google.gson.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress

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

//Converte un JSON a un oggetto di tipo Login
fun LoginFromJSON (obj : JsonObject): Login
{
    val gs = Gson()

    val login = gs.fromJson(obj, Login::class.java)

    return login
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
    try {
        val mail = InternetAddress(user.mail)
        mail.validate()
    }
    catch (ae: AddressException) {
        sendResponseReg("No", addr)
        return
    }
    println(addr.hostAddress)
    if (MongoReg(user))
        sendResponseReg("Yes", addr)
    else
        sendResponseReg("No", addr)
}

//Funzione di login
fun login(login: Login, addr: InetAddress)
{
    println(addr.hostAddress)
    if (MongoLog(login))
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
        jsonobj.has("login") -> login(LoginFromJSON(jsonobj.getAsJsonObject("login")), addr)
    }
}