package code

import java.lang.Exception
import java.lang.IllegalArgumentException


class OneCardGame{

    companion object{
        const val MAX_CARD = 14
        const val STARTING_CARD_COUNT = 7

    }

    private val mainDeck = Card.getDeck() // 남은카드
    private val baseDeck = ArrayList<Card>() // 나온카드
    private val players = ArrayList<Player>()
    private val diedPlayers = ArrayList<Player>()
    private val livingPlayers = ArrayList<Player>()
    var eatStack = 0
        private set
    private var turnPlayerN = 0

    fun new(maxPlayer: Int): List<Player>{
        val playerL = ArrayList<Player>(maxPlayer)
        repeat(maxPlayer){
            addPlayer(Player(it.toString(),randomCard(STARTING_CARD_COUNT)).also(playerL::add))
        }
        return playerL
    }

    fun start(){
        broadcast("Game started, your Deck")

        baseDeck += randomCard()
        players.forEach { it.showInterface(this) }

    }

    fun runTurn(player: Player,card: Card?): Boolean{
        if(player != getNowTurnPlayer()) return false // 자신의 턴이 아닌데 요청을 보내는 경우

        if(card == null) { //

            if(eatStack != 0){// 포기했으니 먹이기(먹을수있으면)
                player.deck.addAll(randomCard(eatStack))
                eatStack = 0
                checkPlayerCardLimit(player)
                nextTurn()
                showUI()
                return true
            }

            player.deck.add(randomCard())
            checkPlayerCardLimit(player)
            nextTurn()
            showUI()
            return true
        }

        if(!player.deck.contains(card)) return false

        if(!(baseDeck.last() isMatchedWith card)) return false
        //유효성검사





        if(eatStack != 0 && !(baseDeck.last() canDefendedWith card)){// 먹어야하는 상황이고 받아칠수있는 카드를 낸게 아닐떄
            //player.deck.addAll(randomCard(eatStack))// 겹치는거 알지? 위랑? 메서드로 만들면 보기 좋을듯
            //eatStack = 0
            //checkPlayerCardLimit(player)
            //nextTurn()
            return false
        }

        //공격카드면 먹기스텍 추가
        card.getAttackStat().takeIf { it != -1 }?.let { eatStack += it }
        //어빌리티 구현해야함ㅇㅇ
        baseDeck.add(card)
        player.deck.remove(card)
        if(player.deck.size <= 0) {
            broadcast("${player.name} won!")
            try {
                gameEnd()
            } catch (_: Exception) {}
        }
        if(card.cardNumber == 13) {//한번 더냄
            showUI()
            return true
        }
        if(card.cardNumber == 11) nextTurn()//차례 한번 건너뜀
        nextTurn()
        showUI()
        return true
    }
    private fun nextTurn(){
        turnPlayerN++
        if(turnPlayerN > players.size-1) turnPlayerN = 0
        if(diedPlayers.contains(getNowTurnPlayer())) {
            nextTurn()
            return
        }
    }
    fun showUI(){
        players.forEach { it.showInterface(this) }

    }
    private fun checkPlayerCardLimit(player: Player):Unit = if(player.deck.size > MAX_CARD) kill(player,"${player.name} has over the card Limit: $MAX_CARD") else Unit

    private fun gameEnd(){
        broadcast("Game Ended!")
        throw GameEndException()
    }
    class GameEndException: Exception("don't worry, it just use for game end")
    fun lastPublicCard(): Card = baseDeck.last()


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
        return mainDeck[(Math.random()*mainDeck.size).toInt()].also(mainDeck::remove)
    }
    private fun randomCard(limit : Int): ArrayList<Card> {
        //mainDeck.shuffle()
        val deck = ArrayList<Card>()
        repeat(limit){
            try {
                deck += mainDeck.random().also(mainDeck::remove)
            } catch (_: Exception) {
                broadcast("all card already used, shuffle basedeck")
                for((i,e) in baseDeck.withIndex()){
                    if(i == baseDeck.size-1) continue
                    mainDeck.add(e)
                }

            }
        }
        return deck
    }

    fun getNowTurnPlayer() = players[turnPlayerN]
}

data class Player(val name: String,var deck: ArrayList<Card>){
    fun connect(port: Int){
        TODO("네트워크 구현예정")
    }

    fun sendMessage(message : String){
        println("[${this.name}] $message")
    }
    fun sendMessageWithOutln(message : String){
        println("[${this.name}] $message")
    }
    fun showDeck(){
        for (card in deck) {
            sendMessageWithOutln(card.toString())
        }
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is Player && other.name == name
    }

    fun showInterface(game: OneCardGame){
        sendMessage("public card: ${game.lastPublicCard()}")
        sendMessage("eatStack : ${game.eatStack}, Turn : #${game.getNowTurnPlayer().name}")
        sendMessage("")

        sendMessage("your deck:")
        for ((i,card) in deck.withIndex()) {
            sendMessageWithOutln("\t$i-$card")
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
        val SPADE_ACE = Card(CardType.Spade,1)

    }
    init{
        if(cardNumber <= 0 || cardNumber > 13)throw IllegalArgumentException("cardNumber must be between 1 and 13")
    }

    infix fun isMatchedWith(card: Card) = this.shape == card.shape || this.cardNumber == card.cardNumber || card.shape == CardType.Joker
    infix fun canDefendedWith(card: Card): Boolean =
        if(this.shape == CardType.Joker) {
            if(this.cardNumber == 1) {
                card == SPADE_ACE // 컬러조커 막는 코드
            }else card.cardNumber == 1 // 그냥 조커 에이스로 막는 코드
        }else if(this.cardNumber == 2){// 공격 2는 조커나 에이스나 3,2으로 막을수있음
            card.cardNumber == 1 || card.shape == CardType.Joker || card.cardNumber == 3 || card.cardNumber == 2
        }else if (cardNumber == 1){//에이스는 조커나 에이스로 막을수있음
            card.cardNumber == 1 || card.shape == CardType.Joker || card.cardNumber == 3
        }else true // 그게 아니라면 공격카드가 아니니 무슨카드든 올수있음

    fun isAttackCard(): Boolean = cardNumber == 1 || shape == CardType.Joker || cardNumber == 2
    fun getAttackStat(): Int =
        if(shape == CardType.Joker){
            if(cardNumber == 1) 3 else 5//black 3 color 5
        }else if(cardNumber == 2){
            2
        }else if(cardNumber == 1){
            3
        }else -1
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if(other is Card){
            return this.cardNumber == other.cardNumber && this.shape == other.shape
        }
        return false
    }

    override fun toString(): String {
        return when(cardNumber) {
            1 -> "$shape:A"
            11 -> "$shape:J"
            12 -> "$shape:Q"
            13 -> "$shape:K"
            else -> "$shape:$cardNumber"
        }
    }

    override fun hashCode(): Int {
        var result = shape.hashCode()
        result = 31 * result + cardNumber
        return result
    }
}
enum class CardType{
    Spade,Diamond,Heart,Clobber,Joker
}
enum class CardAbility {
    Spade,Diamond,Heart,Clobber,Joker
}
// 10 J Q K(13)