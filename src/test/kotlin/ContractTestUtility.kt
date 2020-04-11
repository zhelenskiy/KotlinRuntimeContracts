import Contract.Companion.falseContract
import Contract.Companion.trueContract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

internal object ContractTestUtility {
    fun getErrorMessage(runnable: () -> Unit): String? {
        return try {
            runnable()
            null
        } catch (e: ContractException) {
            e.message
        }
    }

    fun assertFailure(contract: Contract, toString: String, cause: String?) {
        assertBoth(contract, toString, cause, false)
        assertEquals(
            "Contract check failed:\n${cause ?: toString}.", getErrorMessage { contract.assertContract() }
        )
    }

    private fun assertBoth(contract: Contract, toString: String, cause: String?, isSuccess: Boolean) {
        assertName(contract, toString)
        assertWhy(contract, toString, cause, isSuccess)
    }

    private fun assertWhy(contract: Contract, toString: String, cause: String?, isSuccess: Boolean) {
        assertEquals(cause ?: toString, contract.why(isSuccess))
        assertFalse(contract.why(!isSuccess).contains('{'))
        assertEquals(prefixWhen(isSuccess) + contract.name, contract.why(!isSuccess))
    }

    fun assertSuccess(contract: Contract, toString: String, cause: String?) {
        assertBoth(contract, toString, cause, true)
        contract.assertContract()
    }

    private fun assertName(contract: Contract, toString: String) {
        assertEquals(toString, contract.toString())
    }

    fun prefixWhen(value: Boolean) = "[${value.toString().toUpperCase()}]\n"
    fun argumentString(value: Boolean) =
        (if (value) trueContract else falseContract).toString().lines().joinToString("\n") { "\t" + it }

    fun expectedString(name: String, result: Boolean, var1: Boolean, var2: Boolean): String {
        return "${prefixWhen(result)}$name {\n${argumentString(var1)}\n${argumentString(var2)}\n}"
    }

    fun expectedString(name: String, result: Boolean, var1: Boolean): String {
        return "${prefixWhen(result)}$name {\n${argumentString(var1)}\n}"
    }

    fun expectedString(name: String, result: Boolean): String {
        return "${prefixWhen(result)}$name {}"
    }
}