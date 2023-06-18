package blossomFrameWork.frame.abstraction

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface Workable {
    val workMap : HashMap<String,Job>
    val workScope : CoroutineScope
    fun addTask(name: String,coroutine : suspend CoroutineScope.() -> Unit){
        workMap[name] = workScope.launch{ coroutine(this) }
    }
    fun cancelTask(name: String): Boolean{
        workMap[name] ?: return false
        workMap[name]!!.cancel()
        workMap.remove(name)
        return true
    }
}