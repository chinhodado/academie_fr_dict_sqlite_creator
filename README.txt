Download the .prc version of the book here: http://www.ebooksgratuits.com/details.php?book=1609
Convert it to HTML:

    python mobihuff.py dictionnaire_academie_francaise_1932-35_8e_edition.prc output.html

Clean it up:

    node cleanup.js

Change the path to output.html inside MakeDict.java, and run it to get the academie.db file.