package com.savvasdalkitsis.librephotos.log

import com.orhanobut.logger.Logger

fun log(msg: String, tag: String = "") {
    Logger.log(Logger.VERBOSE, tag, msg, null)
}