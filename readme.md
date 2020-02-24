# Above Herbalist Project
This version (v0.0.1) project was created in August 2018 by Andy Wu.
It was required by a Chinese Herbalist who needed to write Chinese Herb Prescription for 
his patients by finger other than by computer keyboard. 

### setup H2 datasource in application.properties
    datasource.driverClassName=org.h2.Driver
    datasource.username=xxx  
    datasource.password=xxx

when application is running, the database will create on c:/data/db/herbalistV2

### setup Squirrel SQL Client
    1) download and install "Squirrel SQL Clinet"
    2) download h2-latest.jar driver to c:/alw/alwInstall folder
    3) connect Squirrel to H2 drive at above location by setup Extra Class Path to 
       c:/alw/alwInstall/h2-latest.jar
    4) create Alias in Squireel with data url, user name and password

### H2 connection
    referer to http://h2database.com/html/features.html#connection_modes

### thymeleaf reference sites
    https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html
    https://medium.com/@grokwich/spring-boot-thymeleaf-html-form-handling-762ef0d51327

## Features and ToDo tasks:

This project focused on application features, did not focus on the programming language as time limited, for this reason, some of parts may contain Java 7 code in this project, but it will be upgraded to Java 8 later on.
#### 1) Language 
     support Chinese/English languages but default is Chinese
#### 2) security
     role: admin, be able to access everywhere in the system
     role: owner, be able to access "Pharmacy" menu, its submenus and other public menus
     role: doctor, be able to access "doctor" menu, its submenus and other public menus
#### 3) multiple tabs on a page to group similar functions together
     E.g. in all submenus of Doctor menu
#### 4) using an anchor to handle the tab location
     E.g. Update Decoction Method page
#### 5) feedback message
     after saving/updating, a successful message displays 5 seconds on a page,
     then disappearing. E.g. update doctor details page
#### 6) confirm message
     after “delete” button is clicked, a message will display on the page and ask for 
     confirmation. A trading way used JavaScript dialogue box (or light box), 
     but this project used JavaScript to dynamically populate a box in html element. 
     I think this solution is better.
#### 7) using @NotEmpty, @Range etc annotations to do interface field validation 
     Step1: defined @NotEmpty private String mobile; in DoctorDto1.java
     Step2: defined 
            public String createDoctorDetailPost(@Valid @ModelAttribute ("doctorCommand")
            DoctorDto1 doctor1, BindingResult result) {…} in DoctorController.java 
     step3: create view field: 
            <input path="mobile" style="width: 150px;" type='text' th:field='*{mobile}' 
            class="normal"/><span class="star">*</span> in doctorInputForm.html. 
     step4: create Error message in properties file
            NotEmpty.doctorCommand.mobile=请输入电话号码!
            In validationMessages_zh_CN.properties 
#### 8) using Wro to minify JavaScript and CSS to one file
     In web page, there are two files:
     all.css contained below files
        <css>/css/bootstrap/3.3.2/css/bootstrap.min.css</css>
        <css>/css/main.css</css>
        <css>/css/herb.css</css>
        <css>/css/style.css</css>
     all.js contains below files
        <js>/js/jquery-1.8.2.js</js>
        <js>/js/jquery-ui.js</js>
        <js>/js/herb.js</js>
        <js>/js/handlebars-1.0.rc.1.js</js>
        <js>/js/bootstrap.js</js>
        <js>/js/bootstrap-transition.js</js>
        <js>/js/bootstrap-filestyle.min.js</js>
        <js>/js/bootstrap-collapse.jss</js>
#### 9) 403/404 error pages were created to handling error or exception   
      403 page: when you log in as “doctor” user, then access “Pharmacy” page,
      system will forward to 403 page
#### 10) pdf file created
    Used html file as a PDF file template to create PDF file by Thymeleaf TemplateEngine. 
    I think this is better than what I used before to create PDF with FOP, iText/Jasper. 
    Because html template page is much easier to create and contain css as well.
#### 11) Bar chart created:
    Used JFree Char library to create 3D bar chart for statistics data
#### 12) searching result pagination used
    In show all patients detail page, the number of patients show in a page depended on
    the setup in doctor information page. 
#### 13) automatically generates “Created Date” and “Updated Date” for each table
    @PrePersist public void prePersist() {..}
    @PreUpdate  public void preUpdate() {…}
#### 14) sensitive data using byte characters or encrypted data:
    available_days in user table using byte characters and password using encrypted data.
#### 15) integration test and unit test for MVC Controller classes used
    BDD and TDD to drive development are used. 
    Controller class test is included
    Todo: integration and unit tests need to improve later as time limited. 
#### 16) ownership solutions for one-to-many and may-to-many entity used
    One-to-Many relationship:
    @OneToMany(fetch = FetchType.LAZY, mappedBy="clinic", cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<ClinicAddress> addresses = new ArrayList();

    public void addAddress(ClinicAddress address){
        addresses.add(address);
        address.setClinic(this);
    }

    // this method used to delete induvidule address only
    // for bulk delete it usded CascadeType.delete
    public void removeAddress(ClinicAddress address){
       //remove relationship, it become orphan object, so address will remove from db
         address.setClinic(null); 
         addresses.remove(address); 
    }

    similar as Many-to-Many relationship
##====TODO tasks in next phases ====
#### 17) bootstrap collapsible in pages used (phase 2)
    Home page needs to improve with bootstrap collapsible
#### 18) electronic signature (phase 2)
    Electronic Doctor signature will be created and populate in Prescription pdf file
#### 19) forget password function (phase 2)
#### 20) a scheduler will be used (phase 2)
     create a scheduler to generate quantity of Herb remainders for those herbs that are
     less than 5 kilograms in the Pharmacy every morning, and send MQ message or
     short text message to the Pharmacy owner.
#### 21) send email via log4j will be used (phase 3)
#### 22) create exception advices to handle exceptions (phase 3)
#### 23) 2-factor authentication (phase 3)
#### 24) shop validation (phase 3)
#### 25) change Shop to Clinic and system can be used for multiple clinics (phase 3)

