package blossomFrameWork.application

import blossomFrameWork.Game
import blossomFrameWork.ifTrue
import blossomFrameWork.transform
import java.io.File
import javax.security.auth.login.Configuration
import kotlin.reflect.KClass

class Application<T: Any>(val instance: T) {

    companion object{
        val applicationMap = HashMap<String,ArrayList<Any>>()
        lateinit var AllClasses: List<KClass<*>>
            private set
        fun <T : Any> run(app: KClass<T>,constructorNumber: Int = 0,vararg args: Any): T{
            if (applicationMap.values.any { app.simpleName == it.first()::class.simpleName }) throw IllegalStateException("instance is already exists")

            return if(args.isEmpty()){
                try {
                    app.constructors.filter { it.parameters.isEmpty() }[constructorNumber].call().also{ applicationAdd(it) }
                } catch (e: Exception) {
                    throw CannotCreateIntanceException("Cannot find application's public constructor")
                }
            }else {// is NOT empty
                try {
                    app.constructors.filter { it.parameters.size == args.size }[constructorNumber].call(*args).also{ applicationAdd(it) }
                } catch (e: Exception) {
                    throw CannotCreateIntanceException("Cannot find application's constructor matching Argument. pls use constructorNumber parameter")
                }
            }
        }

        fun setupApplication(packageName: String){
            AllClasses = loadClassesFromPackage(packageName).transform { it.kotlin }
        }

        private fun applicationAdd(app: Any){
            val name = app::class.simpleName!!
            if(applicationMap[name] == null)
                applicationMap[name] = ArrayList()
            applicationMap[name]!!.add(app)
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