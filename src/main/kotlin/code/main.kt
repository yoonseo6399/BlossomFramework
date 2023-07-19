package code

import blossomFrameWork.BlossomSystem
import blossomFrameWork.application.Application
import code.onecard.OneCardGame
import code.onecard.OnecardBoard


fun main() {

    BlossomSystem.informationForDebug = true

    Application.add(Code::class).main()
    Application.add(Calculator::class)
    Application.add(TEST::class)
    Application.add(OneCardGame::class)
    //Application.add(NewBackBoard2::class)
    Application.setupApplication("code")

    OnecardBoard





    //Console.showFrame()
    //Console.showUI(true)








}

class CannotFindApplicationException(msg : String): Exception(msg)