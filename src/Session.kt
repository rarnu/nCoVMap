package com.rarnu.ncov

import com.rarnu.ktor.session
import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import java.util.*

data class NCoVMapSession(val uuid: String)

inline val PipelineContext<*, ApplicationCall>.localSession: NCoVMapSession
    get() = session {
        NCoVMapSession(UUID.randomUUID().toString())
    }
