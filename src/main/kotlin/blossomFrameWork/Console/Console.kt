package blossomFrameWork.Console

import blossomFrameWork.*
import blossomFrameWork.frame.Frame
import blossomFrameWork.frame.whenKeyPressed
import blossomFrameWork.application.Application
import blossomFrameWork.application.callFunction
import blossomFrameWork.command.Command
import blossomFrameWork.command.request
import blossomFrameWork.values.MutableValue
import code.CannotFindApplicationException
import kotlinx.coroutines.*
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.KeyEvent
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Style
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext
import kotlin.reflect.full.declaredMembers

object Console {
    fun showUI(loop:Boolean = false){
        Command{
            then("Application"){
                then("run"){
                    then("name" request String::class){
                        then("functionName" request String::class){
                            executes {context ->
                                Application.applicationMap[context.argList[0]]
                                    .isNullException(
                                    CannotFindApplicationException("${context.argList[0]} is not defined in application"))
                                    .first()::class.alsoPrint().callFunction(context.argList[1])
                                info("call Application") { "Successfully called Function" }
/*                                    .declaredMembers.first { it.name == context.argList[1] }.call(app)*/

                            }
                        }
                    }
                    then("name" request String::class){
                        executes { context ->
                            val app = Application.applicationMap.get(context.argList[0]).isNullException(
                                CannotFindApplicationException("${context.argList[0]} is not defined in application")
                            ).first()
                            app::class.declaredMembers.first { it.name == "run" }.call(app)
                        }
                    }
                }
            }
            then("Values"){
                then("MutableValueName" request String::class){
                    then("set"){
                        then("value" request String::class){
                            executes { context ->
                                var prefix = "Failed to "
                                if (MutableValue.list.first { it.name == context.argList[0] }.checkedSet(context.argList[1])) {
                                    prefix = "Successfully "
                                }
                                info(name = "Command") { prefix+"change the value : ${context.argList[0]} to ${context.argList[1]}" }
                            }
                        }
                    }
                }
            }
            then("Frame"){
                then("width" request String::class){
                    then("height" request String::class){
                        executes {
                            Frame(it.argList[0].toInt(),it.argList[1].toInt()).add(JButton())
                        }
                    }
                }
            }
        }.`in`()
        if(loop) showUI(true)
    }
    val consoleFrame by lazy { Frame(1000,1000) }
    private lateinit var textArea: JTextPane

    val defaultStyle by lazy {
        val dS = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE)

        val redStyle = textArea.styledDocument.addStyle("red", dS)
        StyleConstants.setForeground(redStyle, Color.WHITE)
        StyleConstants.setFontSize(redStyle,20)
        StyleConstants.setFontFamily(redStyle,"Sans")
        redStyle
    }
    val redStyle by lazy {
        val dS = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE)

        val redStyle = textArea.styledDocument.addStyle("red", dS)
        StyleConstants.setForeground(redStyle, Color.RED)
        StyleConstants.setFontSize(redStyle,20)
        StyleConstants.setFontFamily(redStyle,"Sans")
        redStyle
    }

    private var inputStream = ByteArrayInputStream.nullInputStream()

    val inputCoroutine = CoroutineScope(Dispatchers.IO + Job() + CoroutineName("InputStream"))
    fun showFrame(){

        with(consoleFrame){
            layout = FlowLayout()

            //label$$ text
            val label = JLabel("Input")

            val textField = JTextField(width/50)




            textField.addActionListener {

                uploadLogln("[User] > "+textField.text, defaultStyle)
                inputStream = ByteArrayInputStream(textField.text.toByteArray())
                textField.text = ""
                updateFrame()
            }


            textArea = JTextPane().apply {
                isEditable = true
                background = Color.BLACK
                preferredSize = Dimension(this@with.width-10,this@with.height-100)
            }
            textArea.whenKeyPressed {
                if(it.keyCode == KeyEvent.VK_ENTER){
                    val text = textArea.styledDocument.getText(0,textArea.styledDocument.length)
                    println(text)
                }
            }
            textArea.styledDocument.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent) {
                    println("lengthi"+e.offset.toString())
                }

                override fun removeUpdate(e: DocumentEvent) {
                    println("lengthr"+e.length.toString())
                }

                override fun changedUpdate(e: DocumentEvent) {
                    println("lengthc"+e.length.toString())
                }

            })
            val scrollPane = JScrollPane(textArea)  // JTextPane를 JScrollPane에 추가

            contentPane.add(scrollPane)

            contentPane.add(textArea)
            add(label)
            add(textField)
            updateFrame()
        }
        BlossomSystem.isOnConsole = true
    }

    fun uploadLogln(text: String, style: Style?){
        textArea.styledDocument.let {
            it.insertString(it.length,text,style.insteadIfNull(defaultStyle))
            it.insertString(it.length,"\n",null)
        }
    }
    fun uploadLog(text: String, style: Style?){
        textArea.styledDocument.let {
            it.insertString(it.length,text,style.insteadIfNull(defaultStyle))
        }
    }
    /*IO Blockable*/
    fun getInputStream(): InputStream{
        val job = inputCoroutine.async {
            while (isActive){
                if(inputStream.available() == 0) yield()
                else return@async inputStream
                delay(10)
            }
        }
        var result : InputStream?
        runBlocking {
            result = job.await() as InputStream
        }
        return result!!



    }
    fun setOutput(byteArray: ByteArray){

    }
    private fun updateFrame(){
        if(consoleFrame.isVisible) consoleFrame.isVisible=true
    }
}

