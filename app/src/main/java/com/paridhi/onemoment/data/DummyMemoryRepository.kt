package com.paridhi.onemoment.data

object DummyMemoryRepository {
    fun getMemories(): List<Memory> = listOf(
        Memory(
            id = "1",
            title = "Morning coffee by the lake",
            description = "The water was perfectly still this morning. I spent an hour just watching the mist rise. A rare moment of absolute silence before the day started.",
            date = "15 July",
            day = "Tuesday"
        ),
        Memory(
            id = "2",
            title = "Finally finished the garden",
            description = "Planted the last of the sage and lavender today. The garden smells incredible now. Looking forward to seeing everything bloom.",
            date = "14 July",
            day = "Monday"
        ),
        Memory(
            id = "3",
            title = "Sunday stroll in the park",
            description = "The sun was warm but not overwhelming. Paridhi and I found a small hidden trail we'd never seen before. Nature has a way of surprising you.",
            date = "13 July",
            day = "Sunday"
        ),
        Memory(
            id = "4",
            title = "Rainy afternoon reading",
            description = "Stayed in with a good book while it poured outside. Sometimes the best moments are the ones where you do nothing at all.",
            date = "12 July",
            day = "Saturday"
        )
    )
}
