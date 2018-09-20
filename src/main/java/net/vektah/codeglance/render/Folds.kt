/*
 * Copyright © 2013, Adam Scarr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.vektah.codeglance.render

import com.intellij.openapi.editor.FoldRegion

// Is a copy of Array<FoldRegion> that only contains folded folds and can be passed safely to another thread
class Folds{
    private val starts: ArrayList<Int>
    private val ends: ArrayList<Int>

    constructor(allFolds: Array<FoldRegion>) {
        starts = ArrayList()
        ends = ArrayList()
        allFolds
            .filterNot { it.isExpanded }
            .forEach { foldRegion ->
                if (ends.size == 0 || ends.last() < foldRegion.startOffset) {
                    starts.add(foldRegion.startOffset)
                    ends.add(foldRegion.endOffset)
                }
            }
    }

    // Used by tests that want an empty fold set
    constructor() {
        starts = ArrayList()
        ends = ArrayList()
    }

    /**
     * Checks if a given position is within a folded region
     * @param position  the offset from the start of file in chars
     * *
     * @return true if the given position is folded.
     */
    fun isFolded(position: Int): Boolean {
        val index = starts.binarySearch(position)
        return (0 <= index || (index != -1 && position < ends[-(index+1)-1])) // see binarySearch() docs
    }
}
