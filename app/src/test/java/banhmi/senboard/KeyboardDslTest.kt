package banhmi.senboard

import banhmi.senboard.ime.keyboard.data.layouts.StandardLayout
import banhmi.senboard.ime.keyboard.data.modes.CharactersMode
import banhmi.senboard.ime.keyboard.data.modes.NumericsMode
import banhmi.senboard.ime.keyboard.dsl.layout
import banhmi.senboard.ime.keyboard.dsl.mode
import banhmi.senboard.ime.keyboard.models.KeyVariant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class KeyboardDslTest {

    @Test
    fun testLayoutDslBuilder() {
        val testLayout = layout("test_layout") {
            row {
                keys(10)
            }
            row(heightWeight = 2f) {
                key(areaWeight = 1.5f, variant = KeyVariant.Secondary)
                keys(7)
                key(areaWeight = 1.5f, variant = KeyVariant.Primary)
            }
        }

        assertEquals("test_layout", testLayout.name)
        assertEquals(2, testLayout.keyRows.size)
        assertEquals(10, testLayout.keyRows[0].keys.size)
        assertEquals(9, testLayout.keyRows[1].keys.size)
        assertEquals(2f, testLayout.keyRows[1].heightWeight)
    }

    @Test
    fun testModeDslBuilder() {
        val testMode = mode("test_mode", StandardLayout) {
            charKeys("q", "w", "e")
            spaceKey()
            backspaceKey()
            returnKey()
        }

        assertEquals("test_mode", testMode.name)
        assertEquals(StandardLayout, testMode.layout)
        assertEquals(6, testMode.slots.size)
    }

    @Test
    fun testExistingModesAndLayouts() {
        assertNotNull(StandardLayout)
        assertNotNull(CharactersMode)
        assertNotNull(NumericsMode)
        assertNotNull(banhmi.senboard.ime.keyboard.data.modes.SymbolicsMode)
        assertEquals("standard", StandardLayout.name)
        assertEquals("characters", CharactersMode.name)
        assertEquals("numerics", NumericsMode.name)
        assertEquals("symbolics", banhmi.senboard.ime.keyboard.data.modes.SymbolicsMode.name)
        assertEquals(4, StandardLayout.keyRows.size)
        assertEquals(34, CharactersMode.slots.size)
        assertEquals(34, NumericsMode.slots.size)
        assertEquals(34, banhmi.senboard.ime.keyboard.data.modes.SymbolicsMode.slots.size)
    }
}
