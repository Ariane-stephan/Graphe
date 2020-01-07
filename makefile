
default : TP34.class Graphe.class Sommet.class

TP34.class : TP34.java
	javac TP34.java

Graphe.class : Graphe.java
	javac Graphe.java

Sommet.class : Sommet.java
	javac Sommet.java

Partition.class : Partition.java
	javac Partition.java

Paire.class : Paire.java
	javac Paire.java

Cluster.class : Cluster.java
	javac Cluster.java

clean :
	rm *.class
