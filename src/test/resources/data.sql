INSERT INTO "day_name"  VALUES (1,1,'Monday');
INSERT INTO "day_name"  VALUES (2,2,'Tuesday');
INSERT INTO "day_name"  VALUES (3,3,'Wednesday');
INSERT INTO "day_name"  VALUES (4,4,'Thursday');
INSERT INTO "day_name"  VALUES (5,5,'Friday');
INSERT INTO "day_name"  VALUES (6,6,'Saturday');
INSERT INTO "day_name"  VALUES (7,7,'Sunday');

INSERT INTO "exercise"  VALUES (3,'Ćwiczenie 1','Ćwiczenie 1' );
INSERT INTO "exercise"  VALUES (4,'Ćwiczenie 2', 'Ćwiczenie 2');

INSERT INTO "role"  VALUES (1,'ROLE_USER');
INSERT INTO "role"  VALUES (2,'ROLE_ADMIN');

INSERT INTO "user"  VALUES (1,1,'test','test');

INSERT INTO "user_role"  VALUES (1,1);
INSERT INTO "user_role"  VALUES (1,2);

INSERT INTO "plan" ("id" , "name" ,"start_date", "weeks", "user_id") VALUES (3,'Plan 1','2024-01-09', 4, 1);
INSERT INTO "plan" ("id" , "name" ,"start_date", "weeks", "user_id") VALUES (4,'Plan 2', '2024-01-09', 4, 1);

INSERT INTO "training" VALUES (3,'Training 1','Training 1', 1);
INSERT INTO "training" VALUES (4,'Training 2','Training 2', 1);

INSERT INTO "plan_training" VALUES (3,3,3,4,4);
INSERT INTO "plan_training" VALUES (4,4,4,4,4);

INSERT INTO "training_exercise" VALUES (3,20,20,20,4,4);
INSERT INTO "training_exercise" VALUES (4,10,10,10,4,4);
