package code.onecard

import blossomFrameWork.board.Board
import blossomFrameWork.board.Entity
import blossomFrameWork.functions.alsoPrint
import blossomFrameWork.info
import kotlinx.coroutines.*
import java.awt.Image
import java.awt.Point
import java.util.*
import javax.swing.ImageIcon
import javax.swing.SwingUtilities
import kotlin.collections.HashMap

object OnecardBoard {

    val board = Board(1000,1000)
    val cardImgPath = "A:\\BlossomFramework\\src\\main\\kotlin\\code\\onecard\\cards\\"
    val entityMap = HashMap<String,Entity>()
    var loc = -5
    val game = OneCardGame()
    init {


/*            val img = ImageIcon(ImageIcon(
                "${cardImgPath}4S.png".alsoPrint()
            ).image.getScaledInstance(90,150, Image.SCALE_SMOOTH))

            Entity(img).let {
                it.location = Point(100,100*//*board.width/2, board.width/2*//*)
                board.add(it)

                runBlocking {
                    it.smoothMove(loc,10,100)
                }
            }*/


        //displayCard(Card.getDeck(),Card.getDeck())



        //게임진행 code
        with(game){
            new(2).let {
                start()
                displayCard(it[0].deck,it[1].deck)
                val scan = Scanner(System.`in`)
                while (true){
                    if(getNowTurnPlayer() == it[0]){
                        while (!runTurn(it[0], it[0].deck.getOrNull(scan.nextInt()))) {
                            println("false#0")
                        }
                        println("true#0")
                    }else{
                        while (!runTurn(it[1], it[1].deck.getOrNull(scan.nextInt()))) {
                            println("false#1")
                        }
                        println("true#1")
                    }
                }
            }
        }



    }

    fun displayCard(deck1: List<Card>,deck2: List<Card>){
        //board.removeAll()
        loc = -10
        for ((i,card) in deck1.withIndex()) {
            val img = ImageIcon(ImageIcon(
                "$cardImgPath$card.png".alsoPrint()
            ).image.getScaledInstance(90,150, Image.SCALE_SMOOTH))

            Entity(img).let {
                it.location = Point(board.width/2, board.width/2)
                board.add(it)
                it.applyVelocity(loc.toDouble(),-80.0)
                runBlocking {
                    delay(100)
                }
                it.whenClicked = {
                    print("adasdawdaqwfasefsdf2")
                    try {
                        print(game.runTurn(game.players[0].alsoPrint(),game.players[0].deck[i].alsoPrint()))
                    }catch (e: Exception){ e.printStackTrace() }
                    println("opiewjgopijeopijgergjesrgopij2")
                }
                loc+=10

                entityMap["1-$i"] = it
                info { "done1" }

            }
        }

        loc = -10
        for ((i,card) in deck2.withIndex()) {
            val img = ImageIcon(ImageIcon("$cardImgPath$card.png").image.getScaledInstance(90,150, Image.SCALE_SMOOTH))

            Entity(img).let {
                it.location = Point(board.width/2, board.width/2)
                board.add(it)
                it.applyVelocity(loc.toDouble(),50.0)
                runBlocking {
                    delay(100)
                }
                it.whenClicked = {
                    print("adasdawdaqwfasefsdf")
                    game.runTurn(game.players[1],game.players[1].deck[i])
                    println("opiewjgopijeopijgergjesrgopij")
                }
                game.runTurn(game.players[1],game.players[1].deck[i]).alsoPrint()
                loc+=10

                entityMap["2-$i"] = it
                info { "done" }
            }
        }
    }
}