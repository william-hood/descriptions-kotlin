// Copyright (c) 2020, 2023, 2025 William Arthur Hood
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// of the Software, and to permit persons to whom the Software is furnished
// to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
package io.github.william_hood.descriptions_kotlin

import io.github.william_hood.toolbox_kotlin.ImproperConstructionException

val UnlimitedInt = IntLimitsDescription(Int.MIN_VALUE, Int.MAX_VALUE)

class IntLimitsDescription(lower: Int, upper: Int) : LimitsDescription<Int>(lower, upper) {

    init {
        if (this.lower >= this.upper) {
            throw ImproperConstructionException("Lower limit must not greater than, or equal to, the upper limit.")
        }
    }

    override fun contain(candidate: Int): Boolean {
        return candidate <= upper && candidate >= lower
    }

    override val containZero: Boolean
        get() = contain(0)
}