import Contract.Companion.falseContract
import Contract.Companion.leadsTo
import Contract.Companion.trueContract
import ContractTestUtility.argumentString
import ContractTestUtility.prefixWhen
import org.junit.Test

class LeadsToContractTest {

    private val name = "Conditional"

    private fun stringRepresentation(preCondition: Boolean, postCondition: Boolean?) =
        prefixWhen(!preCondition || (postCondition ?: false)) + name +
                " {\n\t:Pre\n" + argumentString(preCondition) +
                (if (postCondition != null) "\n\t   |\n\t   V\n\t:Post\n" + argumentString(postCondition) else "") +
                "\n}"

    @Test
    fun `When false, false`() {
        ContractTestUtility.assertSuccess(
            falseContract leadsTo falseContract,
            stringRepresentation(preCondition = false, postCondition = false),
            stringRepresentation(preCondition = false, postCondition = null)
        )
    }

    @Test
    fun `When false, true`() {
        ContractTestUtility.assertSuccess(
            falseContract leadsTo trueContract,
            stringRepresentation(preCondition = false, postCondition = true),
            stringRepresentation(preCondition = false, postCondition = null)
        )
    }

    @Test
    fun `When true, false`() {
        ContractTestUtility.assertFailure(
            trueContract leadsTo falseContract,
            stringRepresentation(preCondition = true, postCondition = false),
            null
        )
    }

    @Test
    fun `When true, true`() {
        ContractTestUtility.assertSuccess(
            trueContract leadsTo trueContract,
            stringRepresentation(preCondition = true, postCondition = true),
            null
        )
    }
}