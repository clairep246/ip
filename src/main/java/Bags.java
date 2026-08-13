import java.util.ArrayList;
import java.util.Scanner;

public class Bags {
    private static final String divider =
            "____________________________________________________________";

    private static final String banner =
            " ____                  \n"
                    + "| __ )  __ _  __ _ ___ \n"
                    + "|  _ \\ / _` |/ _` / __|\n"
                    + "| |_) | (_| | (_| \\__ \\\n"
                    + "|____/ \\__,_|\\__, |___/\n"
                    + "               |___/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Object> store = new ArrayList<>();

        System.out.println(divider);
        System.out.println(banner);
        System.out.println(divider);
        System.out.println("Hello! I'm Bags. Nice to meet you!");
        System.out.println(divider);
        System.out.println("What can I do for you?");
        System.out.println(divider);

        String output = scanner.nextLine();
        System.out.println(divider);

        while (!output.equals("bye")) {
            if (output.equals("add task")) {
                System.out.println("Enter your task. Specify type of task in front, add /by for deadline task, add /from and /to for event task. To exit enter exit.");
                System.out.println(divider);

                output = scanner.nextLine();

                while (!output.equals("exit")) {
                    //TODO
                    if (output.startsWith("todo")) {

                        String[] temp = output.split(" ");

                        String name = temp[1];

                        ToDo newTodo = new ToDo(name);
                        store.add(newTodo);

                        System.out.println("Got it. I've added this task:");
                        System.out.println(newTodo.toString());
                        System.out.println("Now you have " + store.size() + " tasks in the list.");
                        System.out.println(divider);

                        // DEADLINE
                    } else if (output.startsWith("deadline")) {

                        String[] temp = output.split(" ");
                        int byIndex = -1;
                        for (int i = 0; i < temp.length; i++) {
                            if (temp[i].equals("/by")) {
                                byIndex = i;
                                break;
                            }
                        }
                        if (byIndex == -1) {
                            System.out.println("Missing /by.");
                            System.out.println(divider);

                        } else {
                            StringBuilder name = new StringBuilder(); //new string
                            for (int i = 1; i < byIndex; i++) {
                                name.append(temp[i]).append(" ");
                            }

                            StringBuilder deadline = new StringBuilder();
                            //after /by
                            for (int i = byIndex + 1; i < temp.length; i++) {
                                deadline.append(temp[i]).append(" ");
                            }

                            Deadlines newDeadline = new Deadlines(name.toString().trim(), deadline.toString().trim());
                            store.add(newDeadline);

                            System.out.println("Got it. I've added this task:");
                            System.out.println(newDeadline.toString());
                            System.out.println("Now you have " + store.size() + " tasks in the list.");
                            System.out.println(divider);
                        }

                        // EVENT
                    } else if (output.startsWith("event")) {

                        String[] temp = output.split(" ");
                        int fromIndex = -1;
                        int toIndex = -1;

                        for (int i = 0; i < temp.length; i++) {
                            if (temp[i].equals("/from")) {
                                fromIndex = i;
                            }
                            if (temp[i].equals("/to")) {
                                toIndex = i;
                                break;
                            }
                        }

                        if (fromIndex == -1 || toIndex == -1) {
                            System.out.println("Missing /from or /to.");

                        } else {
                            //name
                            StringBuilder name = new StringBuilder();
                            for (int i = 1; i < fromIndex; i++) {
                                name.append(temp[i]).append(" ");
                            }
                            //from
                            StringBuilder fromInfo = new StringBuilder();
                            for (int i = fromIndex + 1; i < toIndex; i++) {
                                fromInfo.append(temp[i]).append(" ");
                            }

                            // /to
                            StringBuilder toInfo = new StringBuilder();
                            for (int i = toIndex + 1; i < temp.length; i++) {
                                toInfo.append(temp[i]).append(" ");
                            }

                            Event newEvent = new Event(name.toString().trim(), fromInfo.toString().trim(), toInfo.toString().trim());
                            store.add(newEvent);

                            System.out.println("Got it. I've added this task:");
                            System.out.println(newEvent.toString());
                            System.out.println("Now you have " + store.size() + " tasks in the list.");
                            System.out.println(divider);
                        }
                    } else {
                        System.out.println("Not a valid task type");
                        System.out.println(divider);
                    }

                    output = scanner.nextLine();
                }
                System.out.println("Exited editing mode");
                System.out.println(divider);
            }
           else if (output.equals("list")) {
                if (store.isEmpty()) {
                    System.out.println("Your task list is empty.");
                } else {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < store.size(); i++) {
                        System.out.println((i + 1) + "." + store.get(i).toString());
                    }
                }
                System.out.println(divider);
                // MARK
            } else if (output.startsWith("mark ")) {

                String[] temp = output.split(" ");

                if (temp.length == 1) {
                    System.out.println("Missing task number.");
                } else {
                    int taskNumber = Integer.parseInt(temp[1]);
                    if (taskNumber <= 0 || taskNumber > store.size()) {
                        System.out.println("Task does not exist.");
                    } else {
                        Object task = store.get(taskNumber - 1);
                        if (task instanceof ToDo) {
                            ToDo toDo = (ToDo) task;
                            toDo.markDone();

                        } else if (task instanceof Deadlines) {
                            Deadlines deadlines = (Deadlines) task;
                            deadlines.markDone();

                        } else if (task instanceof Event) {
                            Event event = (Event) task;
                            event.markDone();
                        }

                        System.out.println("Marked task " + taskNumber + " as done.");
                    }
                }

                System.out.println(divider);

                // UNMARK
            } else if (output.startsWith("unmark ")) {
                String[] temp = output.split(" ");
                if (temp.length == 1) {
                    System.out.println("Missing task number.");
                } else {
                    int taskNumber = Integer.parseInt(temp[1]);
                    if (taskNumber <= 0 || taskNumber > store.size()) {
                        System.out.println("Task does not exist.");
                    } else {
                        Object task = store.get(taskNumber - 1);
                        if (task instanceof ToDo) {
                            ToDo toDo = (ToDo) task;
                            toDo.markUndone();

                        } else if (task instanceof Deadlines) {
                            Deadlines deadlines = (Deadlines) task;
                            deadlines.markUndone();

                        } else if (task instanceof Event) {
                            Event event = (Event) task;
                            event.markUndone();
                        }

                        System.out.println("Marked task " + taskNumber + " as undone.");
                    }
                }
                System.out.println(divider);

                // ECHO
            } else if (output.equals("echo")) {

                System.out.println("From now on I will echo your input. To exit enter exit.");
                System.out.println(divider);

                String echo = scanner.nextLine();

                while (!echo.equals("exit")) {
                    System.out.println(echo);
                    System.out.println(divider);

                    echo = scanner.nextLine();
                }

                System.out.println("Exited echo mode.");
                System.out.println(divider);

                // UNKNOWN
            } else {
                System.out.println("The command does not exist.");
                System.out.println(divider);
            }

            System.out.println("What can I do for you?");
            System.out.println(divider);

            output = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(divider);

        scanner.close();
    }
}