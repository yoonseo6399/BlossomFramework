package blossomFrameWork.command


fun String.toType(string: String) {
    when (string) {

        "Int" -> this.toInt()
        "Long" -> this.toLong()
        "Float" -> this.toFloat()
        "Double" -> this.toDouble()
        "String" -> this.toString()
        "Boolean" -> if(this.equals("true", ignoreCase = true) || this.equals("false", ignoreCase = true)) Unit else throw NumberFormatException()

        "int" -> this.toInt()
        "long" -> this.toLong()
        "float" -> this.toFloat()
        "double" -> this.toDouble()
        "string" -> this.toString()
        "boolean" -> if(this.equals("true", ignoreCase = true) || this.equals("false", ignoreCase = true)) Unit else throw NumberFormatException()



        else -> throw ClassCastException("Cannot cast String to $string, Maybe Command's declaration's problem, Requesting Type Must be default type(example. Int,String ---)")
    }
}
