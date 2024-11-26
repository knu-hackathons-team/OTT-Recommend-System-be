package com.software.ott.friend.controller;

import com.software.ott.common.dto.StringTypeMessageResponse;
import com.software.ott.friend.dto.FriendEmailRequest;
import com.software.ott.friend.dto.FriendRequestResponse;
import com.software.ott.friend.dto.FriendResponse;
import com.software.ott.friend.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
@Tag(name = "친구", description = "친구 관련 API")
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "친구 요청 보내기", description = "친구의 email을 기반으로 친구요청합니다.")
    @PostMapping
    public ResponseEntity<StringTypeMessageResponse> sendFriendRequest(@RequestAttribute("memberId") Long memberId, @RequestBody FriendEmailRequest friendEmailRequest) {
        friendService.makeFriendRequest(memberId, friendEmailRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new StringTypeMessageResponse("친구 요청이 생성되었습니다."));
    }

    @Operation(summary = "친구 확인", description = "본인의 친구들을 확인합니다.")
    @GetMapping
    public ResponseEntity<List<FriendResponse>> getAllFriends(@RequestAttribute("memberId") Long memberId) {
        List<FriendResponse> friendResponses = friendService.getAllFriend(memberId);
        return ResponseEntity.ok().body(friendResponses);
    }

    @Operation(summary = "대기중인 친구 요청들 확인", description = "본인에게 온 친구 요청 대기 목록을 확인합니다.")
    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestResponse>> getAllPendingFriendRequest(@RequestAttribute("memberId") Long memberId) {
        List<FriendRequestResponse> friendRequestResponses = friendService.getAllFriendRequests(memberId);
        return ResponseEntity.ok().body(friendRequestResponses);
    }

    @Operation(summary = "거절한 친구 요청들 확인", description = "본인에게 온 친구 요청 중 거절한 요청 목록을 확인합니다.")
    @GetMapping("/declined")
    public ResponseEntity<List<FriendRequestResponse>> getAllDeclinedFriendRequest(@RequestAttribute("memberId") Long memberId) {
        List<FriendRequestResponse> friendRequestResponses = friendService.getAllDeclinedFriends(memberId);
        return ResponseEntity.ok().body(friendRequestResponses);
    }

    @Operation(summary = "친구 요청 수락", description = "친구 요청을 수락합니다.")
    @PutMapping("/accept/{friendRequestId}")
    public ResponseEntity<StringTypeMessageResponse> acceptFriendRequest(@RequestAttribute("memberId") Long memberId, @PathVariable Long friendRequestId) {
        friendService.acceptFriendRequest(memberId, friendRequestId);
        return ResponseEntity.ok().body(new StringTypeMessageResponse("친구 요청이 수락되었습니다."));
    }

    @Operation(summary = "친구 요청 거절", description = "친구 요청을 거절합니다.")
    @PutMapping("/decline/{friendRequestId}")
    public ResponseEntity<StringTypeMessageResponse> declineFriendRequest(@RequestAttribute("memberId") Long memberId, @PathVariable Long friendRequestId) {
        friendService.declineFriendRequest(memberId, friendRequestId);
        return ResponseEntity.ok().body(new StringTypeMessageResponse("친구 요청이 거절되었습니다."));
    }

    @Operation(summary = "친구 또는 친구요청 삭제", description = "친구를 삭제합니다.")
    @DeleteMapping("/{friendRequestId}")
    public ResponseEntity<StringTypeMessageResponse> deleteFriend(@RequestAttribute("memberId") Long memberId, @PathVariable Long friendRequestId) {
        friendService.deleteFriend(memberId, friendRequestId);
        return ResponseEntity.ok().body(new StringTypeMessageResponse("친구가 삭제되었습니다."));
    }
}
