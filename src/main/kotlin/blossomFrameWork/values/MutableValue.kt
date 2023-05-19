package blossomFrameWork.values

import blossomFrameWork.input.toType
import kotlin.reflect.KProperty
import kotlin.reflect.cast

class MutableValue<T:Any>(default:T, val name : String){

    companion object{
        @Suppress("UNCHECKED_CAST")
        inline fun <reified T : Any> edit(name: String, value: T): Boolean{
            val field0 : ArrayList<MutableValue<T>> = list.filter { it.name == name } as? ArrayList<MutableValue<T>>
                    ?: return false
            if(field0.isEmpty()) return false
            if(field0.first().value::class.java != T::class.java) return false
            for (mutableValueConsole in field0) {
                mutableValueConsole.value = value
            }
            return true
        }
        val list = ArrayList<MutableValue<*>>(5)
    }

    init{
        list.add(this)
    }


    var value = default
    operator fun setValue(t: Any?, property: KProperty<*>, t1: T) {
        value = t1
    }
    operator fun getValue(nothing: Any?, property: KProperty<*>): T {
        return value
    }

    @Suppress("UNCHECKED_CAST")
    fun checkedSet(any: Any): Boolean{
        if(any is String){
            try {
                value = when(value::class.simpleName){
                    "Boolean" -> any.toBoolean() as T
                    "Int" -> any.toInt() as T
                    "Long" -> any.toLong() as T
                    "Short" -> any.toShort() as T
                    "String" -> any as T
                    else -> return false
                }
            } catch (_: Exception) {}
            return true
        }
        try {
            value = any::class.cast(value::class) as T
        }catch (_: Exception){
            return false
        }
        return true
    }
}
