package code.onecard

import java.lang.Exception
import java.lang.IllegalArgumentException


class OneCardGame{

    companion object{
        const val MAX_CARD = 14
        const val STARTING_CARD_COUNT = 7

    }

    val mainDeck = Card.getDeck() // 남은카드
    val baseDeck = ArrayList<Card>() // 나온카드
    val players = ArrayList<Player>()
    private val diedPlayers = ArrayList<Player>()
    private val livingPlayers = ArrayList<Player>()
    private var turnDirection = 1
    var eatStack = 0
        private set
    private var turnPlayerN = 0

    fun new(maxPlayer: Int): List<Player>{
        val playerL = ArrayList<Player>(maxPlayer)
        repeat(maxPlayer){
            Player(it.toString(),randomCard(STARTING_CARD_COUNT)).
            also(playerL::add).
            also(livingPlayers::add).
            also(players::add)
        }
        return playerL
    }

    fun start(){
        broadcast("Game started, your Deck")
        baseDeck += randomCard()
        players.forEach { it.showInterface(this) }
    }

    fun runTurn(player: Player, card: Card?): Boolean{
        if(player != getNowTurnPlayer()) return false // 자신의 턴이 아닌데 요청을 보내는 경우

        if(card == null) {
            if(eatStack != 0) player.deck.addAll(randomCard(eatStack)).also { eatStack = 0 } else player.deck.add(randomCard())
            checkPlayerCardLimit(player)
            nextTurn()
            showUI()
            return true
        }
        if (
            !player.deck.contains(card) ||
            !(lastPublicCard() isMatchedWith card) ||
            eatStack != 0 && !(baseDeck.last() canDefendedWith card)
        ) return false


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

        //Abilities
        if(card.cardNumber == 13) {//한번 더냄
            showUI()
            return true
        }
        if(card.cardNumber == 12) turnDirection *= -1
        if(card.cardNumber == 11) nextTurn()//차례 한번 건너뜀
        nextTurn()
        showUI()
        return true
    }
    private fun nextTurn(){
        turnPlayerN += turnDirection
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
        players.forEach { it.sendMessage(msg) }
    }
    private fun kill(player: Player, reason: String = "None"){
        livingPlayers.remove(player)
        diedPlayers.add(player)
        player.sendMessage("you died: $reason")
    }
    private fun randomCard(): Card {
        //mainDeck.shuffle()
        return randomCard(1).first()
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

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + deck.hashCode()
        return result
    }
}
data class Card(val shape : CardType, val cardNumber: Int){
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
            1 ->    "A${shape.toString().slice(0..0)}"
            11 ->   "J${shape.toString().slice(0..0)}"
            12 ->   "Q${shape.toString().slice(0..0)}"
            13 ->   "K${shape.toString().slice(0..0)}"
            else -> "$cardNumber${shape.toString().slice(0..0)}"
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