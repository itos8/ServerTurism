package server

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.nio.charset.Charset

fun main(args : Array<String>)
{
    val listener = DatagramSocket(8889)

    var receiveData = ByteArray(1024)

    mongoCodec()

    while (true)
    {
        val packet = DatagramPacket(receiveData, receiveData.size)
        println("In attesa di connessioni")
        listener.receive(packet)

        println("Connessione stabilita")
        //val data = receiveData.toString(Charsets.UTF_8)
        val data = String(receiveData, packet.offset, packet.length, Charset.defaultCharset())

        Thread({
            receive(data, packet.address)
            }).start()
    }

}