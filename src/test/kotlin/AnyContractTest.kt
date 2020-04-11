import Contract.Companion.any
import Contract.Companion.falseContract
import Contract.Companion.trueContract
import ContractTestUtility.expectedString
import org.junit.Test

class AnyContractTest {

    private val name = "Any"

    @Test
    fun `When false, false`() {
        ContractTestUtility.assertFailure(
            any(falseContract, falseContract),
            expectedString(name, result = false, var1 = false, var2 = false),
            null
        )
    }

    @Test
    fun `When false, true`() {
        ContractTestUtility.assertSuccess(
            any(falseContract, trueContract),
            expectedString(name, result = true, var1 = false, var2 = true),
            expectedString(name, result = true, var1 = true)
        )
    }

    @Test
    fun `When true, false`() {
        ContractTestUtility.assertSuccess(
            any(trueContract, falseContract),
            expectedString(name, result = true, var1 = true, var2 = false),
            expectedString(name, result = true, var1 = true)
        )
    }

    @Test
    fun `When true, true`() {
        ContractTestUtility.assertSuccess(
            any(trueContract, trueContract),
            expectedString(name, result = true, var1 = true, var2 = true),
            null
        )
    }

    @Test
    fun `When empty`() {
        ContractTestUtility.assertFailure(
            any(),
            expectedString(name, result = false),
            null
        )
    }
}