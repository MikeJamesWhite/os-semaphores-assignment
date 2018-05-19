# Makefile for os-semaphores-assignment
# by Michael White
# 19/05/2018

JAVAC=javac
BIN=./bin
SRC=./src
OPTIONS = -cp $(BIN) -d $(BIN) -sourcepath $(SRC)
RUN_OPTIONS= -cp $(BIN)

.PHONY: clean all

all:
	mkdir -p bin
	$(JAVAC) $(OPTIONS) $(SRC)/*.java

clean:
	rm -r -f $(BIN)/*

run: all
	java $(RUN_OPTIONS) Simulator