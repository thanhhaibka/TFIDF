mvn clean compile assembly:single
scp -P 2395 /home/pc/TFIDF/target/TFIDF-1.0.8-SNAPSHOT-jar-with-dependencies.jar haint@10.3.14.156:/home/haint/TFIDF

