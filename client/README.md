Setup:
1. Install the latest Node.js for your OS (comes with npm).

2. Type in a terminal `npm install -g cordova ionic`.

3. In the `client` directory type `npm install`.

4. In the `client/src/providers/listings-service.ts` file modifiy the `ETSY_API_KEY` constant to hold a valid key for the Etsy API.

5. If necessary also modify the `LUCENE_SIMILARITY_URL` constant in the same file to point to the endpoint of the deployed Lucene-based backend.




Run:
1. In the `client` directory type `ionic serve`.

2. If a browser does not automatically open, point one to `http://localhost:8100/`.