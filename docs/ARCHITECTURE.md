# One Moment - Data Layer Specification

## Purpose

Users save one meaningful memory each day.

A memory can contain:
- Memory text (required)
- Photo (optional)

Saved memories are used by:
- Timeline Screen
- Reflection Screen

---

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Room Database
- Repository Pattern
- StateFlow
- Coroutines
- Android Photo Picker

---

## Architecture

UI
→ ViewModel
→ Repository
→ Room Database

Room Database is the single source of truth.

---

## Memory Entity

MemoryEntity

Fields:

- id: Long
- memoryText: String
- photoUri: String?
- createdDate: String
- createdTime: String
- createdTimestamp: Long

---

## Home Screen

Inputs:

- memoryText (required)
- photoUri (optional)

On Save:

- Validate memoryText
- Save memory in Room
- Clear form after successful save

---

## Photo Handling

Use Android Photo Picker.

Store only:
- URI String

Do not store:
- Bitmap
- File bytes

photoUri may be null.

---

## Repository Responsibilities

MemoryRepository

Functions:

- saveMemory()
- getAllMemories()
- getMemoryById()
- deleteMemory()
- getRandomReflectionMemory()

Expose data using StateFlow.

---

## Timeline

Data Source:
- Room Database

Behavior:

- Display all memories
- Sort by createdTimestamp DESC
- Auto-update when data changes

Card Content:

- Date
- Photo (if present)
- Memory Text

---

## Reflection

Data Source:
- Room Database

Logic:

1. Load all memories
2. Exclude today's memory
3. If no eligible memory exists:
   - Show empty state
4. Shuffle memories
5. Select one memory
6. Generate title
7. Generate quote

---

## Reflection Titles

- Today remembered you.
- Remember this day?
- A quiet memory returned.
- Some moments deserve another smile.
- One of your beautiful days.
- A moment found its way back.

---

## Reflection Quotes

- The little moments become the big memories.
- Life is made of ordinary days remembered well.
- Keep what matters.
- Sometimes yesterday gives strength to today.
- Memories are proof that beautiful moments happened.

---

## Empty States

Timeline

Title:
No memories yet

Subtitle:
Start by saving your first moment.

Reflection

Title:
Nothing to reflect on yet

Subtitle:
Save a few memories and come back tomorrow.

---

## State Management

Use StateFlow.

Timeline and Reflection must automatically update when a memory is saved.

No manual refresh.
