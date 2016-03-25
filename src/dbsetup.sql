
CREATE TABLE Hostels(
  hostel_id int,
  name VARCHAR(255),
  PRIMARY KEY (hostel_id)
);
CREATE TABLE Departments(
  department_id int,
  name VARCHAR(255),
  code VARCHAR(5),
  PRIMARY KEY (department_id)
);
CREATE TABLE User_Groups(
  group_id int,
  name VARCHAR(255),
  PRIMARY KEY (group_id)
);
CREATE TABLE Users(
  user_id int,
  group_id int,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  password VARCHAR(255),
  department_id int,
  hostel_id int,
  entry_number VARCHAR(255),
  PRIMARY KEY (user_id),
  FOREIGN KEY (group_id) REFERENCES User_Groups(group_id),
  FOREIGN KEY (department_id) REFERENCES Departments(department_id),
  FOREIGN KEY (hostel_id) REFERENCES Hostels(hostel_id)
);
CREATE TABLE Hostel_Wardens(
  hostel_id int,
  user_id int,
  FOREIGN KEY (hostel_id) REFERENCES Hostels(hostel_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
CREATE TABLE Head_of_Departments(
  department_id int,
  user_id int,
  FOREIGN KEY (department_id) REFERENCES Departments(department_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
CREATE TABLE Complaints(
  complaint_id int,
  user_id int,
  title VARCHAR(255),
  discritption VARCHAR(255),
  date_submitted DATETIME,
  date_resolved DATETIME,
  status int, #0 means unresolved
  level int, #0 means institute,1 hostel, 2 individual
  PRIMARY KEY (complaint_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
CREATE TABLE Comments(
  comment_id int,
  user_id int,
  complaint_id int,
  detail VARCHAR(255),
  date_commented DATETIME,
  PRIMARY KEY (comment_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  FOREIGN KEY (comment_id) REFERENCES Complaints(complaint_id)
);
CREATE TABLE Upvotes(
  complaint_id int,
  user_id int,
  FOREIGN KEY (complaint_id) REFERENCES Complaints(complaint_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
CREATE TABLE Downvotes(
  complaint_id int,
  user_id int,
  FOREIGN KEY (complaint_id) REFERENCES Complaints(complaint_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

INSERT INTO User_Groups VALUES (0,'admin');
INSERT INTO User_Groups VALUES (1,'faculty');
INSERT INTO User_Groups VALUES (2,'btech');
INSERT INTO User_Groups VALUES (3,'mtech');
INSERT INTO Departments VALUES (0,'Computer Science','CSE');
INSERT INTO Hostels VALUES (0,'Udaigiri');
INSERT INTO Hostels VALUES (1,'Vindhyachal');
INSERT INTO Hostels VALUES (2,'Residential');
INSERT INTO Users VALUES (0,0,'Pankaj','Kumar','cop290',0,0,'2014CS10245');
INSERT INTO Users VALUES (1,3,'Rishubh','Singh','prqesf',0,1,'2014CS50293');
INSERT INTO Users VALUES (2,2,'Kritarth','','pspspdf',0,0,'2014CS10230');
INSERT INTO Users VALUES (3,1,'Vinay','Riberio','password',0,2,NULL );

INSERT INTO Complaints VALUES (0,1,'LAN Access after 1:00am','Allow the same','2016-03-11 13:23:34','2016-03-11 13:23:34',0,0);
INSERT INTO Complaints VALUES (1,2,'Broken Fan','Repair the fan in room number WF11','2016-02-11 13:23:34','2016-03-11 13:23:34',0,2);
INSERT INTO Complaints VALUES (2,2,'Mess food is shit','??????????????','2016-02-20 13:23:34','2016-05-11 13:23:34',0,1);

INSERT INTO Comments VALUES (0,0,1,'Seconded','2016-04-22');
INSERT INTO Upvotes VALUES (0,0);
INSERT INTO Upvotes VALUES (0,1);
