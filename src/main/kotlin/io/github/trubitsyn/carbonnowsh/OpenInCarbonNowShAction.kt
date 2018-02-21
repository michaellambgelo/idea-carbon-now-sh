/*
 * Copyright 2018 Nikola Trubitsyn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.trubitsyn.carbonnowsh

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.util.io.URLUtil
import java.awt.datatransfer.DataFlavor

class OpenInCarbonNowShAction : AnAction() {

    companion object {
        const val CARBON_URL =
                "https://carbon.now.sh/" +
                "?bg=rgba(0,0,0,0)" +
                "&t=dracula" +
                "&l=auto&ds=true" +
                "&wc=true" +
                "&wa=true" +
                "&pv=43px" +
                "&ph=57px" +
                "&ln=false" +
                "&code="
    }

    override fun actionPerformed(e: AnActionEvent) {
        val context = e.dataContext
        val provider = PlatformDataKeys.COPY_PROVIDER.getData(context)

        provider?.let {
            it.performCopy(context)
            val contents = CopyPasteManager
                    .getInstance()
                    .getContents<String>(DataFlavor.stringFlavor)

            openInCarbonNowSh(contents, DefaultBrowsable())
        }
    }

    fun openInCarbonNowSh(contents: String?, browsable: Browsable) {
        if (contents != null && contents.isNotEmpty()) {
            browsable.browse(CARBON_URL + URLUtil.encodeURIComponent(contents))
        }
    }

    override fun update(e: AnActionEvent?) {
        val presentation = e?.presentation
        val context = e?.dataContext

        if (presentation == null || context == null) {
            return
        }

        val provider = PlatformDataKeys.COPY_PROVIDER.getData(context)
        val available = provider != null && provider.isCopyEnabled(context) && provider.isCopyVisible(context)
        presentation.isVisible = available
        presentation.isEnabled = available
    }
}