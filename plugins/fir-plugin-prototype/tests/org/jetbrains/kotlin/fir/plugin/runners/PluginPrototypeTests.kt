/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.plugin.runners

import org.jetbrains.kotlin.fir.plugin.services.ExtensionRegistrarConfigurator
import org.jetbrains.kotlin.fir.plugin.services.PluginAnnotationsProvider
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.builders.firHandlersStep
import org.jetbrains.kotlin.test.directives.FirDiagnosticsDirectives.ENABLE_PLUGIN_PHASES
import org.jetbrains.kotlin.test.directives.FirDiagnosticsDirectives.FIR_DUMP
import org.jetbrains.kotlin.test.frontend.fir.DisableLazyResolveChecksAfterAnalysisChecker
import org.jetbrains.kotlin.test.frontend.fir.handlers.FirResolveContractViolationErrorHandler
import org.jetbrains.kotlin.test.runners.AbstractFirDiagnosticTest
import org.jetbrains.kotlin.test.runners.codegen.AbstractFirBlackBoxCodegenTest

open class AbstractFirPluginBlackBoxCodegenTest : AbstractFirBlackBoxCodegenTest() {
    override fun configure(builder: TestConfigurationBuilder) {
        super.configure(builder)
        builder.commonFirWithPluginFrontendConfiguration()
    }
}

abstract class AbstractFirPluginDiagnosticTest : AbstractFirDiagnosticTest() {
    override fun configure(builder: TestConfigurationBuilder) {
        super.configure(builder)
        builder.commonFirWithPluginFrontendConfiguration()
    }
}

fun TestConfigurationBuilder.commonFirWithPluginFrontendConfiguration() {
    useAfterAnalysisCheckers(
        ::DisableLazyResolveChecksAfterAnalysisChecker,
    )

    firHandlersStep {
        useHandlers(
            ::FirResolveContractViolationErrorHandler,
        )
    }

    defaultDirectives {
        +ENABLE_PLUGIN_PHASES
        +FIR_DUMP
    }

    useConfigurators(
        ::PluginAnnotationsProvider,
        ::ExtensionRegistrarConfigurator
    )
}
