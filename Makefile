
.PHONY: docs
docs:
	plantuml -tpng -o . docs/*.puml
