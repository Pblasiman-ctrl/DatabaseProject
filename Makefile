# Define variables
JAVAC = javac
JAR = jar
SRCDIR = src
BUILDDIR = build
LIBDIR = jar  # Update LIBDIR to match the directory with the JAR files
APP_NAME = MyApplication
MAIN_CLASS = main.main

# List your Java source files
JAVA_SOURCES = $(wildcard $(SRCDIR)/*.java)

# List external JAR dependencies
JAR_DEPS = $(wildcard $(LIBDIR)/*.jar)

# Cross-platform handling of classpath (colon for Unix/Mac, semicolon for Windows)
ifeq ($(OS),Windows_NT)
    CLASSPATH = $(subst /,\,$(JAR_DEPS))
else
    CLASSPATH = $(shell echo $(JAR_DEPS) | tr ' ' ':')
endif

# Default target
all: clean compile jar

# Compile Java source files
compile: $(BUILDDIR)
	@if [ -n "$(JAR_DEPS)" ]; then \
		$(JAVAC) -d $(BUILDDIR) -cp $(CLASSPATH) $(JAVA_SOURCES); \
	else \
		$(JAVAC) -d $(BUILDDIR) $(JAVA_SOURCES); \
	fi

# Create the JAR file
jar: $(BUILDDIR)
	echo "Main-Class: $(MAIN_CLASS)" > $(BUILDDIR)/MANIFEST.MF
	$(JAR) cvfm $(BUILDDIR)/$(APP_NAME).jar $(BUILDDIR)/MANIFEST.MF -C $(BUILDDIR) .

# Create the build directory if it doesn't exist
$(BUILDDIR):
	mkdir -p $(BUILDDIR)


# Run the JAR file
run: jar
	$(JAVA) -jar $(BUILDDIR)/$(APP_NAME).jar

# Clean up compiled files and JAR
clean:
	rm -rf $(BUILDDIR)