package com.rewards.Rewards;

import com.rewards.Rewards.config.TestKafkaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestKafkaConfig.class)
class RewardsApplicationTests {

	@Test
	void contextLoads() {
	}

}
