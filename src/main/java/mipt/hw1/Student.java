package mipt.hw1;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;
import java.lang.Runnable;
import java.util.stream.Collectors;
import java.lang.IllegalArgumentException;

public final class Student<MarkType> {
    ////////////////////////////////////////////////////////////////////////////
    ////////                           API                              ////////
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    //////// Constructors.

    public Student(String name) {
        this(name, new ArrayList<MarkType>(), (mark) -> true);
    }

    public Student(String name,
                   List<? extends MarkType> marks) {
        this(name, marks, (mark) -> true);
    }

    public Student(String name,
                   List<? extends MarkType> marks,
                   Predicate<? super MarkType> marksPred) {
        this.name = name;
        this.marks = new ArrayList<>();
        this.marksPred = marksPred;

        marks.forEach(this::validateMark);
        this.marks.addAll(marks);
    }

    ////////////////////////////////////////////////////////////////////////////
    //////// Equals and hashCode.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student<?> student = (Student<?>) o;
        return Objects.equals(name, student.name)
                && Objects.equals(marks, student.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, marks);
    }


    ////////////////////////////////////////////////////////////////////////////
    //////// String representation.

    @Override
    public String toString() {
        return name + ": [" +
                marks.stream().map(Object::toString).collect(
                        Collectors.joining(", ")
                ) + "]";
    }

    ////////////////////////////////////////////////////////////////////////////
    //////// Handle name.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Save history.
        final String prevVal = this.name;
        revertFuncs.add(() -> this.name = prevVal);

        // Set value.
        this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    //////// Handle marks.

    public List<MarkType> getMarks() {
        return Collections.unmodifiableList(marks);
    }

    public MarkType getMark(int index) {
        return marks.get(index);
    }

    public int marksCount() {
        return marks.size();
    }

    public <T extends MarkType> void addMark(T newMark) {
        // Validate passed marks.
        validateMark(newMark);

        // Save history.
        revertFuncs.add(() -> marks.remove(this.marks.size() - 1));

        // Set value.
        marks.add(newMark);
    }

    public <T extends MarkType> void addMark(int index, T newMark) {
        // Validate passed marks.
        validateMark(newMark);

        // Save history.
        revertFuncs.add(() -> marks.remove(index));

        // Set value.
        marks.add(index, newMark);
    }

    public void addAllMarks(List<? extends MarkType> newMarks) {
        // Validate passed marks.
        newMarks.forEach(this::validateMark);

        // Save history.
        int newMarksSize = newMarks.size();
        revertFuncs.add(() -> {
            int leftToRemove = newMarksSize;
            for (int i = marks.size() - 1; leftToRemove > 0; --i, --leftToRemove) {
                 marks.remove(i);
            }
        });

        // Set values.
        marks.addAll(newMarks);
    }

    public boolean removeMark(MarkType mark) {
        // Find element to remove.

        int i;
        for (i = marks.size() - 1; i >= 0; --i) {
            if (marks.get(i).equals(mark)) {
                break;
            }
        }
        if (i == -1) {
            return false;
        }

        // Save history.
        final int index = i;
        final MarkType prevVal = mark;
        revertFuncs.add(() -> marks.add(index, prevVal));

        // Remove mark.
        marks.remove(i);
        return true;
    }

    public void removeMark(int index) {
        // Validate index.
        if (index < 0 || index >= marks.size()) {
            throw new IndexOutOfBoundsException();
        }

        // Save history.
        final MarkType prevVal = marks.get(index);
        revertFuncs.add(() -> marks.add(index, prevVal));

        // Remove mark.
        marks.remove(index);
    }

    public void removeAllMarks() {
        // Save history.
        final List<MarkType> prevVal = new ArrayList<>(marks);
        revertFuncs.add(() -> marks.addAll(prevVal));

        // Remove marks.
        marks.clear();
    }

    ////////////////////////////////////////////////////////////////////////////
    //////// Revert.

    /**
     * In case of adding new fields of class all we need to do is to add to
     * setter function code that adds lambda which reverts action to
     * {@code revertFuncs}.
     *
     * @return {@code true} if revert was done or {@code false} if object was
     * returned to the initial state.
     */

    public boolean revert() {
        if (revertFuncs.isEmpty()) {
            return false;
        }

        int lastPos = revertFuncs.size() - 1;
        revertFuncs.get(lastPos).run();
        revertFuncs.remove(lastPos);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////                       Internals.                           ////////
    ////////////////////////////////////////////////////////////////////////////

    private <T extends MarkType> void validateMark(T newMark) {
        if (!marksPred.test(newMark)) {
            String exceptionMessage = "Try insert invalid mark to student. \n" +
                    "Student: \n" +
                    this + "\n" +
                    "Invalid mark: \n" +
                    newMark.toString();
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //////// Class members.

    private String name;
    private final List<MarkType> marks;
    private final Predicate<? super MarkType> marksPred;
    private final List<Runnable> revertFuncs = new ArrayList<>();
}
