package Hospital_Management_System;

import java.util.*;
import java.io.*;

class AdminLogin {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    public static boolean login(Scanner sc) {
        System.out.println("===== Admin Login =====");
        System.out.print("Enter username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        if (user.equals(USERNAME) && pass.equals(PASSWORD)) {
            System.out.println("Login successful!\n");
            return true;
        } else {
            System.out.println("Invalid credentials.\n");
            return false;
        }
    }
}

class Patient {
    String name;
    int age;
    String gender;
    String disease;
    String assignedDoctor;
    String admissionDate;

    public Patient(String name, int age, String gender, String disease, String doctor, String date) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
        this.assignedDoctor = doctor;
        this.admissionDate = date;
    }

    public String toString() {
        return name + "," + age + "," + gender + "," + disease + "," + assignedDoctor + "," + admissionDate;
    }

    public static Patient fromString(String line) {
        String[] data = line.split(",");
        return new Patient(data[0], Integer.parseInt(data[1]), data[2], data[3], data[4], data[5]);
    }
}

class Doctor {
    String name;
    String specialization;

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public String toString() {
        return name + "," + specialization;
    }

    public static Doctor fromString(String line) {
        String[] data = line.split(",");
        return new Doctor(data[0], data[1]);
    }
}

public class HospitalManagementSystem {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        if (!AdminLogin.login(sc)) return;

        int choice;
        do {
            System.out.println("===== Hospital Management Menu =====");
            System.out.println("1. Add Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Delete Patient");
            System.out.println("4. Add Doctor");
            System.out.println("5. View Doctors");
            System.out.println("6. Search Patient");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: addPatient(); break;
                case 2: viewPatients(); break;
                case 3: deletePatient(); break;
                case 4: addDoctor(); break;
                case 5: viewDoctors(); break;
                case 6: searchPatient(); break;
                case 7: System.out.println("Exiting system..."); break;
                default: System.out.println("Invalid choice.\n");
            }
        } while (choice != 7);
    }

    static void addPatient() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Enter gender: ");
        String gender = sc.nextLine();
        System.out.print("Enter disease: ");
        String disease = sc.nextLine();
        System.out.print("Enter assigned doctor: ");
        String doctor = sc.nextLine();
        System.out.print("Enter admission date (dd-mm-yyyy): ");
        String date = sc.nextLine();

        Patient p = new Patient(name, age, gender, disease, doctor, date);
        try (FileWriter fw = new FileWriter("patients.txt", true)) {
            fw.write(p.toString() + "\n");
            System.out.println("Patient added successfully.\n");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    static void viewPatients() {
        System.out.println("===== Patient List =====");
        try (BufferedReader br = new BufferedReader(new FileReader("patients.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Patient p = Patient.fromString(line);
                System.out.println("Name: " + p.name + ", Age: " + p.age + ", Gender: " + p.gender + ", Disease: " + p.disease + ", Doctor: " + p.assignedDoctor + ", Admission: " + p.admissionDate);
            }
        } catch (IOException e) {
            System.out.println("No patient records found.\n");
        }
    }

    static void deletePatient() {
        System.out.print("Enter patient name to delete: ");
        String name = sc.nextLine();
        File inputFile = new File("patients.txt");
        File tempFile = new File("temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             FileWriter fw = new FileWriter(tempFile)) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                Patient p = Patient.fromString(line);
                if (!p.name.equalsIgnoreCase(name)) {
                    fw.write(p.toString() + "\n");
                } else {
                    found = true;
                }
            }
            if (inputFile.delete()) tempFile.renameTo(inputFile);
            if (found) System.out.println("Patient deleted successfully.\n");
            else System.out.println("Patient not found.\n");
        } catch (IOException e) {
            System.out.println("Error deleting patient.");
        }
    }

    static void addDoctor() {
        System.out.print("Enter doctor name: ");
        String name = sc.nextLine();
        System.out.print("Enter specialization: ");
        String spec = sc.nextLine();

        Doctor d = new Doctor(name, spec);
        try (FileWriter fw = new FileWriter("doctors.txt", true)) {
            fw.write(d.toString() + "\n");
            System.out.println("Doctor added successfully.\n");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    static void viewDoctors() {
        System.out.println("===== Doctor List =====");
        try (BufferedReader br = new BufferedReader(new FileReader("doctors.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Doctor d = Doctor.fromString(line);
                System.out.println("Name: " + d.name + ", Specialization: " + d.specialization);
            }
        } catch (IOException e) {
            System.out.println("No doctor records found.\n");
        }
    }

    static void searchPatient() {
        System.out.print("Enter patient name to search: ");
        String name = sc.nextLine();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("patients.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Patient p = Patient.fromString(line);
                if (p.name.equalsIgnoreCase(name)) {
                    System.out.println("\nPatient Found:");
                    System.out.println("Name: " + p.name + ", Age: " + p.age + ", Gender: " + p.gender + ", Disease: " + p.disease + ", Doctor: " + p.assignedDoctor + ", Admission: " + p.admissionDate + "\n");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Patient not found.\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading patient file.");
        }
    }
}
