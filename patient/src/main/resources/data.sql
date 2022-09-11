INSERT INTO patient (id, lastname, firstname, birthdate, gender, address, phone)
VALUES (1, "Dupont", "Nicolas", "1989-01-01", "M", "38 rue de la rivière", "+33634560001"),
       (2, "Dupont", "Aline", "1988-01-02", "F", "38 rue de la rivière", "+33634560002"),
       (3, "Dupuis", "René", "1950-01-03", "M", "50 rue dessot", "+33634560003"),
       (4, "Messaoud", "Salima", "1985-01-04", "F", "103 bis grand rue", "+33634560004"),
       (5, "Tavares", "Gérard", "1999-01-05", "M", "58, rue de l'adresse postal", "+33634560005"),
       (6, "Dupont", "Charlotte", "2000-01-06", "F", null, null),
       (7, "Sabini", "Sahra", "2002-01-07", "F", "103 bis grand rue", "+33634560007"),
       (8, "Messaoud", "Fatima", "2010-01-08", "F", "103 bis grand rue", "+33634560008"),
       (9, "Messaoud", "Hassan", "2012-01-09", "M", null, null)

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