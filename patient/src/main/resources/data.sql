INSERT INTO patient (id, lastname, firstname, birthdate, gender, address, phone)
VALUES (1, "dupont", "nicolas", "1989-01-01", "m", "38 rue de la rivière", "+33634560001"),
       (2, "dupont", "aline", "1988-01-02", "f", "38 rue de la rivière", "+33634560002"),
       (3, "dupuis", "rené", "1950-01-03", "m", "50 rue dessot", "+33634560003"),
       (4, "messaoud", "salima", "1985-01-04", "f", "103 bis grand rue", "+33634560004"),
       (5, "tavares", "gérard", "1999-01-05", "m", "58, rue de l'adresse postal", "+33634560005"),
       (6, "dupont", "charlotte", "2000-01-06", "f", null, null),
       (7, "sabini", "sahra", "2002-01-07", "f", "103 bis grand rue", "+33634560007"),
       (8, "messaoud", "fatima", "2010-01-08", "f", "103 bis grand rue", "+33634560008"),
       (9, "messaoud", "hassan", "2012-01-09", "m", null, null)

ON DUPLICATE KEY
    UPDATE lastname  =
               values(lastname),
           firstname =
               values(firstname),
           birthdate =
               values(birthdate),
           gender    =
               values(gender),
           address   =
               values(address),
           phone     =
               values(phone);