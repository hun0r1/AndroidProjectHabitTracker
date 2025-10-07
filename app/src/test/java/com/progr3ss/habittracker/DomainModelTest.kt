package com.progr3ss.habittracker

import com.progr3ss.habittracker.domain.model.Habit
import com.progr3ss.habittracker.domain.model.Schedule
import com.progr3ss.habittracker.domain.model.ScheduleStatus
import com.progr3ss.habittracker.domain.model.RepeatPattern
import org.junit.Assert.*
import org.junit.Test

class DomainModelTest {

    @Test
    fun `habit model should be created correctly`() {
        val habit = Habit(
            id = "1",
            userId = "user1",
            name = "Exercise",
            category = "Health",
            goal = "Exercise 30 minutes daily"
        )

        assertEquals("1", habit.id)
        assertEquals("user1", habit.userId)
        assertEquals("Exercise", habit.name)
        assertEquals("Health", habit.category)
        assertEquals("Exercise 30 minutes daily", habit.goal)
    }

    @Test
    fun `schedule model should be created correctly`() {
        val schedule = Schedule(
            id = "1",
            habitId = "habit1",
            habitName = "Exercise",
            startTime = "09:00",
            endTime = "10:00",
            repeatPattern = RepeatPattern.DAILY,
            status = ScheduleStatus.PENDING,
            date = "2024-01-01"
        )

        assertEquals("1", schedule.id)
        assertEquals("habit1", schedule.habitId)
        assertEquals("Exercise", schedule.habitName)
        assertEquals("09:00", schedule.startTime)
        assertEquals("10:00", schedule.endTime)
        assertEquals(RepeatPattern.DAILY, schedule.repeatPattern)
        assertEquals(ScheduleStatus.PENDING, schedule.status)
        assertEquals("2024-01-01", schedule.date)
    }

    @Test
    fun `schedule status enum should have all values`() {
        val statuses = ScheduleStatus.values()
        assertEquals(3, statuses.size)
        assertTrue(statuses.contains(ScheduleStatus.PENDING))
        assertTrue(statuses.contains(ScheduleStatus.IN_PROGRESS))
        assertTrue(statuses.contains(ScheduleStatus.COMPLETED))
    }

    @Test
    fun `repeat pattern enum should have all values`() {
        val patterns = RepeatPattern.values()
        assertEquals(4, patterns.size)
        assertTrue(patterns.contains(RepeatPattern.DAILY))
        assertTrue(patterns.contains(RepeatPattern.WEEKLY))
        assertTrue(patterns.contains(RepeatPattern.MONTHLY))
        assertTrue(patterns.contains(RepeatPattern.CUSTOM))
    }
}
