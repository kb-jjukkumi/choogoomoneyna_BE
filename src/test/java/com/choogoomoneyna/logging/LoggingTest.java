package com.choogoomoneyna.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggingTest.class);

    public static void main(String[] args) {
        logger.debug("디버그 메시지 출력");
        logger.info("정보 메시지 출력");
        logger.warn("경고 메시지 출력");
        logger.error("에러 메시지 출력");
    }
}
