package com.induce.userservice.controller

import com.induce.userservice.dto.MyProfileResponse
import com.induce.userservice.dto.PublicProfileResponse
import com.induce.userservice.dto.UpdateProfileRequest
import com.induce.userservice.service.UserProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserProfileController(
    private val profileService: UserProfileService
) {

    @GetMapping("/me")
    fun getMe(
        @RequestHeader("X-Auth-User-Id") userId: UUID
    ): ResponseEntity<MyProfileResponse> {
        return ResponseEntity.ok(profileService.getMyProfile(userId))
    }

    @GetMapping("/{username}")
    fun getPublic(
        @PathVariable username: String
    ): ResponseEntity<PublicProfileResponse> {
        return ResponseEntity.ok(profileService.getPublicProfile(username))
    }

    @PutMapping("/me")
    fun updateMe(
        @RequestHeader("X-Auth-User-Id") userId: UUID,
        @RequestBody request: UpdateProfileRequest
    ): ResponseEntity<MyProfileResponse> {
        return ResponseEntity.ok(profileService.updateProfile(userId, request))
    }
}
