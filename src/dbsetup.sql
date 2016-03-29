#This table stores the hostel id with name
CREATE TABLE Hostels(
  hostel_id int AUTO_INCREMENT,
  name VARCHAR(255),
  PRIMARY KEY (hostel_id)
);
#This table stores Department id with name and code .. e.g. id:0 | name:Computer Science | code:CSE
CREATE TABLE Departments(
  department_id int AUTO_INCREMENT,
  name VARCHAR(255),
  code VARCHAR(5),
  PRIMARY KEY (department_id)
);
#This table stores different user groups. Example: group_id:0 | name: Admin || group_id:1 | name: Faculty etc
CREATE TABLE User_Groups(
  group_id int AUTO_INCREMENT,
  name VARCHAR(255),
  PRIMARY KEY (group_id)
);
#This table stores information about the signed up users
CREATE TABLE Users(
  user_id int AUTO_INCREMENT,
  group_id int, #primary group
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  login VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  department_id int,
  hostel_id int,
  entry_number VARCHAR(255),
  PRIMARY KEY (user_id),
  FOREIGN KEY (group_id) REFERENCES User_Groups(group_id),
  FOREIGN KEY (department_id) REFERENCES Departments(department_id),
  FOREIGN KEY (hostel_id) REFERENCES Hostels(hostel_id)
);

#This table links the hostels with the user ids of their wardens
CREATE TABLE Hostel_Wardens(
  hostel_id int,
  user_id int,
  FOREIGN KEY (hostel_id) REFERENCES Hostels(hostel_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

#This table links the HOD's of departments with the department id's
CREATE TABLE Head_of_Departments(
  department_id int,
  user_id int,
  FOREIGN KEY (department_id) REFERENCES Departments(department_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

#This table stores the details of the complaints submitted
CREATE TABLE Complaints(
  complaint_id int AUTO_INCREMENT,
  user_id int,
  title VARCHAR(255),
  discritption VARCHAR(255),
  image VARCHAR(255),
  date_submitted DATETIME,
  date_resolved DATETIME,
  status int, #0 means unresolved
  level int, #0 means institute,1 hostel, 2 individual
  image_loc int,
  PRIMARY KEY (complaint_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  FOREIGN KEY (image_loc) REFERENCES Images(image_loc)
);

#This table stores the details about the comments including timestamp
CREATE TABLE Comments(
  comment_id int AUTO_INCREMENT,
  user_id int,
  complaint_id int,
  detail VARCHAR(255),
  date_commented DATETIME,
  PRIMARY KEY (comment_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  FOREIGN KEY (comment_id) REFERENCES Complaints(complaint_id)
);

#This table stores the complaint which is upvoted by a user.
#If a user A upvotes Complaint C then the table stores C:A .
CREATE TABLE Upvotes(
  complaint_id int,
  user_id int,
  FOREIGN KEY (complaint_id) REFERENCES Complaints(complaint_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

#This table stores the complaint which is downvoted by a user.
#If a user A downvotes Complaint C then the table stores C:A .
CREATE TABLE Downvotes(
  complaint_id int,
  user_id int,
  FOREIGN KEY (complaint_id) REFERENCES Complaints(complaint_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

#This links tag ids with tag names for searching purposes
CREATE TABLE Tags(
  tag_id int AUTO_INCREMENT,
  tag_name VARCHAR(255),
  PRIMARY KEY (tag_id)
);

#This associates tag id's with complaint id's
CREATE TABLE Tag_Association(
  complaint_id int,
  tag_id int,
  FOREIGN KEY (complaint_id) REFERENCES Complaints(complaint_id),
  FOREIGN KEY (tag_id) REFERENCES Tags(tag_id)
);

#May be used in future.
CREATE TABLE Resolve_Association(
  group_id int,
  tag_id int,
  FOREIGN KEY (group_id) REFERENCES User_Groups(group_id),
  FOREIGN KEY (tag_id) REFERENCES Tags(tag_id)
);

#User id linked with complained id. For future use on GCM.
CREATE TABLE User_Follow(
  user_id int,
  complaint_id int,
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  FOREIGN KEY (complaint_id) REFERENCES Complaints(complaint_id)
);


CREATE UNIQUE INDEX UPC ON Upvotes(complaint_id, user_id);
CREATE UNIQUE INDEX DPC ON Downvotes(complaint_id, user_id);
CREATE UNIQUE INDEX lin ON Users(login);

#User Groups Specified
INSERT INTO User_Groups(name) VALUES ('admin');
INSERT INTO User_Groups(name) VALUES ('faculty');
INSERT INTO User_Groups(name) VALUES ('btech');
INSERT INTO User_Groups(name) VALUES ('mtech');

#Department names and codes specified
INSERT INTO Departments(name, code) VALUES ('Computer Science','CSE');

#Hostel names with id's
INSERT INTO Hostels(name) VALUES ('Udaigiri');
INSERT INTO Hostels(name) VALUES ('Vindhyachal');
INSERT INTO Hostels(name) VALUES ('Residential');

#Tag names with id's
INSERT INTO Tags(tag_name) VALUES ('Mess');
INSERT INTO Tags(tag_name) VALUES ('Electrical Appliances');
INSERT INTO Tags(tag_name) VALUES ('LAN');
INSERT INTO Tags(tag_name) VALUES ('Bathroom');

#Users for testing purposes
INSERT INTO Users(group_id, first_name, last_name, login, password, department_id, hostel_id, entry_number) VALUES (1,'Pankaj','Kumar','pankaj','cop290',1,1,'2014CS10245');
INSERT INTO Users(group_id, first_name, last_name, login, password, department_id, hostel_id, entry_number) VALUES (4,'Rishubh','Singh','2014CS50293','prqesf',1,2,'2014CS50293');
INSERT INTO Users(group_id, first_name, last_name, login, password, department_id, hostel_id, entry_number) VALUES (3,'Kritarth','','kritarth','pspspdf',1,1,'2014CS10230');
INSERT INTO Users(group_id, first_name, last_name, login, password, department_id, hostel_id, entry_number) VALUES (2,'Vinay','Riberio','vinay','password',1,3,NULL );
INSERT INTO Users(group_id, first_name, last_name, login, password, department_id, hostel_id, entry_number) VALUES (2,'SAK','Kumar','sak','col226',1,3,NULL );
INSERT INTO Hostel_Wardens(hostel_id, user_id) VALUES (1,4);
INSERT INTO Hostel_Wardens(hostel_id, user_id) VALUES (2,5);
INSERT INTO Complaints(user_id, title, discritption, image, date_submitted, date_resolved, status, level) VALUES (2,'LAN Access after 1:00am','Allow the same',NULL,'2016-03-11 13:23:34','2016-03-11 13:23:34',0,0);
INSERT INTO Complaints(user_id, title, discritption, image, date_submitted, date_resolved, status, level) VALUES (3,'tgrtvrtgAN Access after 1:00am','Allow the same',NULL,'2016-03-11 13:23:34','2016-03-11 13:23:34',0,0);
INSERT INTO Complaints(user_id, title, discritption, image, date_submitted, date_resolved, status, level) VALUES (2,'Leaking tap in XXX bathroom','Repair the same',NULL,NOW(),NULL,0,1);
INSERT INTO Complaints(user_id, title, discritption, image, date_submitted, date_resolved, status, level) VALUES (3,'Broken Fan','Repair the fan in room number WF11',NULL,'2016-02-11 13:23:34','2016-03-11 13:23:34',0,2);
INSERT INTO Complaints(user_id, title, discritption, image, date_submitted, date_resolved, status, level) VALUES (3,'Mess food is shit in Ud','??????????????',NULL,'2016-02-20 13:23:34','2016-05-11 13:23:34',0,1);
INSERT INTO Complaints(user_id, title, discritption, image, date_submitted, date_resolved, status, level) VALUES (2,'Mess food is shit in VD','??????????????',NULL,'2016-02-20 13:23:34','2016-05-11 13:23:34',0,1);
INSERT INTO Complaints(user_id, title, discritption, image, date_submitted, date_resolved, status, level) VALUES (4,'Mess food is shit everywhere','??????????????',NULL,'2016-02-20 13:23:34','2016-05-11 13:23:34',0,0);
INSERT INTO Tag_Association VALUES (1,3);
INSERT INTO Tag_Association VALUES (2,3);
INSERT INTO Tag_Association VALUES (3,4);
INSERT INTO Tag_Association VALUES (4,2);
INSERT INTO Tag_Association VALUES (5,1);
INSERT INTO Tag_Association VALUES (6,1);
INSERT INTO Tag_Association VALUES (7,1);
INSERT INTO Comments(user_id, complaint_id, detail, date_commented) VALUES (2,7,'True',NOW());
INSERT INTO Comments(user_id, complaint_id, detail, date_commented) VALUES (1,2,'Seconded',NOW());
INSERT INTO Upvotes VALUES (1,2);
INSERT INTO Upvotes VALUES (1,3);
INSERT INTO Upvotes VALUES (1,4);
INSERT INTO Upvotes VALUES (2,2);
INSERT INTO Upvotes VALUES (2,4);
INSERT INTO User_Follow(user_id, complaint_id) VALUES (3,4);
