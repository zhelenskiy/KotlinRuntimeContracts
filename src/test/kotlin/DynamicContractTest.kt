import org.junit.Assert.assertEquals
import org.junit.Test

class DynamicContractTest {

    @Test
    fun `When changing once`() {
        for (first in listOf(false, true)) {
            var value = first
            val contract = Contract("Changing condition contract") { value }
            assertEquals(first, contract())
            value = !value
            assertEquals(!first, contract())
        }
    }

    @Test
    fun `When changing a lot`() {
        var i = 0
        val contract = Contract("Changing condition contract") { i % 2 == 0 }
        while (i < 10) {
            assertEquals(i % 2 == 0, contract())
            i++
        }
    }
}