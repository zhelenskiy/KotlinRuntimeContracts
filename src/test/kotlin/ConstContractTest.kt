import Contract.Companion.falseContract
import Contract.Companion.trueContract
import org.junit.Test

class ConstContractTest {
    @Test
    fun `Always true`() {
        ContractTestUtility.assertSuccess(trueContract, ContractTestUtility.prefixWhen(true) + "Always correct", null)
    }

    @Test
    fun `Always false`() {
        ContractTestUtility.assertFailure(falseContract, ContractTestUtility.prefixWhen(false) + "Never correct", null)
    }
}