package com.torresj.PS5Stock.scheduledTasks


import com.torresj.PS5Stock.config.ScheduledConfig
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(ScheduledConfig::class)
@ActiveProfiles("test")
class PS5StockCheckerTest() {
    @Test
    fun amazonCheckerTest(){}
}
