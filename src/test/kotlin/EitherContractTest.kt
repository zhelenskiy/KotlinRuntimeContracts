import Contract.Companion.either
import Contract.Companion.falseContract
import Contract.Companion.trueContract
import ContractTestUtility.expectedString
import org.junit.Test

class EitherContractTest {

    private val name = "Either"

    @Test
    fun `When false, false`() {
        ContractTestUtility.assertFailure(
            either(falseContract, falseContract),
            expectedString(name, result = false, var1 = false, var2 = false),
            null
        )
    }

    @Test
    fun `When false, true`() {
        ContractTestUtility.assertSuccess(
            either(falseContract, trueContract),
            expectedString(name, result = true, var1 = false, var2 = true),
            null
        )
    }

    @Test
    fun `When true, false`() {
        ContractTestUtility.assertSuccess(
            either(trueContract, falseContract),
            expectedString(name, result = true, var1 = true, var2 = false),
            null
        )
    }

    @Test
    fun `When true, true`() {
        ContractTestUtility.assertFailure(
            either(trueContract, trueContract),
            expectedString(name, result = false, var1 = true, var2 = true),
            null
        )
    }

    @Test
    fun `When empty`() {
        ContractTestUtility.assertFailure(
            either(),
            expectedString(name, result = false),
            null
        )
    }
}