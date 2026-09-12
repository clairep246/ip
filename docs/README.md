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

Enter `add task` to start adding task mode.Enter `exit` to leave the adding task mode.

| Type of task                                                          | Purpose                                  |
|-----------------------------------------------------------------------|------------------------------------------|
| `todo <description>`                                                  | Add a to-do while in task-entry mode.    |
| `deadline <description> /by <YYYY-MM-DD HH:MM>`                       | Add a deadline while in task-entry mode. |
| `event <description> /from <YYYY-MM-DD HH:MM> /to <YYYY-MM-DD HH:MM>` | Add an event while in task-entry mode.   |

### Listing tasks: `list`

Use this command to display all saved tasks.

### Marking a task as done: `mark <task number`

Use this command to mark a task as completed.

### Marking a task as not done: `unmark <task number>`

Use this command to mark a completed task as incomplete again.

### Editing a task: `edit <task number>`

Enter `edit <task number>` to enter editing mode. The replacement must have the same type as the original task. 
Enter `exit` instead of a replacement to cancel the edit.

**Format:** `edit <task number>`

**Example:**

```text
edit 1
todo Read chapters 3 and 4
```

### Finding tasks: `search <keyword>`

Use this command to find tasks whose descriptions contain a keyword. The search is not case-sensitive.


### Deleting a task: `delete <task number>`

Use this command to permanently remove a task from the list.

### Echo mode: `echo`

Enter `echo` to make Bags repeat each message you enter. Enter `exit` to leave echo mode.

**Format:** `echo`

**Example:**

```text
echo
Hello, Bags!
```

### Saving and ending the session: `bye`

Use this command to save your tasks and end the session.

## Command Summary

| Command                                                               | Purpose                                   |
|-----------------------------------------------------------------------|-------------------------------------------|
| `add task`                                                            | Start task-entry mode.                    |
| `todo <description>`                                                  | Add a to-do while in task-entry mode.     |
| `deadline <description> /by <YYYY-MM-DD HH:MM>`                       | Add a deadline while in task-entry mode.  |
| `event <description> /from <YYYY-MM-DD HH:MM> /to <YYYY-MM-DD HH:MM>` | Add an event while in task-entry mode.    |
| `list`                                                                | Display all tasks.                        |
| `mark <task number>`                                                  | Mark a task as done.                      |
| `unmark <task number>`                                                | Mark a task as not done.                  |
| `edit <task number>`                                                  | Replace a task with one of the same type. |
| `search <keyword>`                                                    | Find tasks by description.                |
| `delete <task number>`                                                | Delete a task.                            |
| `echo`                                                                | Start echo mode.                          |
| `exit`                                                                | Leave task-entry, edit, or echo mode.     |
| `bye`                                                                 | Save tasks and end the session.           |
