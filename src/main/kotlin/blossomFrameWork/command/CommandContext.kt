package blossomFrameWork.command

data class CommandContext(val runnableCommand: RunnableCommand, val argList: List<String>,val input: List<String>) {

}