package banhmi.senboard.keyboard.lifecycle

import android.inputmethodservice.InputMethodService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/* Took reference from the FlorisBoard implementation of the lifecycle InputMethodService:
https://github.com/florisboard/florisboard/blob/main/app/src/main/kotlin/dev/patrickgold/florisboard/ime/lifecycle/LifecycleInputMethodService.kt
Additionally, thanks ChatGPT and Gemini for assisting me! */
open class SenLifecycleImService : InputMethodService(), LifecycleOwner, SavedStateRegistryOwner,
    ViewModelStoreOwner, CoroutineScope by CoroutineScope(
        SupervisorJob() + Dispatchers.Main + CoroutineExceptionHandler { _, exception ->
            println("Error in coroutine: $exception")
        }) {
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    private val savedStateRegistryController: SavedStateRegistryController =
        SavedStateRegistryController.create(this)

    private val _viewModelStore: ViewModelStore = ViewModelStore()

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val viewModelStore: ViewModelStore
        get() = _viewModelStore

    override fun onCreate() {
        super.onCreate()
        // Don't restore the saved state before moving the lifecycle to CREATED state
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    // Set lifecycle state to STARTED for reactive states to work
    protected fun handleLifecycleOnStartEvent() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    protected fun setViewTreeOwners() {
        window.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeViewModelStoreOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }
    }

    override fun onWindowShown() {
        super.onWindowShown()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onWindowHidden() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        super.onWindowHidden()
    }

    override fun onDestroy() {
        _viewModelStore.clear()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDestroy()
    }
}
