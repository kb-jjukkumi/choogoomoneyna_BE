package com.choogoomoneyna.choogoomoneyna_be.batch.tasklet;

import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingMissionResultService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingService;
import com.choogoomoneyna.choogoomoneyna_be.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CodefWeeklyMissionTasklet implements Tasklet {

    private final MatchingMissionResultService matchingMissionResultService;
    private final MatchingService matchingService;
    private final MissionService missionService;

    /**
     * CODEF_WEEKLY 미션 자동 검증을 수행하는 배치 step의 실행 로직
     **/
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("✅ CODEF_WEEKLY 미션 자동 검증 시작");

        List<Long> userIds = matchingService.findAllUserIdInProgressMatching();

        for (Long userId : userIds) {
            Long matchingId = matchingService.getProgressMatchingIdByUserId(userId);
            List<Integer> missionIds = matchingMissionResultService.findMissionIdsByUserIdAndMatchingId(userId, matchingId);
            for (Integer missionId : missionIds) {
                String missionType = missionService.getMissionType(missionId);
                if(missionType.equals("CODEF_WEEKLY")) {
                    int limitAmount = missionService.getMissionLimitAmount(missionId);
                    int missionScore = missionService.getMissionScore(missionId);
                    matchingMissionResultService.validateMissionType1(userId, matchingId, missionId, missionScore, limitAmount);
                }
            }
        }

        return RepeatStatus.FINISHED;
    }
}
