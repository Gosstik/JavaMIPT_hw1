package mipt.hw1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class StudentTest {
    @Test
    void testConstructors() {
        // By name.
        Student<Integer> st1 = new Student<Integer>("Name 1");

        // Name with marks.
        Student<Integer> st2 =
                new Student<Integer>("Name 2",
                                     new LinkedList<>(Arrays.asList(1, 2, 3)));

        // Name + marks + predicate.
        List<String> marks = new ArrayList<>(Arrays.asList("неуд", "уд", "хор", "отл"));
        Student<String> st =
                new Student<String>("Name 3", marks, marks::contains);
    }

    @Test
    void testEquals() {
        List<String> stringMarks = new ArrayList<>(Arrays.asList("неуд", "уд", "хор", "отл"));
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));

        Student<Integer> mikeInteger = new Student<>("Mike");
        Student<String> mikeString = new Student<>("Mike");
        Student<Integer> mikeMarks1 =
                new Student<>("Mike", integerMarks);
        Student<Integer> mikeMarks2 =
                new Student<>("Mike", integerMarks);
        Student<Integer> mikeMarksComp =
                new Student<>("Mike", integerMarks, integerMarks::contains);
        Student<Integer> peterInteger = new Student<>("Peter", integerMarks);

        Assertions.assertTrue(mikeInteger.equals(mikeInteger));

        Assertions.assertFalse(mikeInteger.equals(null));
        Assertions.assertFalse(mikeInteger.equals(stringMarks));

        Assertions.assertTrue(mikeInteger.equals(mikeString));
        Assertions.assertFalse(mikeInteger.equals(peterInteger));
        Assertions.assertTrue(mikeMarks1.equals(mikeMarks2));
        Assertions.assertTrue(mikeMarksComp.equals(mikeMarks1));
    }

    @Test
    void testHashCode() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));

        Student<Integer> mikeInteger = new Student<>("Mike");
        Student<String> mikeString = new Student<>("Mike");
        Student<Integer> mikeMarks1 =
                new Student<>("Mike", integerMarks);
        Student<Integer> mikeMarks2 =
                new Student<>("Mike", integerMarks);
        Student<Integer> mikeMarksComp =
                new Student<>("Mike", integerMarks, integerMarks::contains);

        Assertions.assertEquals(mikeInteger.hashCode(), mikeString.hashCode());
        Assertions.assertEquals(mikeMarks1.hashCode(), mikeMarks2.hashCode());
        Assertions.assertEquals(mikeMarks1.hashCode(), mikeMarksComp.hashCode());
    }

    @Test
    void testToString() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks);

        Assertions.assertEquals(mikeMarks.toString(), "Mike: [1, 2, 3]");
    }

    @Test
    void testGetName() {
        Student<Integer> mikeInteger = new Student<>("Mike");

        Assertions.assertEquals(mikeInteger.getName(), "Mike");
    }

    @Test
    void testSetName() {
        Student<Integer> mikeInteger = new Student<>("Mike");
        mikeInteger.setName("Peter");

        Assertions.assertEquals(mikeInteger.getName(), "Peter");
    }

    @Test
    void testGetMarks() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks);

        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);

        integerMarks.add(4);

        Assertions.assertNotEquals(mikeMarks.getMarks(), integerMarks);
    }

    @Test
    void testGetMark() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks);

        Assertions.assertEquals(mikeMarks.getMark(0), 1);
        Assertions.assertEquals(mikeMarks.getMark(2), 3);
    }

    @Test
    void testMarksCount() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks);

        Assertions.assertEquals(mikeMarks.marksCount(), 3);
    }

    @Test
    void testAddMarkObject() {
        final List<Integer> validMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks, validMarks::contains);

        mikeMarks.addMark(1);
        integerMarks.add(1);
        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);

        boolean catched = false;
        try {
            mikeMarks.addMark(4);
        } catch (IllegalArgumentException ignored) {
            catched = true;
        }
        Assertions.assertTrue(catched);
    }

    @Test
    void testAddMarkIndex() {
        final List<Integer> validMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks, validMarks::contains);

        mikeMarks.addMark(0,1);
        mikeMarks.addMark(2,2);
        mikeMarks.addMark(4,3);
        Assertions.assertEquals(mikeMarks.getMarks(),
                                new ArrayList<>(Arrays.asList(1, 1, 2, 2, 3, 3)));

        boolean catched = false;
        try {
            mikeMarks.addMark(0, 4);
        } catch (IllegalArgumentException ignored) {
            catched = true;
        }
        Assertions.assertTrue(catched);
    }

    @Test
    void testAddAllMarks() {
        final List<Integer> validMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(1, 2, 3));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks, validMarks::contains);

        mikeMarks.addAllMarks(integerMarks);
        Assertions.assertEquals(mikeMarks.getMarks(),
                new ArrayList<>(Arrays.asList(1, 2, 3, 1, 2, 3)));

        boolean catched = false;
        try {
            mikeMarks.addAllMarks(new ArrayList<>(Arrays.asList(1, 4, 2)));
        } catch (IllegalArgumentException ignored) {
            catched = true;
        }
        Assertions.assertTrue(catched);
    }

    @Test
    void testRemoveMarkObject() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(2, 4, 6));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks, (mark) -> mark % 2 == 0);

        Assertions.assertTrue(mikeMarks.removeMark(Integer.valueOf(6)));
        Assertions.assertNotEquals(mikeMarks.getMarks(), integerMarks);

        integerMarks.remove(Integer.valueOf(6));
        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);

        mikeMarks.addMark(2);
        integerMarks.add(2);
        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);

        Assertions.assertFalse(mikeMarks.removeMark(Integer.valueOf(100)));
        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);
    }

    @Test
    void testRemoveMarkIndex() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(2, 4, 6));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks, (mark) -> mark % 2 == 0);

        mikeMarks.removeMark(2);
        integerMarks.remove(2);

        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);

        mikeMarks.addMark(8);
        integerMarks.add(8);
        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);

        boolean catched = false;
        try {
            mikeMarks.removeMark(5);
        } catch (IndexOutOfBoundsException ignored) {
            catched = true;
        }
        Assertions.assertTrue(catched);
    }

    @Test
    void testRemoveAllMarks() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(2, 4, 6));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks, (mark) -> mark % 2 == 0);

        mikeMarks.removeAllMarks();
        Assertions.assertTrue(mikeMarks.getMarks().isEmpty());

        mikeMarks.addMark(8);
        Assertions.assertEquals(mikeMarks.getMarks(),
                                new ArrayList<>(List.of(8)));
    }

    @Test
    void testRevert() {
        List<Integer> integerMarks = new ArrayList<>(Arrays.asList(2, 4, 6));
        Student<Integer> mikeMarks =
                new Student<>("Mike", integerMarks, (mark) -> mark % 2 == 0);

        Assertions.assertFalse(mikeMarks.revert());

        mikeMarks.addMark(8);
        Assertions.assertTrue(mikeMarks.revert());
        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);

        mikeMarks.addAllMarks(integerMarks);
        Assertions.assertTrue(mikeMarks.revert());
        Assertions.assertEquals(mikeMarks.getMarks(), integerMarks);

        mikeMarks.setName("Peter");
        Assertions.assertTrue(mikeMarks.revert());
        Assertions.assertEquals(mikeMarks.getName(), "Mike");

        mikeMarks.addMark(8);
        mikeMarks.setName("Peter");
        mikeMarks.addMark(10);
        Assertions.assertTrue(mikeMarks.revert());
        Assertions.assertEquals(mikeMarks.getMarks(),
                                new ArrayList<>(Arrays.asList(2, 4, 6, 8)));
        Assertions.assertEquals(mikeMarks.getName(), "Peter");


    }
}
