import Contract.Companion.falseContract
import Contract.Companion.not
import Contract.Companion.trueContract
import ContractTestUtility.expectedString
import org.junit.Test

class NotContractTest {

    private val name = "Not"

    @Test
    fun `When false`() {
        ContractTestUtility.assertSuccess(
            not(falseContract),
            expectedString(name, result = true, var1 = false),
            null
        )
    }

    @Test
    fun `When true`() {
        ContractTestUtility.assertFailure(
            not(trueContract),
            expectedString(name, result = false, var1 = true),
            null
        )
    }
}