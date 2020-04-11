import java.lang.RuntimeException

open class Contract(val name: String, private val invariant: () -> Boolean, private val treeBody: String) :
        () -> Boolean {
    constructor(name: String, invariant: () -> Boolean) : this(name, invariant, "")

    fun assertContract() {
        if (!invariant()) {
            throw ContractException("Contract check failed:\n${this.why(false)}.")
        }
    }


    override fun invoke() = invariant()
    override fun toString() = toStringWithBody(this)
    open fun why(interestInSuccess: Boolean) = toStringWithBody(this)

    companion object {
        internal fun (() -> Boolean).formatNameWhen(result: Boolean, addBody: Boolean = false) =
            formatBool(result) + if (this is Contract) (name + if (addBody) treeBody else "") else "<Predicate>"

        fun toStringWithBody(predicate: () -> Boolean): String = predicate.formatNameWhen(predicate(), true)

        fun all(vararg predicates: () -> Boolean): Contract =
            object : Contract(
                "All", { predicates.all { it() } },
                generateBody(predicates)
            ) {
                override fun why(interestInSuccess: Boolean): String {
                    val partition = predicates.partition { it() }
                    val (_, falseList) = partition
                    val assertedSuccessfully = falseList.isEmpty()
                    return generateCause(
                        assertedSuccessfully,
                        partition,
                        interestInSuccess,
                        takeIf = assertedSuccessfully == interestInSuccess
                    )

                }
            }

        fun either(vararg predicates: () -> Boolean): Contract =
            object : Contract(
                "Either", { predicates.filter { it() }.size == 1 },
                generateBody(predicates)
            ) {
                override fun why(interestInSuccess: Boolean): String {
                    val partition = predicates.partition { it() }
                    val (trueList, _) = partition
                    val assertedSuccessfully = trueList.size == 1
                    return when {
                        interestInSuccess != assertedSuccessfully -> formatNameWhen(assertedSuccessfully)
                        assertedSuccessfully -> toStringWithBody(this)
                        else -> generateCause(assertedSuccessfully, partition, trueList.isNotEmpty())
                    }
                }
            }

        fun any(vararg predicates: () -> Boolean): Contract =
            object : Contract(
                "Any", { predicates.any { it() } },
                generateBody(predicates)
            ) {
                override fun why(interestInSuccess: Boolean): String {
                    val partition = predicates.partition { it() }
                    val (trueList, _) = partition
                    val assertedSuccessfully = trueList.isNotEmpty()
                    return generateCause(
                        assertedSuccessfully,
                        partition,
                        interestInSuccess,
                        takeIf = assertedSuccessfully == interestInSuccess
                    )
                }
            }

        fun none(vararg predicates: () -> Boolean): Contract =
            object : Contract(
                "None", { predicates.none { it() } },
                generateBody(predicates)
            ) {
                override fun why(interestInSuccess: Boolean): String {
                    val partition = predicates.partition { !it() }
                    val (_, trueList) = partition
                    val assertedSuccessfully = trueList.isEmpty()
                    return generateCause(
                        assertedSuccessfully,
                        partition,
                        interestInSuccess,
                        takeIf = assertedSuccessfully == interestInSuccess
                    )
                }
            }

        val trueContract = Contract("Always correct") { true }
        val falseContract = Contract("Never correct") { false }


        infix fun (() -> Boolean).leadsTo(postCondition: () -> Boolean): Contract {
            val preCondition = this
            return object : Contract(
                "Conditional", { !preCondition() || postCondition() },
                " {\n" + (":Pre\n" +
                        "${toStringWithBody(preCondition)}\n" +
                        "   |\n" +
                        "   V\n" +
                        ":Post\n" +
                        toStringWithBody(postCondition)).indent() + "\n" +
                        "}"
            ) {
                override fun why(interestInSuccess: Boolean): String {
                    val assertionResult = this()
                    val formattedAssertionResult = formatNameWhen(assertionResult)
                    return when {
                        interestInSuccess != assertionResult -> formattedAssertionResult
                        interestInSuccess && !preCondition() -> {
                            val subCause =
                                if (preCondition is Contract) preCondition.why(true)
                                else toStringWithBody(preCondition)
                            formattedAssertionResult + " {\n" +
                                    "\t:Pre\n" +
                                    "${subCause.indent()}\n" +
                                    "}"
                        }
                        else -> toStringWithBody(this)
                    }
                }
            }
        }

        fun not(pred: () -> Boolean) =
            object : Contract("Not", { !pred() }, generateBody(arrayOf(pred))) {
                override fun why(interestInSuccess: Boolean): String {
                    val assertResult = this()
                    return if (assertResult == interestInSuccess) toString() else formatNameWhen(assertResult)
                }
            }

        private fun formatBool(cond: Boolean) = "[${cond.toString().toUpperCase()}]\n"

        private fun String.indent() = this.lines().joinToString("\n") { "\t" + it }
        private fun List<String>.indent() = this.joinToString("\n").indent()

        private fun generateBody(predicates: Array<out () -> Boolean>) =
            if (predicates.isEmpty()) " {}" else " {\n${predicates.map { toStringWithBody(it) }.indent()}\n}"


    }

    internal fun generateCause(
        success: Boolean,
        partition: Pair<List<() -> Boolean>, List<() -> Boolean>>,
        successIsInteresting: Boolean,
        takeIf: Boolean = true
    ): String {
        val data = if (successIsInteresting) partition.first else partition.second
        return formatBool(success) + name +
                when {
                    !takeIf -> ""
                    data.isEmpty() -> " {}"
                    else ->
                        " {\n${
                        data.map { if (it is Contract) it.why(successIsInteresting) else toStringWithBody(it) }.indent()
                        }\n}"
                }
    }
}

class ContractException(override val message: String?) : RuntimeException(message, null, false, true)

interface Contracted {
    val contract: Contract
}

fun <T> T.checkClassContract() {
    if (this is Contracted) {
        this.contract.assertContract()
    }
}