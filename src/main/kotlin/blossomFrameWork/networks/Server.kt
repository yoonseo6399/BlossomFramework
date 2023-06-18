package blossomFrameWork.networks

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket


class Server private constructor(val port: Int) {
    val socket: ServerSocket = ServerSocket(port)
    val clients = ArrayList<Client>()
    lateinit var onRequestConnections: (Server) -> Unit
    companion object{
        fun create(port: Int): Server {
            return Server(port)
        }
    }

    private fun run(){
        CoroutineScope(Dispatchers.IO+ Job()+CoroutineName("Server#$port-Accept")).launch {
            while (isActive){
                clients += Client(socket.accept())
                onRequestConnections(this@Server)
                delay(10)
            }
        }
        CoroutineScope(Dispatchers.IO+ Job()+CoroutineName("Server#$port-Stream")).launch {
            while (isActive){
                if(clients.isNotEmpty()){
                    for (client in clients) {
                        val ins = BufferedReader(InputStreamReader(client.socket.getInputStream()))
                        val out = PrintWriter(client.socket.getOutputStream(), true)
                    }
                }
                delay(10)
            }
        }
    }

    fun onRequestConnect(consumer: (Server) -> Unit){
        consumer(this)
    }
    fun readClientMessage(client: Client){

    }
}
class Client(val socket: Socket){

}