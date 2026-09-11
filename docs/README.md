# Bags User Guide

**Bags** is a chatbot that helps you keep track of to-do tasks, deadlines, and events.

## How to Use Bags

Type a command in the command box and press <kbd>Enter</kbd> to run it. Commands are case-sensitive.

Try these commands first:

- `add task` starts task-entry mode.
- `list` shows every task in your list.
- `search report` finds tasks whose descriptions contain `report`.
- `bye` saves your tasks and ends the session.


## Features

### Adding tasks

Enter `add task` to start adding task mode. Bags then accepts one task at a time until you enter `exit`.

**Example:**

```text
add task
```

#### Adding a to-do

Use a to-do for a task without a date or time.

**Format:** `todo <description>`

**Example:**

```text
todo Read chapter 3
```

#### Adding a deadline

Use a deadline for a task that must be completed by a specific date and time. Dates and times must use `YYYY-MM-DD HH:MM` in 24-hour time.

**Format:** `deadline <description> /by <YYYY-MM-DD HH:MM>`

**Example:**

```text
deadline Submit report /by 2026-09-30 23:59
```

#### Adding an event

Use an event for an activity with a start and end date and time. Dates and times must use `YYYY-MM-DD HH:MM` in 24-hour time.

**Format:** `event <description> /from <YYYY-MM-DD HH:MM> /to <YYYY-MM-DD HH:MM>`

**Example:**

```text
event Project meeting /from 2026-09-20 14:00 /to 2026-09-20 15:00
```

#### Leaving task-entry mode

**Format:** `exit`

### Listing tasks

Use this command to display all saved tasks.

**Format:** `list`

**Example:**

```text
list
```

### Marking a task as done

Use this command to mark a task as completed.

**Format:** `mark <task number>`

**Example:**

```text
mark 1
```

### Marking a task as not done

Use this command to mark a completed task as incomplete again.

**Format:** `unmark <task number>`

**Example:**

```text
unmark 1
```

### Editing a task

Use this command to replace a task. The replacement must have the same type as the original task. Enter `exit` instead of a replacement to cancel the edit.

**Format:** `edit <task number>`

**Example:**

```text
edit 1
todo Read chapters 3 and 4
```

### Finding tasks

Use this command to find tasks whose descriptions contain a keyword. The search is not case-sensitive.

**Format:** `search <keyword>`

**Example:**

```text
search report
```

### Deleting a task

Use this command to permanently remove a task from the list.

**Format:** `delete <task number>`

**Example:**

```text
delete 2
```

### Echo mode

Use this command to make Bags repeat each message you enter. Enter `exit` to leave echo mode.

**Format:** `echo`

**Example:**

```text
echo
Hello, Bags!
```

### Saving and ending the session

Use this command to save your tasks and end the session.

**Format:** `bye`

**Example:**

```text
bye
```

## Command Summary

| Command | Purpose |
| --- | --- |
| `add task` | Start task-entry mode. |
| `todo <description>` | Add a to-do while in task-entry mode. |
| `deadline <description> /by <YYYY-MM-DD HH:MM>` | Add a deadline while in task-entry mode. |
| `event <description> /from <YYYY-MM-DD HH:MM> /to <YYYY-MM-DD HH:MM>` | Add an event while in task-entry mode. |
| `list` | Display all tasks. |
| `mark <task number>` | Mark a task as done. |
| `unmark <task number>` | Mark a task as not done. |
| `edit <task number>` | Replace a task with one of the same type. |
| `search <keyword>` | Find tasks by description. |
| `delete <task number>` | Delete a task. |
| `echo` | Start echo mode. |
| `exit` | Leave task-entry, edit, or echo mode. |
| `bye` | Save tasks and end the session. |
