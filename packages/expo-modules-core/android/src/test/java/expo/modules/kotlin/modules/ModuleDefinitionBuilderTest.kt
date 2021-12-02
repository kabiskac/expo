package expo.modules.kotlin.modules

import com.google.common.truth.Truth
import expo.modules.core.Promise
import expo.modules.kotlin.events.EventName
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class ModuleDefinitionBuilderTest {

  @Test
  fun `builder should throw if modules name wasn't provided`() {
    Assert.assertThrows(IllegalArgumentException::class.java) {
      ModuleDefinition { }
    }

    Assert.assertThrows(IllegalArgumentException::class.java) {
      ModuleDefinition {
        function("method") { _: Int, _: Int -> }
      }
    }
  }

  @Test
  fun `builder should constructed correct definition`() {
    val moduleName = "Module"
    val moduleConstants = emptyMap<String, Any?>()

    val moduleDefinition = ModuleDefinition {
      name(moduleName)
      constants {
        moduleConstants
      }
      function("m1") { _: Int -> }
      function("m2") { _: Int, _: Promise -> }
    }

    Truth.assertThat(moduleDefinition.name).isEqualTo(moduleName)
    Truth.assertThat(moduleDefinition.constantsProvider()).isSameInstanceAs(moduleConstants)
    Truth.assertThat(moduleDefinition.methods).containsKey("m1")
    Truth.assertThat(moduleDefinition.methods).containsKey("m2")
  }

  @Test
  fun `builder should allow adding view manager`() {
    val moduleName = "Module"

    val moduleDefinition = ModuleDefinition {
      name(moduleName)
      viewManager {
        view { mockk() }
      }
    }

    Truth.assertThat(moduleDefinition.name).isEqualTo(moduleName)
    Truth.assertThat(moduleDefinition.viewManagerDefinition).isNotNull()
  }

  @Test
  fun `builder should respect events`() {
    val moduleName = "Module"

    val moduleDefinition = ModuleDefinition {
      name(moduleName)
      onCreate { }
      onDestroy { }
      onActivityDestroys { }
      onActivityEntersForeground { }
      onActivityEntersBackground { }
    }

    Truth.assertThat(moduleDefinition.name).isEqualTo(moduleName)
    Truth.assertThat(moduleDefinition.eventListeners[EventName.MODULE_CREATE]).isNotNull()
    Truth.assertThat(moduleDefinition.eventListeners[EventName.MODULE_DESTROY]).isNotNull()
    Truth.assertThat(moduleDefinition.eventListeners[EventName.ACTIVITY_ENTERS_FOREGROUND]).isNotNull()
    Truth.assertThat(moduleDefinition.eventListeners[EventName.ACTIVITY_ENTERS_BACKGROUND]).isNotNull()
    Truth.assertThat(moduleDefinition.eventListeners[EventName.ACTIVITY_DESTROYS]).isNotNull()
  }

  @Test
  fun `onStartObserving should be translated into method`() {
    val moduleDefinition = ModuleDefinition {
      name("module")
      onStartObserving { }
    }

    Truth.assertThat(moduleDefinition.methods).containsKey("startObserving")
  }

  @Test
  fun `onStopObserving should be translated into method`() {
    val moduleDefinition = ModuleDefinition {
      name("module")
      onStopObserving { }
    }

    Truth.assertThat(moduleDefinition.methods).containsKey("stopObserving")
  }
}
