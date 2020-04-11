import Contract.Companion.falseContract
import Contract.Companion.none
import Contract.Companion.trueContract
import ContractTestUtility.expectedString
import org.junit.Test

class NoneContractTest {

    private val name = "None"

    @Test
    fun `When false, false`() {
        ContractTestUtility.assertSuccess(
            none(falseContract, falseContract),
            expectedString(name, result = true, var1 = false, var2 = false),
            null
        )
    }

    @Test
    fun `When false, true`() {
        ContractTestUtility.assertFailure(
            none(falseContract, trueContract),
            expectedString(name, result = false, var1 = false, var2 = true),
            expectedString(name, result = false, var1 = true)
        )
    }

    @Test
    fun `When true, false`() {
        ContractTestUtility.assertFailure(
            none(trueContract, falseContract),
            expectedString(name, result = false, var1 = true, var2 = false),
            expectedString(name, result = false, var1 = true)
        )
    }

    @Test
    fun `When true, true`() {
        ContractTestUtility.assertFailure(
            none(trueContract, trueContract),
            expectedString(name, result = false, var1 = true, var2 = true),
            null
        )
    }

    @Test
    fun `When empty`() {
        ContractTestUtility.assertSuccess(
            none(),
            expectedString(name, result = true),
            null
        )
    }
}