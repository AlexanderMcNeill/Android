drop database if exists mcnear1_agile_manager;
create database mcnear1_agile_manager;

use mcnear1_agile_manager;

create table Member(
id int not null auto_increment,
firstName varchar(30) not null,
lastName varchar(30) not null,
userName varchar(30) unique not null,
pass varchar(50) not null,

primary key(id)
) engine=INNODB;

create table Team(
id int not null auto_increment,
name varchar(30) unique not null,
pass varchar(50) not null,

primary key(id)
) engine=INNODB;

create table TeamMember(
	memberID int not null,
	teamID int not null,
	
	primary key(memberID, teamID),

	foreign key(memberID)
	references Member(id),

	foreign key(teamID)
	references Team(id)
) engine=INNODB;

create table ScrumMeeting(
	id int not null auto_increment,
	teamID int not null,
	date date not null,
	barriersRemoved varchar(255),
	collabOpportunities varchar(255),

	foreign key(teamID)
	references Team(id),	

	primary key(id)
) engine=INNODB;

create table MemberScrum(
	id int not null auto_increment,
	scrumMeetingID int not null,
	memberID int not null,

	obsticles varchar(255),

	foreign key(scrumMeetingID)
	references ScrumMeeting(id),

	foreign key(memberID)
	references Member(id),

	primary key(id)
) engine=INNODB;

create table Goal(
	id int not null auto_increment,
	memberScrumID int not null,

	description varchar(255) not null,
	achieved boolean not null,
	comment varchar(255) not null,

	foreign key(memberScrumID)
	references MemberScrum(id),

	primary key(id)
) engine=INNODB;
