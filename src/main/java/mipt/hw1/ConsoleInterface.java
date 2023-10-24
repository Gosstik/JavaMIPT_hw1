package mipt.hw1;

import java.util.*;
import java.util.stream.Collectors;

public class ConsoleInterface {
    public enum InputOption {
        CREATE_STUDENT,
        ADD_MARK,
        REMOVE_MARK,
        PRINT_STUDENT,
        EXIT;
    }

    private static class InputOptionInfo {
        public InputOption option;
        public String name;

        public InputOptionInfo(InputOption option, String name ) {
            this.option = option;
            this.name = name;
        }
    }

    private final static Map<Integer, InputOptionInfo> inputMap = new TreeMap<>() {{
        put(1, new InputOptionInfo(InputOption.CREATE_STUDENT, "Create student"));
        put(2, new InputOptionInfo(InputOption.ADD_MARK, "Add mark"));
        put(3, new InputOptionInfo(InputOption.REMOVE_MARK, "Remove mark"));
        put(4, new InputOptionInfo(InputOption.PRINT_STUDENT, "Print student"));
        put(5, new InputOptionInfo(InputOption.EXIT, "Exit"));
    }};

    final static String chooseOptionText = """
            Choose option, put according value and press "enter".
            -------------------
            """ + generateOptionsText() + """
            -------------------
            Your value:\s""";

    final static String invalidOptionText = """
            Invalid option, you should put value from 1 to\s""" +
            inputMap.size() + """
            . Press "enter" to continue.
            """;

    private static String generateOptionsText() {
        return inputMap.keySet().stream()
                .map(key -> key + " - " + inputMap.get(key).name)
                .collect(Collectors.joining("\n", "", "\n"));
    }

    public static InputOption optionFromInteger(int num) {
        if (!inputMap.containsKey(num)) {
            throw new IllegalArgumentException();
        }
        return inputMap.get(num).option;
    }

    ////////////////////////////////////////////////////////////////////////////
    //////// Handle input options.

    public static Student<Integer> CreateStudent(Scanner in) {
        System.out.print("Put student name: ");

        Student<Integer> res =
                new Student<>(in.nextLine(),
                              new ArrayList<>(),
                              (mark) -> mark >= 1 && mark <= 10);

        System.out.println("Student \"" + res.getName() + "\" created.");
        return res;
    }

    public static void AddMark(Student<Integer> student, Scanner in) {
        System.out.print("Put mark to add: ");

        try {
            int mark = Integer.parseInt(in.nextLine());
            student.addMark(mark);
        } catch (IllegalArgumentException e) {
            System.out.print("""
                                 Invalid mark. Mark must be integer from 1 to 10.
                                 Press "enter" to continue.""");
            in.nextLine();
            return;
        }

        System.out.println("Mark " +
                           student.getMark(student.marksCount() - 1) +
                           " added to student \"" + student.getName() + "\".");
    }

    public static void RemoveMark(Student<Integer> student, Scanner in) {
        System.out.print("Put mark to remove: ");

        int mark;
        try {
            mark = Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            System.out.print("""
                                 Invalid mark. Mark must be integer from 1 to 10.
                                 Press "enter" to continue.""");
            in.nextLine();
            return;
        }

        if (student.removeMark(Integer.valueOf(mark))) {
            System.out.println("Mark was removed from student \"" + student.getName() + "\".");
        } else {
            System.out.println("Student \"" + student.getName() + "\" does not have such mark.");
        }
    }

    public static void PrintStudent(Student<Integer> student) {

    }

    public static void main(String[] args) {
        // Variables for reading from console.
        Scanner in = new Scanner(System.in);
        InputOption inputOption;

        System.out.println();
        Student<Integer> student = CreateStudent(in);

        mainLoop: while (true) {
            // Choose option.
            System.out.println();
            System.out.print(chooseOptionText);
            try {
                int num = Integer.parseInt(in.nextLine());
                inputOption = ConsoleInterface.optionFromInteger(num);
                if (inputOption == null) {
                    throw new NumberFormatException();
                }
            } catch (IllegalArgumentException e) {
                System.out.print(invalidOptionText);
                in.nextLine();
                continue;
            }

            switch (inputOption) {
                case CREATE_STUDENT -> student = CreateStudent(in);
                case ADD_MARK -> AddMark(student, in);
                case REMOVE_MARK -> RemoveMark(student, in);
                case PRINT_STUDENT -> System.out.println(student);
                case EXIT -> { break mainLoop; }
            }
        }
        System.out.println("Exiting.");
    }
}
