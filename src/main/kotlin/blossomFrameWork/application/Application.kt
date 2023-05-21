package blossomFrameWork.application

import blossomFrameWork.*
import code.CannotFindApplicationException
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.javaType

class Application<T: Any>(val instance: T) {

    companion object{
        val applicationMap = HashMap<String,ArrayList<Any>>()
        @JvmStatic
        lateinit var AllClasses: List<KClass<*>>
            private set



        fun <T : Any> add(app: KClass<T>, constructorNumber: Int = 0, vararg args: Any): T{
            if (applicationMap.values.any { app.simpleName == it.first()::class.simpleName }) throw IllegalStateException("instance is already exists")// 지워야할거같은데
            return app.callConstructor(*args).also(::applicationAdd)
        }

        fun setupApplication(packageName: String){
            AllClasses = loadClassesFromPackage(packageName).transform { it.kotlin }
        }

        private fun applicationAdd(app: Any){
            val name = app::class.simpleName!!
            if(applicationMap[name] == null) applicationMap[name] = ArrayList()
            applicationMap[name]!!.add(app)
        }
        // 인젝팅 안할땐 어캄? 그럼

        private fun <T : Any> KClass<T>.callConstructor(vararg args: Any): T{
            return this.callConstructorWithInject(*args).alsoPrint() insteadIfNull { this.new(*args) }
        }
        @OptIn(ExperimentalStdlibApi::class)
        private fun <T : Any> KClass<T>.callConstructorWithInject(vararg args: Any): T?{
            val resultLet = ArrayList<Any>()
            val pair = this.findConstructorPWithAnno(Inject::class) ?: return null
            if(pair.first.parameters.size != args.size+pair.second.size) return null
            info("adding Application $$this") { "Need Dependency injection" }
            pair.second.forEach { kParameter ->
                resultLet +=
                    applicationMap.values.firstOrNull { it.first()::class.java == kParameter.type.javaType }
                        .isNullException(CannotCreateIntanceException("Cannot inject dependencies on constructor, Is it registered?")).first()
                        .withInfo("adding Application $$this") { "Application Constructor dependency found!, $it" }
            }
            return pair.first.call(*resultLet.toArray(),*args)
        }
        private fun <T : Any> KClass<T>.new(vararg args: Any): T{
            return this.constructors.filter { it.parameters.size == args.size }
                .ifEmpty { throw CannotCreateIntanceException("$this has not constructor that can called by ${args.toArrayList()}") }
                .first().call(*args)

        // 부합하는
        }
    }// companion object end


    fun execute(){
        instance::class.annotations.any{ it.annotationClass == Game::class }
    }


}
class CannotCreateIntanceException(msg : String): Exception(msg)

fun loadClassesFromPackage(packageName: String): List<Class<*>> {
    val classLoader = Thread.currentThread().contextClassLoader
    val packagePath = packageName.replace(".", File.separator)
    val packageUrl = classLoader.getResource(packagePath)

    val classes = mutableListOf<Class<*>>()

    if (packageUrl != null) {
        val packageFile = File(packageUrl.file)
        if (packageFile.isDirectory) {
            val classFiles = packageFile.listFiles { file ->
                file.isFile && file.name.endsWith(".class")
            }
            classFiles?.forEach { classFile ->
                val className = packageName + '.' + classFile.nameWithoutExtension
                try {
                    val clazz = Class.forName(className)
                    classes.add(clazz)
                } catch (e: ClassNotFoundException) {
                    // 클래스를 로드할 수 없는 경우 처리
                    e.printStackTrace()
                }
            }
        }
    }

    return classes
}

fun <T : Any> KClass<T>.findConstructor(vararg any: Any){
    constructor@for (constructor in this.constructors) {
        for ((i,parameter) in constructor.parameters.withIndex()) {
            if(parameter != any[i]) continue@constructor
        }
    }
}
fun <T : Any,R: Annotation> KClass<T>.findConstructor(annotation: KClass<R>,vararg any: Any): KFunction<T>? {
    constructor@for (constructor in this.constructors) {
        if(!constructor.parameters.any { it.annotations.any { it == annotation }}) continue@constructor
        for ((i,parameter) in constructor.parameters.withIndex()) {
            if(parameter != any[i]) continue@constructor
        }
        return constructor
    }
    return null
}
//성능 테스트좀 parameter 가없을때 vs 없는거감지하고 컨티뉴 넣을때
fun <T : Any,R: Annotation> KClass<T>.findConstructorPWithAnno(annotation: KClass<R>): Pair<KFunction<T>,List<KParameter>>? {
    constructor@for (constructor in this.constructors) {
        val result = ArrayList<KParameter>()
        for ((i,parameter) in constructor.parameters.withIndex()) {
            if(parameter.annotations.none { it.annotationClass == annotation }) continue@constructor
            result.add(parameter)
        }
        if(result.isNotEmpty()) return constructor to result
    }
    return null
}
fun <T : Any> KClass<T>.callFunction(name: String, vararg args: Any) =
    declaredFunctions.filter { it.name == name }.forEach { it.call(Application.applicationMap[this.simpleName].isNullException(CannotFindApplicationException("")).first(),*args) }
