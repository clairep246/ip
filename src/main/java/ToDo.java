//Todo task type 

public class ToDo extends Task {

    public ToDo(String description) {
        super(description, Tasktype.TODO);
    }

    /** Creates a to-do task from add task command*/
    //Use AI to reconfigure method into each task type class
    public static ToDo fromCommand(String output) throws BagsException {
        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task description! Add info after the type of task");
        }

        StringBuilder name = new StringBuilder();
        for (int i = 1; i < temp.length; i++) {
            name.append(temp[i]).append(" ");
        }

        String description = name.toString().trim();
        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add info after the type of task");
        }

        return new ToDo(description);
    }

    @Override
    public String toString() {
        return "[T][" + super.getStatusIcon() + "] " + super.getDescription();
    }

    @Override
    public String parseEvent() {
        return "T | " + "[" + getStatusIcon() + "] | " + description;
    }

}
