save as "StudentGradeTracker.java"

mport java.util.ArrayList;
import java.util.Scanner;

class Student {
    String name;
    ArrayList<Double> grades;

    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) return 0;
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    public double getHighest() {
        if (grades.isEmpty()) return 0;
        double max = grades.get(0);
        for (double grade : grades) {
            if (grade > max) max = grade;
        }
        return max;
    }

    public double getLowest() {
        if (grades.isEmpty()) return 0;
        double min = grades.get(0);
        for (double grade : grades) {
            if (grade < min) min = grade;
        }
        return min;
    }

    public void printReport() {
        System.out.println("Student: " + name);
        System.out.println("Grades: " + grades);
        System.out.printf("Average: %.2f\n", getAverage());
        System.out.printf("Highest: %.2f\n", getHighest());
        System.out.printf("Lowest: %.2f\n", getLowest());
        System.out.println("---------------------------");
    }
}

public class StudentGradeTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Student> studentList = new ArrayList<>();

        System.out.println("==== Student Grade Tracker ====");
        while (true) {
            System.out.print("Enter student name (or type 'done' to finish): ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) break;

            Student student = new Student(name);

            while (true) {
                System.out.print("Enter grade for " + name + " (or type -1 to stop): ");
                double grade = scanner.nextDouble();
                if (grade == -1) break;
                student.addGrade(grade);
            }
            scanner.nextLine(); // consume newline
            studentList.add(student);
        }

        System.out.println("\n===== Summary Report =====");
        for (Student s : studentList) {
            s.printReport();
        }

        scanner.close();
    }
}

