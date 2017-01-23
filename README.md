# IR_NLP_Etsy

Install Gradle if necessary (and add to PATH).

Install Apache Tomcat if necessary.

Modify the `webAppsFolder` definition in `build.gradle` to point to your Apache Tomcat installation.

Modify `INDEX_DIR` in `SimilarListingsRetriever.java`.

Modify `INDEX_DIR` and `EXAMPLE_LISTINGS_FOLDER` (the dir with data JSONs) in `Main.java`.

Run the EtsyClusterizer project so the index is generated (you can do it through NetBeans if you install Gradle plugin and add the project).

In the root folder do `gradle build` - this will also copy the .war to the webserver folder.

Start the Apache Tomcat webserver & run the client.

For the client setup & run, follow the `README.md` in the `client` dir.