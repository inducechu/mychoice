package com.induce.userservice.exception

open class BaseException(
    val errorCode: ErrorCode,
    message: String
) : RuntimeException(message)

class UserNotFoundException(message: String) :
    BaseException(ErrorCode.USER_NOT_FOUND, message)

class BadRequestException(message: String) :
    BaseException(ErrorCode.BAD_REQUEST, message)
