package banhmi.senboard.app.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import dagger.hilt.android.scopes.ActivityRetainedScoped

// Route is Any because it can be a (data) object or class
@ActivityRetainedScoped
class SenNavigator(startDestination: Any) {
    val backStack: SnapshotStateList<Any> = mutableStateListOf(startDestination)

    fun goTo(destination: Any) = backStack.add(destination)

    fun goBack() = backStack.removeLastOrNull()
}
