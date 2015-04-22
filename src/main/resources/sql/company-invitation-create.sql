create table CompanyTempInvitation (userId varchar(255) not null,
	companyId varchar(255) not null,
	token varchar(255),
	createdDate timestamp,
	expiryDate timestamp not null,
	primary key (userId, companyId, token));
create unique index CompanyTempInvitationIndex on CompanyTempInvitation(userId, companyId, token);