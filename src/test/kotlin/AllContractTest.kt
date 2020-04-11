import Contract.Companion.all
import Contract.Companion.falseContract
import Contract.Companion.trueContract
import ContractTestUtility.expectedString
import org.junit.Test

class AllContractTest {

    private val name = "All"

    @Test
    fun `When false, false`() {
        ContractTestUtility.assertFailure(
            all(falseContract, falseContract),
            expectedString(name, result = false, var1 = false, var2 = false),
            null
        )
    }

    @Test
    fun `When false, true`() {
        ContractTestUtility.assertFailure(
            all(falseContract, trueContract),
            expectedString(name, result = false, var1 = false, var2 = true),
            expectedString(name, result = false, var1 = false)
        )
    }

    @Test
    fun `When true, false`() {
        ContractTestUtility.assertFailure(
            all(trueContract, falseContract),
            expectedString(name, result = false, var1 = true, var2 = false),
            expectedString(name, result = false, var1 = false)
        )
    }

    @Test
    fun `When true, true`() {
        ContractTestUtility.assertSuccess(
            all(trueContract, trueContract),
            expectedString(name, result = true, var1 = true, var2 = true),
            null
        )
    }

    @Test
    fun `When empty`() {
        ContractTestUtility.assertSuccess(
            all(),
            expectedString(name, result = true),
            null
        )
    }
}