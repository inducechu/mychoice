package com.induce.userservice.exception

open class BaseException(
    val errorCode: ErrorCode,
    message: String
) : RuntimeException(message)

class UserNotFoundException(message: String) :
    BaseException(ErrorCode.USER_NOT_FOUND, message)

class BadRequestException(message: String) :
    BaseException(ErrorCode.BAD_REQUEST, message)

class AffiliationNotFoundException(id: Long) :
    BaseException(ErrorCode.AFFILIATION_NOT_FOUND, "Affiliation $id not found")

class ConflictException(message: String) :
    BaseException(ErrorCode.CONFLICT, message)

class ServiceUnavailableException(message: String) :
    BaseException(ErrorCode.SERVICE_UNAVAILABLE, message)
