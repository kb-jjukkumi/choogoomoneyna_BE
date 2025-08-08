package com.choogoomoneyna.choogoomoneyna_be.batch.tasklet;

import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchingStartTasklet implements Tasklet {

    private final MatchingService matchingService;

    /**
     * 매칭 시작 알고리줌울 자동으로 실행하는 배치 step 실행 로직
     **/
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("✅ 매칭 시작 알고리즘 자동 시작");

        matchingService.startAllMatching();

        return RepeatStatus.FINISHED;
    }
}
