
JC = javac
JAVA = java
BUILDDIR = build

SRC = $(shell find src -name '*.java' | tr '\n' ' ')
MAIN = 'org.xkqr.braceml.BraceML'
ARGV = ''

all: prebuild braceml

prebuild:
	@echo 'Creating build directory'
	@[ -d $(BUILDDIR) ] || mkdir $(BUILDDIR)

braceml:
	@echo 'Compiling Java code'
	$(JC) -d $(BUILDDIR) $(SRC)

run: all
	@echo 'Running compiled classes'
	$(JAVA) -cp $(BUILDDIR) $(MAIN) $(ARGV)

clean:
	@echo 'Removing build directory'
	rm -rf build


.PHONY: all clean prebuild run
