package contacts;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Contact> contacts = new ArrayList<>();
    private static File file = null;

    public static void main(String[] args) {
        if (args.length > 0) {
            file = new File(args[0]);
            if (file.exists()) {
                loadContacts();
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Failed to create file: " + e.getMessage());
                }
            }
        }

        while (true) {
            System.out.println("[menu] Enter action (add, list, search, count, exit):");
            String action = scanner.nextLine().trim().toLowerCase();
            switch (action) {
                case "add":
                    addContact();
                    saveContacts();
                    break;
                case "list":
                    listContacts();
                    break;
                case "search":
                    searchContacts();
                    break;
                case "count":
                    System.out.println("The Phone Book has " + contacts.size() + " records.");
                    break;
                case "exit":
                    saveContacts();
                    return;
                default:
                    System.out.println("Unknown command!");
            }
            System.out.println(); // Add an empty line to separate actions
        }
    }

    private static void addContact() {
        System.out.println("Enter the type (person, organization):");
        String type = scanner.nextLine().trim().toLowerCase();
        if ("person".equals(type)) {
            System.out.println("Enter the name:");
            String name = scanner.nextLine().trim();
            System.out.println("Enter the surname:");
            String surname = scanner.nextLine().trim();
            System.out.println("Enter the birth date (YYYY-MM-DD):");
            String birthDate = scanner.nextLine().trim();
            System.out.println("Enter the gender (M, F):");
            String gender = scanner.nextLine().trim();
            System.out.println("Enter the number:");
            String phoneNumber = scanner.nextLine().trim();
            contacts.add(new Person(name, surname, birthDate, gender, phoneNumber));
            System.out.println("The record added.");
        } else if ("organization".equals(type)) {
            System.out.println("Enter the organization name:");
            String orgName = scanner.nextLine().trim();
            System.out.println("Enter the address:");
            String address = scanner.nextLine().trim();
            System.out.println("Enter the number:");
            String phoneNumber = scanner.nextLine().trim();
            contacts.add(new Organization(orgName, address, phoneNumber));
            System.out.println("The record added.");
        } else {
            System.out.println("Invalid type!");
        }
    }

    private static void listContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No records to display!");
            return;
        }
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
        System.out.println();
        System.out.println("[list] Enter action ([number], back):");
        String action = scanner.nextLine().trim().toLowerCase();
        if (action.matches("\\d+")) {
            int index = Integer.parseInt(action) - 1;
            if (index >= 0 && index < contacts.size()) {
                Contact contact = contacts.get(index);
                if (contact instanceof Person) {
                    Person person = (Person) contact;
                    System.out.println("Name: " + person.getName());
                    System.out.println("Surname: " + person.getSurname());
                    System.out.println("Birth date: " + (person.getBirthDate().isEmpty() ? "[no data]" : person.getBirthDate()));
                    System.out.println("Gender: " + (person.getGender().isEmpty() ? "[no data]" : person.getGender()));
                    System.out.println("Number: " + person.getPhoneNumber());
                    System.out.println("Time created: " + person.getTimeCreated());
                    System.out.println("Time last edit: " + person.getLastEditTime());
                } else if (contact instanceof Organization) {
                    Organization org = (Organization) contact;
                    System.out.println("Organization name: " + org.getOrgName());
                    System.out.println("Address: " + org.getAddress());
                    System.out.println("Number: " + org.getPhoneNumber());
                    System.out.println("Time created: " + org.getTimeCreated());
                    System.out.println("Time last edit: " + org.getLastEditTime());
                }
                System.out.println("[record] Enter action (edit, delete, menu):");
                String recordAction = scanner.nextLine().trim().toLowerCase();
                switch (recordAction) {
                    case "edit":
                        editContact(contact);
                        saveContacts();
                        break;
                    case "delete":
                        contacts.remove(index);
                        System.out.println("The record removed!");
                        saveContacts();
                        break;
                    case "menu":
                        return;
                    default:
                        System.out.println("Unknown command!");
                }
            }
        } else if ("back".equals(action)) {
            return;
        } else {
            System.out.println("Unknown command!");
        }
    }

    private static void searchContacts() {
        System.out.println("Enter search query:");
        String query = scanner.nextLine().trim().toLowerCase();
        List<Contact> results = new ArrayList<>();
        for (Contact contact : contacts) {
            String displayName = contact.getDisplayName().toLowerCase();
            String phoneNumber = contact.getPhoneNumber().toLowerCase();
            String fields = String.join(" ", contact.getFields()).toLowerCase();
            if (displayName.contains(query) || phoneNumber.contains(query) || fields.contains(query)) {
                results.add(contact);
            }
        }
        System.out.println("Found " + results.size() + " results:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i));
        }
        System.out.println("[search] Enter action ([number], back, again):");
        String action = scanner.nextLine().trim().toLowerCase();
        if (action.matches("\\d+")) {
            int index = Integer.parseInt(action) - 1;
            if (index >= 0 && index < results.size()) {
                Contact contact = results.get(index);
                if (contact instanceof Person) {
                    Person person = (Person) contact;
                    System.out.println("Name: " + person.getName());
                    System.out.println("Surname: " + person.getSurname());
                    System.out.println("Birth date: " + (person.getBirthDate().isEmpty() ? "[no data]" : person.getBirthDate()));
                    System.out.println("Gender: " + (person.getGender().isEmpty() ? "[no data]" : person.getGender()));
                    System.out.println("Number: " + person.getPhoneNumber());
                    System.out.println("Time created: " + person.getTimeCreated());
                    System.out.println("Time last edit: " + person.getLastEditTime());
                } else if (contact instanceof Organization) {
                    Organization org = (Organization) contact;
                    System.out.println("Organization name: " + org.getOrgName());
                    System.out.println("Address: " + org.getAddress());
                    System.out.println("Number: " + org.getPhoneNumber());
                    System.out.println("Time created: " + org.getTimeCreated());
                    System.out.println("Time last edit: " + org.getLastEditTime());
                }
                System.out.println("[record] Enter action (edit, delete, menu):");
                String recordAction = scanner.nextLine().trim().toLowerCase();
                switch (recordAction) {
                    case "edit":
                        editContact(contact);
                        saveContacts();
                        break;
                    case "delete":
                        contacts.remove(contact);
                        System.out.println("The record removed!");
                        saveContacts();
                        break;
                    case "menu":
                        return;
                    default:
                        System.out.println("Unknown command!");
                }
            }
        } else if ("back".equals(action)) {
            return;
        } else if ("again".equals(action)) {
            searchContacts();
        } else {
            System.out.println("Unknown command!");
        }
    }

    private static void editContact(Contact contact) {
        System.out.println("Select a field (" + String.join(", ", contact.getFields()) + "):");
        String field = scanner.nextLine().trim().toLowerCase();
        System.out.println("Enter " + field + ":");
        String newValue = scanner.nextLine().trim();
        contact.updateField(field, newValue);
        System.out.println("Saved");
    }

    private static void saveContacts() {
        if (file != null) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(contacts);
            } catch (IOException e) {
                System.out.println("Failed to save contacts: " + e.getMessage());
            }
        }
    }

    private static void loadContacts() {
        if (file != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                contacts.clear();
                contacts.addAll((List<Contact>) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load contacts: " + e.getMessage());
            }
        }
    }
}

abstract class Contact implements Serializable {
    protected String phoneNumber = "";
    protected LocalDateTime timeCreated;
    protected LocalDateTime lastEditTime;

    public Contact(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.timeCreated = LocalDateTime.now();
        this.lastEditTime = timeCreated;
    }

    public abstract String getDisplayName();

    public String getPhoneNumber() {
        return phoneNumber.isEmpty() ? "[no number]" : phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            this.phoneNumber = "";
        }
        updateLastEditTime();
    }

    protected boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        String regex = "^\\+?(\\([\\dA-Za-z]{2,}\\)|[\\dA-Za-z]{1,})([ -][\\dA-Za-z()]{2,})*$";
        if (!Pattern.matches(regex, phoneNumber)) {
            return false;
        }
        int parenthesisCount = phoneNumber.replaceAll("[^()]", "").length();
        if (parenthesisCount > 2) {
            return false;
        }
        if (phoneNumber.contains("()")) {
            return false;
        }
        String[] groups = phoneNumber.split("[ -]");
        boolean isFirstGroup = true;
        for (String group : groups) {
            if (group.isEmpty()) {
                return false;
            }
            if (isFirstGroup) {
                if (group.length() < 1 || group.length() > 20) {
                    return false;
                }
                isFirstGroup = false;
            } else {
                if (group.length() < 2 || group.length() > 20) {
                    return false;
                }
            }
        }
        return true;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public LocalDateTime getLastEditTime() {
        return lastEditTime;
    }

    protected void updateLastEditTime() {
        lastEditTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return getDisplayName() + ", " + getPhoneNumber();
    }

    // New methods for polymorphism
    public abstract List<String> getFields();

    public abstract void updateField(String field, String newValue);

    public abstract String getField(String field);
}

class Person extends Contact {
    private String name;
    private String surname;
    private String birthDate = "";
    private String gender = "";

    public Person(String name, String surname, String birthDate, String gender, String phoneNumber) {
        super(phoneNumber);
        this.name = name;
        this.surname = surname;
        setBirthDate(birthDate);
        setGender(gender);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateLastEditTime();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        updateLastEditTime();
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        if (isValidDate(birthDate)) {
            this.birthDate = birthDate;
        } else {
            System.out.println("Bad birth date!");
            this.birthDate = "";
        }
        updateLastEditTime();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if ("M".equalsIgnoreCase(gender) || "F".equalsIgnoreCase(gender)) {
            this.gender = gender.toUpperCase();
        } else {
            System.out.println("Bad gender!");
            this.gender = "";
        }
        updateLastEditTime();
    }

    @Override
    public String getDisplayName() {
        return name + " " + surname;
    }

    private boolean isValidDate(String date) {
        try {
            LocalDateTime.parse(date + "T00:00");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getFields() {
        return Arrays.asList("name", "surname", "birth", "gender", "number");
    }

    @Override
    public void updateField(String field, String newValue) {
        switch (field.toLowerCase()) {
            case "name":
                setName(newValue);
                break;
            case "surname":
                setSurname(newValue);
                break;
            case "birth":
                setBirthDate(newValue);
                break;
            case "gender":
                setGender(newValue);
                break;
            case "number":
                setPhoneNumber(newValue);
                break;
            default:
                System.out.println("Invalid field!");
        }
    }

    @Override
    public String getField(String field) {
        switch (field.toLowerCase()) {
            case "name":
                return getName();
            case "surname":
                return getSurname();
            case "birth":
                return getBirthDate();
            case "gender":
                return getGender();
            case "number":
                return getPhoneNumber();
            default:
                System.out.println("Invalid field!");
                return "";
        }
    }
}

class Organization extends Contact {
    private String orgName;
    private String address;

    public Organization(String orgName, String address, String phoneNumber) {
        super(phoneNumber);
        this.orgName = orgName;
        this.address = address;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
        updateLastEditTime();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        updateLastEditTime();
    }

    @Override
    public String getDisplayName() {
        return orgName;
    }

    @Override
    public List<String> getFields() {
        return Arrays.asList("address", "number");
    }

    @Override
    public void updateField(String field, String newValue) {
        switch (field.toLowerCase()) {
            case "address":
                setAddress(newValue);
                break;
            case "number":
                setPhoneNumber(newValue);
                break;
            default:
                System.out.println("Invalid field!");
        }
    }

    @Override
    public String getField(String field) {
        switch (field.toLowerCase()) {
            case "address":
                return getAddress();
            case "number":
                return getPhoneNumber();
            default:
                System.out.println("Invalid field!");
                return "";
        }
    }
}