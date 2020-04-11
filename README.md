# KotlinRuntimeContracts

#### This is a small library that provides runtime contracts.
*Tests are attached*

It supports:
* Using common `() -> Boolean` as contract
* Using `Contract` type
* `Contract` class:
    * Field `name` - name of the contract
    * Constructor that takes name of the contract, predicate checker and optional body string (can be used for composite contracts)
    * Method invoke (from `() -> Boolean`)
    * Readable `.toString` method (hierarchic structure representation) with given name
    * Open method `why(interestInSuccess)` that gives string representation only from that part of contract structure which leads to the asked result
        * It is just `.toString` by default
    * Method `assertContract` that checks if the the contract is fulfilled. If not so, the message `why(false)` is given as a description for the thrown `ContractException`
    * `trueContract` that is always fulfilled
    * `falseContract` that is never fulfilled
    * Static methods:
        1. `all` - takes contracts and returns contract that is fulfilled only if every parameter is fulfilled
        2. `any` - takes contracts and returns contract that is fulfilled only if at least 1 parameter is fulfilled
        3. `none` - takes contracts and returns contract that is fulfilled only if 0 parameters is fulfilled
        4. `either` - takes contracts and returns contract that is fulfilled only if exactly 1 parameter is fulfilled
        5. infix `leadsTo` - takes two contracts and returns a contracts that checks that if precondition is fulfilled, then postcondition is correct too.
        6. `not` - takes a contract and returns a contract with negated condition of success
    * Interface `Contracted`
        * Indicates that class has invariant contract.
    * Extension method `T.checkClassContract` that check contract of the object if it is `Contracted`
* Examples:
    * ```kotlin
      all({ a % 2 == 0}, { b == 0 } leadsTo { a == null })
      ```
    * ```kotlin
      all(Contract("Length is even") { a % 2 == 0}, { b == 0 } leadsTo { a == null }) 
      ```
    * ```kotlin
      either(
          not { coll is RandomAccess },
          Contract ("Some strange condition") {
              when (a) {
                  a is Int -> a + 1 < 100
                  a is String -> a.length < 100
                  else -> true
              }
          }
      )
      ```
* `.toString` output of the prelast example when `a = 1`, `b = 2`
    ```haskell
    [FALSE]
    All {
    	[FALSE]
    	Length is even
    	[TRUE]
    	Conditional {
    		:Pre
    		[FALSE]
    		<Predicate>
    		   |
    		   V
    		:Post
    		[FALSE]
    		<Predicate>
    	}
    }
    ```
* `.why(false)` output of that example
    ```haskell
    [FALSE]
    All {
    	[FALSE]
    	Length is even
    }
    ```
* `.why(true)` output of that example
    ```haskell 
    [FALSE]
    All
    ```
