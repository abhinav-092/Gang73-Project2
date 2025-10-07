# Simple Makefile for JavaFX + PostgreSQL FXML App
# Assumes Makefile is outside the app/ folder

# Paths to JavaFX SDK and PostgreSQL JAR
JAVA_FX_LIB=~/javafx/javafx-sdk-25/lib
PG_DRIVER=$(JAVA_FX_LIB)/postgresql-42.7.8.jar

# Main class
MAIN=com.example.app.Main

# Source and resources (inside app/)
SRC=app/src/main/java
RES=app/src/main/resources

# Compile all Java files
all:
	javac --module-path $(JAVA_FX_LIB) --add-modules javafx.controls,javafx.fxml $(SRC)/com/example/app/*.java

# Run the application
run: all
	java --module-path $(JAVA_FX_LIB) --add-modules javafx.controls,javafx.fxml \
	-cp "$(SRC):$(RES):$(PG_DRIVER)" $(MAIN)

# Clean compiled class files
clean:
	rm -f $(SRC)/com/example/app/*.class
