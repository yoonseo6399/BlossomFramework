package code

import blossomFrameWork.log
import blossomFrameWork.logWithoutln
import java.lang.IllegalArgumentException


class OneCard_Game{

    private val mainDeck = Card.getDeck() // 남은카드
    private val baseDeck = ArrayList<Card>() // 나온카드
    private val players = ArrayList<Player>()
    private val diedPlayers = ArrayList<Player>()
    private val livingPlayers = ArrayList<Player>()

    fun new(maxPlayer: Int){
        repeat(maxPlayer){
            addPlayer(Player("",randomCard(7)))
        }
    }

    fun start(){
        broadcast("Game started, your Deck")
        players.forEach(Player::showDeck)
        baseDeck.add(randomCard())
        broadcast("Public card : ${baseDeck.last()}")
    }

    fun runTurn(player: Player,card: Card): Boolean{
        if(!(baseDeck.last() isMatchedWith card)) return false


    }

    fun broadcast(msg: String){
        players.forEach{ it.sendMessage(msg) }
    }
    fun broadcastToDied(msg: String){
        diedPlayers.forEach{ it.sendMessage(msg) }
    }
    private fun kill(player: Player,reason: String = "None"){
        livingPlayers.remove(player)
        diedPlayers.add(player)
        player.sendMessage("you died: $reason")
    }
    private fun addPlayer(player: Player){
        players.add(player)
        livingPlayers.add(player)
    }
    private fun randomCard(): Card {
        //mainDeck.shuffle()
        return mainDeck[(Math.random()*mainDeck.size).toInt()]
    }
    private fun randomCard(limit : Int): ArrayList<Card> {
        //mainDeck.shuffle()
        val deck = ArrayList<Card>()
        repeat(limit){
            deck += mainDeck[(Math.random()*mainDeck.size).toInt()]
        }
        return deck
    }
}

data class Player(val name: String,var deck: ArrayList<Card>){
    fun connect(port: Int){
        TODO("네트워크 구현예정")
    }

    fun sendMessage(message : String){
        println("[$this] $message")
    }
    fun sendMessageWithOutln(message : String){
        println("[$this] $message")
    }
    fun showDeck(){
        for (card in deck) {
            sendMessageWithOutln(card.toString())
        }
    }
}
data class Card(val shape : CardType,val cardNumber: Int){
    companion object{
        fun getDeck(): ArrayList<Card> {
            val deck = ArrayList<Card>(60)
            repeat(4*13+2){
                deck += Card(CardType.values()[it/13],it%13+1)
            }
            return deck
        }

    }
    init{
        if(cardNumber <= 0 || cardNumber > 13)throw IllegalArgumentException("cardNumber must be between 1 and 13")
    }

    infix fun isMatchedWith(card: Card) = this.shape == card.shape || this.cardNumber == card.cardNumber

    override fun toString(): String {
        return when(cardNumber) {
            1 -> "$shape:A"
            11 -> "$shape:J"
            12 -> "$shape:Q"
            13 -> "$shape:K"
            else -> "$shape:$cardNumber"
        }
    }
}
enum class CardType{
    Spade,Diamond,Heart,Clobber,Joker
}
// 10 J Q K(13)