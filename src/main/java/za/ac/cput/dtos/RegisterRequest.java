package za.ac.cput.dtos;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;

    // Student-specific
    private String studentNumber;
    private String faculty;
    private String department;
    private int yearOfStudy;

    // Organizer-specific
    private String organizationName;
    private String organizationType;
    private String organizationEmail;

    public RegisterRequest() {}

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
    public String getStudentNumber() { return studentNumber; }
    public String getFaculty() { return faculty; }
    public String getDepartment() { return department; }
    public int getYearOfStudy() { return yearOfStudy; }
    public String getOrganizationName() { return organizationName; }
    public String getOrganizationType() { return organizationType; }
    public String getOrganizationEmail() { return organizationEmail; }
}
