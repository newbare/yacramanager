--default admin credential admin/admin
INSERT INTO users(
            id, createddate, lastmodifieddate, activationkey, enabled, password,
            profileimageurl, profileurl, socialproviderid, socialuser, socialuserid,
            email, createdby_id, lastmodifiedby_id)
    VALUES (nextval('hibernate_sequence'), current_date, current_date, NULL, true, '$2a$11$QuUqmRbaQ1lGtrTzzOgVKekMan6AT3mwZqzI4xXbYJKE9Fyk9OUYG',
            NULL, NULL, NULL, false, NULL,
            'admin@admin.com', NULL, NULL);

INSERT INTO personne(
            birthday, firstname, gender, lastname, id, contactid)
    VALUES (NULL, 'Admin', NULL, 'Admin', (SELECT id FROM users WHERE email='admin@admin.com'), NULL);

INSERT INTO company(
            id, createddate, lastmodifieddate, name, registereddate, 
            createdby_id, lastmodifiedby_id)
    VALUES (nextval('hibernate_sequence'), current_date, current_date, 'YACRAMANAGER', current_date, 
            (SELECT id FROM users WHERE email='admin@admin.com'), (SELECT id FROM users WHERE email='admin@admin.com'));

INSERT INTO companyaccountinfo(
            id, createddate, lastmodifieddate, expireddate, locked, createdby_id, 
            lastmodifiedby_id, company_id)
    VALUES (nextval('hibernate_sequence'), current_date, current_date, current_date + integer '365', false, (SELECT id FROM users WHERE email='admin@admin.com'), 
            (SELECT id FROM users WHERE email='admin@admin.com'), (select id FROM company where name='YACRAMANAGER'));
            
UPDATE company SET companyaccountinfo_id=(select max(id) from companyaccountinfo);

	
INSERT INTO employe(
            id, activeproject_id, company_id, manager_id)
    VALUES ((SELECT id FROM users WHERE email='admin@admin.com'), NULL, (select id FROM company where name='YACRAMANAGER'), NULL);

INSERT INTO users_roles(
            roleid, userid)
    VALUES ((SELECT id FROM role WHERE role='ROLE_ADMIN'), (SELECT id FROM users WHERE email='admin@admin.com'));