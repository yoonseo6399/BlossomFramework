package code

import blossomFrameWork.log
import blossomFrameWork.logWithoutln
import java.lang.IllegalArgumentException

class OneCard_Game {
    fun run(){
        for (card in Card.getDeck()) {
            println(card)
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