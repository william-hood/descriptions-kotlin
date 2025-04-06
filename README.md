# Descriptions
A library for random Test Object generation for use with Koarse Grind or other test frameworks.

The Descriptions library lets you generate test appropriate instances of your objects with fields at a specified test case value, a random valid value, or a fixed "happy path" value.

*(Although the Descriptions library is Kotlin, it should be usable with Java as it is.)*

## Usage
Suppose we start with the following Kotlin class we wish to test...
`
    class PackageTracker(val sent: LocalDate = LocalDate.now()) {
    var expectedArrivalDate: LocalDate? = null
    var expectedMaxStops = 0
    var totalStops = 0
    private var _lastStop: String? = null

    val lastStop: String
        get() {
            _lastStop?.let {
                return it
            }

            return "(awaiting pickup)"
        }

    fun addStop(stopName: String) {
        totalStops++
        _lastStop = stopName
    }

    fun verify() {
        if (sent.compareTo(LocalDate.now()) > 0) {
            throw IllegalStateException("Sent date is in the future.")
        }

        expectedArrivalDate?.let {
            if (it.compareTo(LocalDate.now()) < 0) {
                throw IllegalStateException("Arrival date is in the past.")
            }
        }

        if (totalStops < 0) {
            throw IllegalStateException("Total stops is negative.")
        }

        if (expectedMaxStops < 1) {
            throw IllegalStateException("Expected Max Stops is less than one.")
        }

        _lastStop?.let {
            for (thisChar in lastStop) {
                if (thisChar !in 'A'..'Z' && thisChar !in 'a'..'z' && thisChar !in '0'..'9') {
                    throw IllegalStateException("Last Stop is not alphanumeric.")
                }
            }
        }
    }
}
`

...we can use the Descriptions library to create an object describing the kind of `PackageTracker` we want to test...

`
    class PackageTrackerDescription: ObjectDescription<PackageTracker>() {
    val sentDate = DateFieldDescription(LocalDate.now(), UnlimitedDate)
    val expectedArrivalDate = DateFieldDescription(LocalDate.now(), UnlimitedDate)
    val totalStops = IntFieldDescription(3, IntLimitsDescription(0, 100))
    val expectedMaxStops = IntFieldDescription(5, IntLimitsDescription(1, 100))
    val addStopName = StringFieldDescription("Oceanside")

    override val describedObject: PackageTracker
        get() {
            sentDate.describedValue?.let { sentDate ->
                expectedMaxStops.describedValue?.let {maxStops ->
                    totalStops.describedValue?.let { stops ->
                        val result = PackageTracker(sentDate)
                        result.expectedArrivalDate = expectedArrivalDate.describedValue
                        result.expectedMaxStops = maxStops
                        result.totalStops = stops
                        return result
                    }
                }
            }

            throw InappropriateDescriptionException("Neither sentDate nor expectedMaxStops nor totalStops may be null.")
        }
}
`

The block of code below will go through all ttpes of Date field test cases the Descriptions library knows about...

`
            DateFieldTargets.values().forEach {
            reset(testDataGenerator)
            testDataGenerator.sentDate.target = it

            try {
                val testData = testDataGenerator.describedObject
                producedTests.add(PackageTrackerTest(
                        "PackageTracker: Sent Date ${it.toString()}",
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-03${subname.currentSubname}",
                        testData))
            } catch (dontCare: Throwable) {
                // Silently ignore inappropriate test cases
            }

            reset(testDataGenerator)
            testDataGenerator.expectedArrivalDate.target = it

            try {
                val testData = testDataGenerator.describedObject
                producedTests.add(PackageTrackerTest(
                        "PackageTracker: Expected Arrival Date ${it.toString()}",
                        "This test will show you what the candidate PackageTracker looks like and see if the verify() function throws an exception.",
                        "DES-04${subname.nextSubname}",
                        testData))
            } catch (dontCare: Throwable) {
                // Silently ignore inappropriate test cases
            }
        }
`

The complete list: *DEFAULT, NULL, HAPPY_PATH, EXPLICIT, MAXIMUM_POSSIBLE_VALUE, RANDOM_WITHIN_LIMITS, SLIGHTLY_BELOW_MAXIMUM, SLIGHTLY_ABOVE_MINIMUM, MINIMUM_POSSIBLE_VALUE, WELL_BEYOND_UPPER_LIMIT, SLIGHTLY_BEYOND_UPPER_LIMIT, AT_UPPER_LIMIT, SLIGHTLY_WITHIN_UPPER_LIMIT, WELL_WITHIN_UPPER_LIMIT, WELL_IN_FUTURE, SLIGHTLY_IN_FUTURE, AT_PRESENT, SLIGHTLY_IN_PAST, WELL_IN_PAST, WELL_BEYOND_LOWER_LIMIT, SLIGHTLY_BEYOND_LOWER_LIMIT, AT_LOWER_LIMIT, SLIGHTLY_WITHIN_LOWER_LIMIT, WELL_WITHIN_LOWER_LIMIT, FIVE_DIGIT_YEAR, FOUR_DIGIT_YEAR, THREE_DIGIT_YEAR, TWO_DIGIT_YEAR, SINGLE_DIGIT_YEAR, TWO_DIGIT_MONTH, SINGLE_DIGIT_MONTH, TWO_DIGIT_DAY, SINGLE_DIGIT_DAY, SINGLE_DIGIT_MONTH_AND_DAY*

For a more in-depth look, refer to the file [DescriptionExample.kt](https://github.com/william-hood/koarse-grind-kotlin/blob/master/src/test/kotlin/DescriptionExample.kt) in the [Koarse Grind](https://github.com/william-hood/koarse-grind-kotlin) project.

## Released under the terms of the MIT License
© 2020, 2021, 2022, 2023 William Hood

*Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished
to do so, subject to the following conditions:*

*The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.*

*THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.*


## Contact Information
https://www.linkedin.com/in/william-a-hood-pdx/

william.arthur.hood@gmail.com
