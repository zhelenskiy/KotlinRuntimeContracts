import Contract.Companion.falseContract
import Contract.Companion.trueContract
import ContractTestUtility.getErrorMessage
import org.junit.Assert.assertEquals
import org.junit.Test

class ClassContractTest {

    @Test
    fun `When false`() {
        assertEquals(
            getErrorMessage { falseContract.assertContract() },
            getErrorMessage {
                object : Contracted {
                    override val contract = falseContract
                }.checkClassContract()
            }
        )

    }

    @Test
    fun `When true`() {
        assertEquals(
            getErrorMessage { trueContract.assertContract() },
            getErrorMessage {
                object : Contracted {
                    override val contract = trueContract
                }.checkClassContract()
            }
        )
    }
}