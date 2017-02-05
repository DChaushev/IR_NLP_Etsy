# IR_NLP_Etsy

Install Gradle if necessary (and add to PATH).

Install Apache Tomcat if necessary.

Modify the `webAppsFolder` definition in `build.gradle` to point to your Apache Tomcat installation.

Modify the `BASE_INDEX_DIR` and `BASE_DATA_DIR` constants in `SimilarListingsRetriever.java`.

`BASE_DATA_DIR` should point to a directory that has `categories` and `listings` subdirectories.
Download and add the correspondig data JSON files there from [here](https://drive.google.com/drive/folders/0B5iTVLqLnNC6SlVHRXMxSkFheU0?usp=sharing).

Run the EtsyClusterizer project so the indexes are generated (you can do it through NetBeans if you install Gradle plugin and add the project).

In the root folder run `gradle build` - this will also copy the .war to the webserver folder.

Start the Apache Tomcat webserver & run the client.

For the client setup & run, follow the `README.md` in the `client` dir.
