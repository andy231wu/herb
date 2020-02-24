create sequence hibernate_sequence start with 1 increment by  1
create table clinic (clinic_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), chinese_name varchar2(64 char) not null, englist_name varchar2(64 char), remark varchar2(4000 char), primary key (clinic_id))
create table clinic_address (address_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), address1 varchar2(255 char) not null, address2 varchar2(255 char), country varchar2(255 char) not null, postcode varchar2(255 char) not null, state varchar2(255 char) not null, suburb varchar2(255 char) not null, clinic_id number(19,0), primary key (address_id))
create table doctor (doctor_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), business_hours varchar2(255 char), date_of_work varchar2(255 char), default_fee float, email varchar2(255 char), first_name varchar2(255 char), last_name varchar2(255 char), phone varchar2(255 char), sex varchar2(255 char), clinic_id number(19,0), primary key (doctor_id))
create table doctor_patient (doctor_id number(19,0) not null, patient_id number(19,0) not null)
create table patient (patient_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), dob timestamp, first_name varchar2(255 char), last_name varchar2(255 char), phone varchar2(255 char), primary key (patient_id))
create table prescription (prescription_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), medicines varchar2(255 char), symptom varchar2(255 char), fee float, patient_id number(19,0), primary key (prescription_id))
alter table patient add constraint UK_9gxe97j2ngjjvtkig6b6jvy91 unique (phone)
alter table clinic_address add constraint FKob22dukrqe66qu65xfw7je5e9 foreign key (clinic_id) references clinic
alter table doctor add constraint FKaqgufpq4bfr4au915m6u0s4dm foreign key (clinic_id) references clinic
alter table doctor_patient add constraint FKoee3w1lhncf3q845n6vatov1j foreign key (patient_id) references patient
alter table doctor_patient add constraint FK1vod5519v8g177r669gytdahb foreign key (doctor_id) references doctor
alter table prescription add constraint FKqrlh184tfvdi95erwl65p4xj3 foreign key (patient_id) references patient
create sequence hibernate_sequence start with 1 increment by  1
create table clinic (clinic_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), chinese_name varchar2(64 char) not null, englist_name varchar2(64 char), remark varchar2(4000 char), primary key (clinic_id))
create table clinic_address (address_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), address1 varchar2(255 char) not null, address2 varchar2(255 char), country varchar2(255 char) not null, postcode varchar2(255 char) not null, state varchar2(255 char) not null, suburb varchar2(255 char) not null, clinic_id number(19,0), primary key (address_id))
create table doctor (doctor_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), business_hours varchar2(255 char), date_of_work varchar2(255 char), default_fee float, email varchar2(255 char), first_name varchar2(255 char), last_name varchar2(255 char), phone varchar2(255 char), sex varchar2(255 char), clinic_id number(19,0), primary key (doctor_id))
create table doctor_patient (doctor_id number(19,0) not null, patient_id number(19,0) not null)
create table patient (patient_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), dob timestamp, first_name varchar2(255 char), last_name varchar2(255 char), phone varchar2(255 char), primary key (patient_id))
create table prescription (prescription_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), medicines varchar2(255 char), symptom varchar2(255 char), fee float, patient_id number(19,0), primary key (prescription_id))
alter table patient add constraint UK_9gxe97j2ngjjvtkig6b6jvy91 unique (phone)
alter table clinic_address add constraint FKob22dukrqe66qu65xfw7je5e9 foreign key (clinic_id) references clinic
alter table doctor add constraint FKaqgufpq4bfr4au915m6u0s4dm foreign key (clinic_id) references clinic
alter table doctor_patient add constraint FKoee3w1lhncf3q845n6vatov1j foreign key (patient_id) references patient
alter table doctor_patient add constraint FK1vod5519v8g177r669gytdahb foreign key (doctor_id) references doctor
alter table prescription add constraint FKqrlh184tfvdi95erwl65p4xj3 foreign key (patient_id) references patient
create sequence hibernate_sequence start with 1 increment by  1
create table clinic (clinic_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), chinese_name varchar2(64 char) not null, englist_name varchar2(64 char), remark varchar2(4000 char), primary key (clinic_id))
create table clinic_address (address_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), address1 varchar2(255 char) not null, address2 varchar2(255 char), country varchar2(255 char) not null, postcode varchar2(255 char) not null, state varchar2(255 char) not null, suburb varchar2(255 char) not null, clinic_id number(19,0), primary key (address_id))
create table doctor (doctor_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), business_hours varchar2(255 char), date_of_work varchar2(255 char), default_fee float, email varchar2(255 char), first_name varchar2(255 char), last_name varchar2(255 char), phone varchar2(255 char), sex varchar2(255 char), clinic_id number(19,0), primary key (doctor_id))
create table doctor_patient (doctor_id number(19,0) not null, patient_id number(19,0) not null)
create table patient (patient_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), dob timestamp, first_name varchar2(255 char), last_name varchar2(255 char), phone varchar2(255 char), primary key (patient_id))
create table prescription (prescription_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), medicines varchar2(255 char), symptom varchar2(255 char), fee float, patient_id number(19,0), primary key (prescription_id))
alter table patient add constraint UK_9gxe97j2ngjjvtkig6b6jvy91 unique (phone)
alter table clinic_address add constraint FKob22dukrqe66qu65xfw7je5e9 foreign key (clinic_id) references clinic
alter table doctor add constraint FKaqgufpq4bfr4au915m6u0s4dm foreign key (clinic_id) references clinic
alter table doctor_patient add constraint FKoee3w1lhncf3q845n6vatov1j foreign key (patient_id) references patient
alter table doctor_patient add constraint FK1vod5519v8g177r669gytdahb foreign key (doctor_id) references doctor
alter table prescription add constraint FKqrlh184tfvdi95erwl65p4xj3 foreign key (patient_id) references patient
create sequence hibernate_sequence start with 1 increment by  1
create table clinic (clinic_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), chinese_name varchar2(64 char) not null, englist_name varchar2(64 char), remark varchar2(4000 char), primary key (clinic_id))
create table clinic_address (address_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), address1 varchar2(255 char) not null, address2 varchar2(255 char), country varchar2(255 char) not null, postcode varchar2(255 char) not null, state varchar2(255 char) not null, suburb varchar2(255 char) not null, clinic_id number(19,0), primary key (address_id))
create table doctor (doctor_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), business_hours varchar2(255 char), date_of_work varchar2(255 char), default_fee float, email varchar2(255 char), first_name varchar2(255 char), last_name varchar2(255 char), phone varchar2(255 char), sex varchar2(255 char), clinic_id number(19,0), primary key (doctor_id))
create table doctor_patient (doctor_id number(19,0) not null, patient_id number(19,0) not null)
create table patient (patient_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), dob timestamp, first_name varchar2(255 char), last_name varchar2(255 char), phone varchar2(255 char), primary key (patient_id))
create table prescription (prescription_id number(19,0) not null, create_date timestamp, create_user varchar2(255 char), update_date timestamp, update_user varchar2(255 char), medicines varchar2(255 char), symptom varchar2(255 char), fee float, patient_id number(19,0), primary key (prescription_id))
alter table patient add constraint UK_9gxe97j2ngjjvtkig6b6jvy91 unique (phone)
alter table clinic_address add constraint FKob22dukrqe66qu65xfw7je5e9 foreign key (clinic_id) references clinic
alter table doctor add constraint FKaqgufpq4bfr4au915m6u0s4dm foreign key (clinic_id) references clinic
alter table doctor_patient add constraint FKoee3w1lhncf3q845n6vatov1j foreign key (patient_id) references patient
alter table doctor_patient add constraint FK1vod5519v8g177r669gytdahb foreign key (doctor_id) references doctor
alter table prescription add constraint FKqrlh184tfvdi95erwl65p4xj3 foreign key (patient_id) references patient
