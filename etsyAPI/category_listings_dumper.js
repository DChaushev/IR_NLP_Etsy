var https = require('https');
var fs = require('fs');
var _ = require('lodash');

var API_KEY = '';
var FIELDS = [
  'listing_id', 'state', 'user_id', 'title', 'creation_tsz', 'ending_tsz',
  'tags', 'category_path', 'category_path_ids', 'materials', 'views', 'num_favorers', 'is_supply',
  'occasion', 'style', 'has_variations', 'taxonomy_path', 'taxonomy_id'
];

var LISTINGS_BASE = '/listings/active';

var categories = [];

function readCategoriesFile(fileName) {
  console.log('Reading ' + fileName);
  var contents = fs.readFileSync(fileName);
  var jsonContents = JSON.parse(contents);
  console.log(jsonContents.length + ' records in ' + fileName);
  categories = jsonContents;
}

var results = [];
var count = 0;
var uniqueCount = 0;

function finishCategory(categoryIndex) {
  var categoryPath = categories[categoryIndex].category_name.split('/');
  console.log('\nFinished category ', categoryPath.join('/'));
  writeResults(categoryPath);
  console.log('Items so far: ', count, ', of which unique: ', uniqueCount);
  results = [];
  if (categoryIndex > 0) {
    console.log(categoryIndex + ' categories left.\n');
    getListingsFromCategory(categoryIndex-1, 0, 100);
  }
}

function writeResults(categoryPath) {
  var uniqResults = _.uniqBy(results, 'listing_id');
  count += results.length;
  uniqueCount += uniqResults.length;
  console.log('All count: ', results.length, '; Unqiue count: ', uniqResults.length);
  fs.writeFileSync('./' + categoryPath.join('.') + '.json', JSON.stringify(uniqResults, null, ' '), { flag : 'w' });
}

function printError(message) {
  console.log(message);
  fs.writeFileSync('./error', message);
}

var targetCount = 0;
var retriesLeft = 4;
var currentRetry = null;

function retry(categoryIndex, offset, limit, categoryPath) {
  --retriesLeft;
  if (retriesLeft >= 0) {
    console.log('Will retry in a bit. Retries left: ', retriesLeft);
    if (currentRetry !== null) {
      console.log('Canceling previous sheduled retry.');
      clearTimeout(currentRetry);
    }
    currentRetry = setTimeout(() => {
      console.log('Retrying.');
      getListingsFromCategory(categoryIndex, offset, limit);
    }, 8000);
  } else {
    console.log('No more retries left. Exiting.');
    // writeResults(categoryPath);
  }
}

function getListingsFromCategory(categoryIndex, offset, limit) {
  var categoryPath = categories[categoryIndex].category_name.split('/');

  var limitOffset = 'limit=' + limit + '&offset=' + offset;
  var apiKey = '&api_key=' + API_KEY;
  var fields = '&fields=' + FIELDS.join(',');
  var category = '&category=' + categoryPath.join('/');

  var params = '?' + limitOffset + apiKey + fields + category;
  var self = this;

  console.log('Category: ', categoryPath.join('/'), 'Offset: ' + offset);

  var request = https.get({
    protocol: 'https:',
    hostname: 'openapi.etsy.com',
    path: '/v2' + LISTINGS_BASE + params
  }, (res) => {
    var statusCode = res.statusCode;
    if (statusCode !== 200) {
      // consume response data to free up memory
      res.resume();
      printError('Request Failed.\n Status Code: ' + statusCode);
      retry(categoryIndex, offset, limit, categoryPath);
      return;
    }

    res.setEncoding('utf8');
    var rawData = '';
    res.on('data', (chunk) => rawData += chunk);
    res.on('end', () => {
      try {
        var parsedData = JSON.parse(rawData);
        results = results.concat(parsedData.results);
        targetCount = Math.min(parsedData.count, 500);
        if (offset === 0) {
          console.log('Expected count: ' + targetCount);
        }
        retriesLeft = 4;
        currentRetry = null;
        if (offset + limit < targetCount) {
          getListingsFromCategory(categoryIndex, offset + limit, limit);
        } else {
          finishCategory(categoryIndex);
        }
      } catch (e) {
        printError('Error: ' + e.message);
        writeResults(categoryPath);
      }
    });
  }).on('error', (e) => {
    printError('Got error: ' + e.message);
    retry(categoryIndex, offset, limit, categoryPath);
  });
  request.setTimeout(20000, () => {
    request.abort();
    printError('Timeout!');
    retry(categoryIndex, offset, limit, categoryPath);
  });
}

readCategoriesFile('categories.json');

getListingsFromCategory(categories.length, 0, 100);