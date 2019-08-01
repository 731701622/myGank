package com.wkx.common.utils


enum class Code {
    SUCCESS, ING, ERROR, EMPTY, ERROR_LOAD_MORE, END
}

data class Status(val code: Code, val msg: String ?= "")