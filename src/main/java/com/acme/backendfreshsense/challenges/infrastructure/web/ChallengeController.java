package com.acme.backendfreshsense.challenges.infrastructure.web;

import com.acme.backendfreshsense.challenges.application.dto.ChallengeDto;
import com.acme.backendfreshsense.challenges.application.dto.LeaderboardEntryDto;
import com.acme.backendfreshsense.challenges.application.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping
    public List<ChallengeDto> getAll() {
        return challengeService.getAllChallenges();
    }

    @PostMapping("/{challengeId}/enroll")
    public ResponseEntity<Void> enroll(@PathVariable Long challengeId) {
        challengeService.enroll(challengeId, resolveUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{challengeId}/enroll")
    public ResponseEntity<Void> leave(@PathVariable Long challengeId) {
        challengeService.leave(challengeId, resolveUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{challengeId}/leaderboard")
    public List<LeaderboardEntryDto> getLeaderboard(@PathVariable Long challengeId) {
        return challengeService.getLeaderboard(challengeId);
    }

    private Long resolveUserId() {
        var auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getDetails();
    }
}
