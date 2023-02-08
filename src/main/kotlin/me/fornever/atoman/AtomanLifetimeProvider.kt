package me.fornever.atoman

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition

@Service(Service.Level.PROJECT)
class AtomanLifetimeProvider : Disposable {

    companion object {
        fun getInstance(project: Project): AtomanLifetimeProvider = project.service()
    }

    private val ld = LifetimeDefinition()
    val lifetime: Lifetime
        get() = ld.lifetime

    override fun dispose() {
        ld.terminate()
    }
}
