INSERT INTO "exercise"  VALUES (3,'Ćwiczenie 1','Ćwiczenie 1' );
INSERT INTO "exercise"  VALUES (4,'Ćwiczenie 2', 'Ćwiczenie 2');

INSERT INTO "role"  VALUES (1,'ROLE_USER');
INSERT INTO "role"  VALUES (2,'ROLE_ADMIN');

INSERT INTO "user"  VALUES (1,1,'test','test');

INSERT INTO "user_role"  VALUES (1,1);
INSERT INTO "user_role"  VALUES (1,2);

INSERT INTO "plan" ("id" , "name" ,"start_date", "weeks", "user_id") VALUES (3,'Plan 1','2024-01-09', 4, 1);
INSERT INTO "plan" ("id" , "name" ,"start_date", "weeks", "user_id") VALUES (4,'Plan 2', '2024-01-09', 4, 1);