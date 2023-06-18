package code

import blossomFrameWork.BlossomSystem
import blossomFrameWork.application.Application


fun main() {

    BlossomSystem.informationForDebug = true

    Application.add(Code::class).main()
    Application.add(Calculator::class)
    Application.add(TEST::class)
    Application.add(OneCardGame::class)
    Application.add(NewBackBoard2::class)
    Application.setupApplication("code")

/*    val game = OneCardGame()
    with(game){
        new(2).let {
            start()
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

    }*/


    //Console.showFrame()
    //Console.showUI(true)








}

class CannotFindApplicationException(msg : String): Exception(msg)