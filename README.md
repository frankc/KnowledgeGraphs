# KnowledgeGraphs
Data and code for building knowledge graphs to feed LLMs  
LLM World: LLM related terms and software stored as Turtle (ttl) triples

## Running the Project
- Download Java Runtime Environment 
- Download Java Development Kit to compile
- Download gradle
	- Homebrew: `brew install gradle` on Mac
	- Or install manually from gradle.org/install

- `cd` to project directory in terminal
- You may need to make the `gradlew` file executable using `chown -x ./gradlew` in terminal
- Run gradle project with `./gradlew bootRun` in terminal
- Go to localhost:8080 in web browser. The website should be there.
